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
package io.github.steve4744.whatisthis.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.EquipmentSlot;

import io.github.steve4744.whatisthis.WhatIsThis;

public class WhatIsThisListener implements Listener {

	private final WhatIsThis plugin;

	public WhatIsThisListener(WhatIsThis plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onQueryBlock(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getHand().equals(EquipmentSlot.OFF_HAND) || !plugin.getSettings().isRightClickEnabled()) {
			return;
		}
		Player player = event.getPlayer();
		if (player.hasPermission("whatisthis.use") && player.getInventory().getItemInMainHand().getType() == plugin.getSettings().getClickItem()) {
			plugin.getDisplayHandler().getVisualMethod(event.getClickedBlock(), player);
		}
	}

	/**
	 * Checks if another plugin we are watching for is enabled
	 * @author CuddleTheif
	 */
	@EventHandler(priority = EventPriority.MONITOR)
    protected void onPluginEnable(PluginEnableEvent event) {
		this.plugin.getDataHandler().setEnabled(event.getPlugin().getName());
		if(event.getPlugin().getName().equals("ItemsAdder"))
			plugin.createIAHud();
    }

	/**
	 * Checks if another plugin we are watching for is disabled
	 * @author CuddleTheif
	 */
    @EventHandler(priority = EventPriority.MONITOR)
    protected void onPluginDisable(PluginDisableEvent event) {
		this.plugin.getDataHandler().setDisabled(event.getPlugin().getName());
		if(event.getPlugin().getName().equals("ItemsAdder"))
			plugin.removeIAHud();
    }
}
