
package com.thedemgel.regions;

import com.thedemgel.regions.annotations.OnTickParser;
import com.thedemgel.regions.command.PlayerCommands;
import com.thedemgel.regions.feature.Feature;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.list.linked.TLongLinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.Engine;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.RootCommand;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginLogger;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.concurrent.AtomicFloat;


public class Regions extends CommonPlugin {
	private Engine engine;
	private static Regions instance;
	private TicksPerSecondMonitor tpsMonitor;

	@Override
	public void onLoad() {
		setInstance(this);
		((PluginLogger) getLogger()).setTag(new ChatArguments(ChatStyle.RESET, "[", ChatStyle.GOLD, "Regions", ChatStyle.RESET, "] "));
		engine = getEngine();
		
		getLogger().info("loaded");
	}
	
	@Override
	public void onEnable() {
		//Commands
		final CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(this), new SimpleAnnotatedCommandExecutorFactory());
		final RootCommand root = engine.getRootCommand();
		root.addSubCommands(this, PlayerCommands.class, commandRegFactory);
		//root.addSubCommands(this, AdminCommands.class, commandRegFactory);

		engine.getEventManager().registerEvents(new PlayerListener(this), this);
		
		tpsMonitor = new TicksPerSecondMonitor();
		getEngine().getScheduler().scheduleSyncRepeatingTask(this, tpsMonitor, 0, 50, TaskPriority.CRITICAL);

		getLogger().log(Level.INFO, "v{0} enabled.", getDescription().getVersion());
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "v{0} disabled.", getDescription().getVersion());
	}
	
	private static void setInstance(Regions instance) {
		Regions.instance = instance;
	}
	
	public static Regions getInstance() {
		return instance;
	}
	
	public TicksPerSecondMonitor getTPDMonitor() {
		return tpsMonitor;
	}
	
	public static class TicksPerSecondMonitor implements Runnable {
		private static final int MAX_MEASUREMENTS = 20 * 60;
		private TLongLinkedList timings = new TLongLinkedList();
		private long lastTime = System.currentTimeMillis();
		private final AtomicFloat ticksPerSecond = new AtomicFloat(20);
		private final AtomicFloat avgTicksPerSecond = new AtomicFloat(20);

		@Override
		public void run() {
			long time = System.currentTimeMillis();
			timings.add(time - lastTime);
			lastTime = time;
			if (timings.size() > MAX_MEASUREMENTS) {
				timings.removeAt(0);
			}
			final int size = timings.size();
			if (size > 20) {
				TLongIterator i = timings.iterator();
				int count = 0;
				long last20 = 0;
				long total = 0;
				while (i.hasNext()) {
					long next = i.next();
					if (count > size - 20) {
						last20 += next;
					}
					total += next;
					count++;
				}
				ticksPerSecond.set(1000F / (last20 / 20F));
				avgTicksPerSecond.set(1000F / (total / ((float) size)));
			}
		}

		public float getTPS() {
			return ticksPerSecond.get();
		}

		public float getAvgTPS() {
			return avgTicksPerSecond.get();
		}
	}
}
