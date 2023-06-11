package io.github.steve4744.whatisthis.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerCustomHudWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerHudsHolderWrapper;
import io.github.steve4744.whatisthis.WhatIsThis;
import io.github.steve4744.whatisthis.display.Hud.ANCHOR;
import net.md_5.bungee.api.ChatMessageType;

/**
 * HUD for a single player for the block they are looking at
 * 
 * @author CuddleTheif
 */
public class IAPlayerHuds {

    private PlayerHudsHolderWrapper holder;
    private TextHud blockNameHud;
    private TextHud resourceNameHud;
    private TileHud backgroundHud;
    private String prevBlock = "";
    private WhatIsThis plugin;

    public IAPlayerHuds(WhatIsThis plugin, Player player)
    {
        this.plugin = plugin;

        // Create the holder for all the huds
        this.holder = new PlayerHudsHolderWrapper(player);

        // Setup the backgroung Hud
        backgroundHud = new TileHud(this.holder, "whatisthis:block", 
                                        new String[]{"whatisthis:background_start"},  new String[]{"whatisthis:background_middle"},  new String[]{"whatisthis:background_end"}, 
                                        ANCHOR.CENTER, plugin.getSettings().getIAHudOffset(), 0, plugin.getSettings().getDisplayTime());
        backgroundHud.hide();

        // Setup the name and resource huds
        blockNameHud = new TextHud(this.holder, "whatisthis:block_name", "whatisthis:block_", ANCHOR.LEFT, plugin.getSettings().getIAHudOffset(), plugin.getSettings().getDisplayTime());
        resourceNameHud = new TextHud(this.holder, "whatisthis:resource_name", "whatisthis:resource_", ANCHOR.LEFT, plugin.getSettings().getIAHudOffset(), plugin.getSettings().getDisplayTime());
        blockNameHud.hide();
        resourceNameHud.hide();

    }
    
	/**
	 * Shows the block on the HUD
	 * 
	 * @param block The name of the block being looked at
	 * @param resource The name of the plugin the block comes from
	 */
	public void showTarget(String block, String resource){

        // Check to see if it's just blank so hide
        if(block.trim().equals("")){
            this.hide();
            return;
        }

        // Check to see if it's the same block as before and still visible
        if(prevBlock.equals(resource+":"+block) && blockNameHud.isVisible()){
            this.show();
            return;
        }

        // clear and write the block name
        blockNameHud.clearImgs();
        blockNameHud.addText(block);

        // Clear and write the resource name
        resourceNameHud.clearImgs();
        resourceNameHud.addText("  "+(resource.equals("") ? "Minecraft" : resource));

        // Make the background as big as the bigger of the resource Name or block name
        backgroundHud.setWidth((resourceNameHud.width()>blockNameHud.width() ?  resourceNameHud.width() : blockNameHud.width())+plugin.getSettings().getIAHudPadding()*2);

        // Align the text to the background plus padding
        blockNameHud.setOffsetX(backgroundHud.getX(false)+plugin.getSettings().getIAHudPadding());
        resourceNameHud.setOffsetX(backgroundHud.getX(false)+plugin.getSettings().getIAHudPadding());

        // Draw all the huds, background first
        backgroundHud.draw();
        blockNameHud.draw();
        resourceNameHud.draw();
        
        // Add the buffer to the actual hud, save the block for checking for new, and update
        prevBlock = resource+":"+block;

        // Show the HUD
        this.show();

    }

    /**
     * Hides the hud for when it's not being used
     */
    private void hide(){
        backgroundHud.hide();
        blockNameHud.hide();
        resourceNameHud.hide();
    }

    private void show(){
        backgroundHud.show();
        blockNameHud.show();
        resourceNameHud.show();
        holder.sendUpdate();
    }

    
    public void checkUI(){
        backgroundHud.checkUI();
        blockNameHud.checkUI();
        resourceNameHud.checkUI();
    }

    /**
     * Cleanup the HUD for removal
     */
    public void cleanup(){
        backgroundHud.cleanup();
        blockNameHud.cleanup();
        resourceNameHud.cleanup();
        prevBlock = null;
    }

}
