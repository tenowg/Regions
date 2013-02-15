
package com.thedemgel.regions.util;

import gnu.trove.iterator.TLongIterator;
import gnu.trove.list.linked.TLongLinkedList;
import org.spout.api.util.concurrent.AtomicFloat;


public class TicksPerSecondMonitor implements Runnable {
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