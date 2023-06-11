package io.github.steve4744.whatisthis.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;

import io.github.steve4744.whatisthis.WhatIsThis;

/**
 * Listener used to listen for events that hide or show the IA HUD if Item Adder is installed
 */
public class ItemAdderListener implements Listener {
    
	private final WhatIsThis plugin;

	public ItemAdderListener(WhatIsThis plugin) {
		this.plugin = plugin;
	}

    /**
     * When Items Adder is loaded, load/reload the HUD
     */
    @EventHandler
    public void onItemAdderItemsLoad(ItemsAdderLoadDataEvent  e){
        plugin.getIAHudManager().reload();
    }

    /**
     * When a player enters enable the HUD for them
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        plugin.getIAHudManager().initPlayer(e.getPlayer());
        plugin.getSettings().initPlayer(e.getPlayer().getPersistentDataContainer());
    }

    /**
     * When a player leaves remove their HUD
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        plugin.getIAHudManager().removePlayer(e.getPlayer().getUniqueId());
    }

}
