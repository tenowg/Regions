package com.thedemgel.regions.annotations;

import com.thedemgel.regions.ChatStyle;
import com.thedemgel.regions.Regions;
import com.thedemgel.regions.exception.FeaturePermissionException;
import com.thedemgel.regions.exception.InvalidFeatureCommandException;
import com.thedemgel.regions.feature.Feature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Used to parse feature commands.
 */
public class FeatureCommandParser {

	/**
	 * Used to parse feature commands, throws FeaturePermissionException if the issuer of the command
	 * fails a permissions test.
	 * @param feature
	 * @param command
	 * @throws InvalidFeatureCommandException
	 * @throws FeaturePermissionException
	 */
	public final void parse(Feature feature, FeatureCommandArgs command) throws InvalidFeatureCommandException, FeaturePermissionException  {
		//Method[] methods = feature.getClass().getMethods();
		Method[] methods = feature.getClass().getDeclaredMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(FeatureCommand.class)) {
				// Check for Permissions
				if (method.isAnnotationPresent(FeatureCommandPermission.class)) {
					if (!feature.hasPermission(method.getAnnotation(FeatureCommandPermission.class).perm(), command)) {
						command.getPlayer().sendMessage(ChatStyle.RED + "You do not have permission to use that command.");
						throw new FeaturePermissionException(command.getPlayer().getName() + " does not have permission.");
					}
				}
				try {
					if (method.getAnnotation(FeatureCommand.class).alias().equalsIgnoreCase(command.getCommand())) {
						try {
							method.invoke(feature, command);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
							Regions.getInstance().getLogger().log(Level.SEVERE, ex.getMessage(), ex);
						}
					}
				} catch (Exception ex) {
					throw ex;
				}
			}
		}
	}
}
