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
package io.github.steve4744.whatisthis.display;

import dev.lone.itemsadder.api.ItemsAdder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.steve4744.whatisthis.WhatIsThis;

/**
 * Display Manager for the HUD display of the block using the ItemAdder plugin
 * 
 * @author CuddleTheif
 */
public class IAHudManager {

	private WhatIsThis plugin;

    static final String WARNING = "Please don't forget to regen your resourcepack using /iazip command.";

    private final Map<UUID, IAPlayerHuds> huds = new HashMap<>();
	private boolean needsIaZip;

	public IAHudManager(WhatIsThis plugin) {
		this.plugin = plugin;

		extractDefaultAssets();

		if(ItemsAdder.areItemsLoaded() && !needsIaZip)
			initAllPlayers();
	}

	/**
	 * Reloads all the huds
	 */
	public void reload(){
		needsIaZip = false;
		this.cleanup();
		this.initAllPlayers();
	}
	
	/**
	 * Shows the block on the HUD for the time
	 * 
	 * @param player The player looking at the block
	 * @param block The name of the block being looked at
	 * @param resource The name of the plugin the block comes from
	 */
	public void showTarget(Player player, String block, String resource){

		//try
		//{
			IAPlayerHuds hud = huds.get(player.getUniqueId());
			if(hud!=null)
				hud.showTarget(block, resource);
			else
				plugin.getLogger().warning("PLAYER NOT FOUND");
		/*}
		catch (NullPointerException e)
		{
			plugin.getLogger().warning(WARNING);
			return;
		}*/
	}

	/**
	 * Get all the online players and make their HUDs
	 */
	private void initAllPlayers()
    {
            for (Player player : Bukkit.getServer().getOnlinePlayers())
                if(!initPlayer(player))
					return;
    }

	/**
	 * Creates a hud for a player and adds it
	 * @param player
	 */
	public boolean initPlayer(Player player){

		if(!needsIaZip){

			try
			{
				IAPlayerHuds playerHud = new IAPlayerHuds(plugin, player);
				huds.put(player.getUniqueId(), playerHud);
				return true;
			}
			catch (NullPointerException e)
			{
				needsIaZip = true;
				plugin.getLogger().warning("ERROR:"+WARNING);
				for(StackTraceElement stack : e.getStackTrace())
					plugin.getLogger().warning(stack.toString());
				return false;
			}

		}
		else{
			plugin.getLogger().warning("NEED:"+WARNING);
			return false;
		}
	}

	/**
	 * Removes the given player and cleanups their hud
	 * @param player The player to remove
	 */
	public void removePlayer(UUID player){
		IAPlayerHuds hud = huds.get(player);
		if(hud!=null){
			huds.get(player).cleanup();
			huds.remove(player);
		}
	}

	/** 
	 * Clean up the HUDs for removal
	 */
	public void cleanup()
    {

		// Clean up all the huds
		Set<UUID> players = huds.keySet();
        for (UUID player : players)
		  huds.get(player).cleanup();
		huds.clear();

    }

	public void checkUI(UUID player)
    {
		huds.get(player).checkUI();
    }

	/** 
	 * Extracts the assets to be used in the HUD for IA (Needs to been iazipped before use)
 	 * Source: https://github.com/LoneDev6/RPGhuds/blob/master/src/main/java/dev/lone/rpghuds/core/RPGHuds.java
	 */
	private void extractDefaultAssets()
    {
        CodeSource src = WhatIsThis.class.getProtectionDomain().getCodeSource();
        if (src != null)
        {
            File itemsadderRoot = new File(plugin.getDataFolder().getParent() + "/ItemsAdder");

            URL jar = src.getLocation();
            ZipInputStream zip;
            try
            {
                plugin.getLogger().info(ChatColor.AQUA + "Extracting assets...");

                zip = new ZipInputStream(jar.openStream());
                while (true)
                {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null)
                        break;
                    String name = e.getName();
                    if (!e.isDirectory() && name.startsWith("data/"))
                    {
                        File dest = new File(itemsadderRoot, name);
                        if (!dest.exists())
                        {
                            FileUtils.copyInputStreamToFile(plugin.getResource(name), dest);
                            plugin.getLogger().info(ChatColor.AQUA + "       - Extracted " + name);
                            needsIaZip = true;
							plugin.getLogger().warning(WARNING);
                        }
                    }
                }
                plugin.getLogger().info(ChatColor.GREEN + "DONE extracting assets!");

            }
            catch (IOException e)
            {
                plugin.getLogger().severe("        ERROR EXTRACTING assets! StackTrace:");
                e.printStackTrace();
            }
        }
    }
}
