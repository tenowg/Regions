package com.thedemgel.regions.annotations;

import com.thedemgel.regions.feature.Feature;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.Spout;
import org.spout.api.chat.style.ChatStyle;

public class FeatureCommandParser {

	public void parse(Feature feature, FeatureCommandArgs command)  {
		//Method[] methods = feature.getClass().getMethods();
		Method[] methods = feature.getClass().getDeclaredMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(FeatureCommand.class)) {
				// Check for Permissions
				if (method.isAnnotationPresent(FeatureCommandPermission.class)) {
					if(!feature.hasPermission(method.getAnnotation(FeatureCommandPermission.class).perm(), command)) {
						command.getPlayer().sendMessage(ChatStyle.RED, "You do not have permission to use that command.");
						return;
					}
				}
				if (method.getAnnotation(FeatureCommand.class).alias().equalsIgnoreCase(command.getCommand())) {
					try {
						method.invoke(feature, command);
					} catch (IllegalAccessException ex) {
						Spout.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
					} catch (IllegalArgumentException ex) {
						Spout.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
					} catch (InvocationTargetException ex) {
						Spout.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
					}
				}
			}
		}
	}
}
