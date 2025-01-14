/*
 * MIT License

Copyright (c) 2019 steve4744

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */
package io.github.steve4744.whatisthis.commands;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import io.github.steve4744.whatisthis.WhatIsThis;
import io.github.steve4744.whatisthis.utils.Utils;

public class WhatIsThisCommand implements CommandExecutor {

	private final String version;
	private final WhatIsThis plugin;

	public WhatIsThisCommand(String version, WhatIsThis plugin) {
		this.version = version;
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String prefix = ChatColor.GREEN + "[WhatIsThis] " + ChatColor.WHITE;
		String infoMessage = prefix + "Version " + version + " : plugin by "+ ChatColor.AQUA + "steve4744";
		
		if (!sender.hasPermission("whatisthis.use")) {
			sender.sendMessage(prefix + "You do not have permission to run this command");
			return false;
		}
		if (args.length > 0) {

			// Check if it's always on and the user is trying to use the display command
			if ((sender instanceof Player) && 
					this.plugin.getSettings().isAutoDisplayEnabled() && 
					args[0].equalsIgnoreCase("toggledisplay")){

				// Make sure the user has permission to use this command
				if (!sender.hasPermission("whatisthis.use")) {
					sender.sendMessage(prefix + "You do not have permission to run this command");
					return false;
				}

				// Flip the hidden setting in the player
				PersistentDataContainer playerData = ((Player) sender).getPersistentDataContainer();
				boolean display = plugin.getSettings().isPlayerDisplay(playerData);
				plugin.getSettings().setPlayerDisplay(playerData, !display);
				sender.sendMessage(prefix + "Automatic Display toggled " + ChatColor.AQUA + (display ? "OFF" : "ON"));
				return true;

			}

			if (!sender.hasPermission("whatisthis.admin")) {
				sender.sendMessage(prefix + "You do not have permission to run this command");
				return false;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				plugin.reloadPlugin();
				sender.sendMessage(prefix + "Config reloaded");
				return true;
			} else if (args[0].equalsIgnoreCase("toggleclick")) {
				String action = "OFF";
				plugin.getSettings().toggleRightClick();
				if (plugin.getSettings().isRightClickEnabled()) {
					action = "ON";
				}
				sender.sendMessage(prefix + "Right-click toggled " + ChatColor.AQUA + action);
				return true;
			}
			sender.sendMessage(infoMessage);
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(infoMessage);
			return false;
		}
		Player player = (Player) sender;
		plugin.getDisplayHandler().getVisualMethod(Utils.getTargetBlock(player), player);
		return true;
	} 

}
