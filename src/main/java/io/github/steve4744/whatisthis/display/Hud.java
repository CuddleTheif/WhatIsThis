package io.github.steve4744.whatisthis.display;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerCustomHudWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerHudsHolderWrapper;
import io.github.steve4744.whatisthis.WhatIsThis;

/**
 * Basic Hud for displaying data on screen
 * 
 * @author CuddleTheif
 */
public class Hud extends PlayerCustomHudWrapper{

    private List<FontImageWrapper> imgBuffer;

    private BukkitTask hudHideSchedule;
    private PlayerHudsHolderWrapper holder;

    private int hideTime;
    private int width;
    private int height;
    private int anchorOffset;
    private int realOffset;
    private boolean changed;
    private ANCHOR anchor;

    /**
     * Creates a basic hud with the holder
     * 
     * @param plugin The plugin that made this HUD
     * @param holder The holder of the player who sees this HUD
     * @param namespaceID The id of the hud in IA
     * @param images The images of the HUD (If any to start with, can be null for none)
     * @param anchor The anchor of the hud that the X position is at (LEFT, MIDDLE, or RIGHT)
     * @param offsetX The x offset of the left of this hud
     * @param hideTime The amount of time in ticks this hud is visible until it auto hides (set to -1 for no auto hide)
     */
    public Hud(PlayerHudsHolderWrapper holder, String namespaceID, @Nullable String[] images, ANCHOR anchor, int offsetX, int hideTime)
    {
        // Setup the hud
        super(holder, namespaceID);

        // Store and initalize variables
        this.holder = holder;
        this.anchor = anchor;
        this.hideTime = hideTime;
        this.changed = false;
        this.width = 0;
        this.height = 0;
        this.imgBuffer = new ArrayList<>();

        // Add any starting images if given
        if(images!=null){
            for(String img : images){
                FontImageWrapper imageWrapper = new FontImageWrapper(img);
                if(imageWrapper.exists()){
                    this.imgBuffer.add(imageWrapper);
                    int height = imageWrapper.getHeight();
                    if(height>this.height)
                        this.height = height;
                }
                else
                    WhatIsThis.instance.getLogger().warning("The font image "+img+" couldn't be found! Make sure to iareload and iazip before creating a HUD. Skipping this image");
            }
        }

        // Update the offset based on the width, given offset, and anchor
        this.update();


    }

    /**
     * Sets the offset of the HUD relative to it's anchor using the imgBuffer to calculate width
     */
    @Override
    public void setOffsetX(int offset){

        // Update the class variable
        this.anchorOffset = offset;

        // Update the hud's size and offset
        this.update();
        
    }

    /**
     * Adds an image to this HUD's buffer. Call draw() to draw all current images
     * 
     * @param image The image to add to the buffer
     */
    public void addImage(FontImageWrapper image){
        imgBuffer.add(image);
        this.changed = true;
    }

    /**
     * Clears all the images from the HUD's img buffer. NOTE: This does not clear the actual HUD, call cleanup or hide for that
     */
    public void clearImgs(){
        imgBuffer.clear();
        this.changed = true;
    }

    /**
     * Draws the current img buffer to the screen
     */
    public void draw(){
        this.setFontImages(imgBuffer);
        this.holder.recalculateOffsets();
    }

    /**
     * Get the current width of the whole HUD
     */
    public int width(){

        if(this.changed)
            update();
        return this.width;

    }

    /**
     * Get the current height of the whole HUD
     */
    public int height(){

        if(this.changed)
            update();
        return this.height;

    }

    /**
     * Get the x positon of this window
     * 
     * @param useAnchor If to use the anchor or just get the top left of the hud
     */
    public int getX(boolean useAnchor){
        
        if(this.changed)
            update();
        return useAnchor ? this.anchorOffset : this.realOffset;

    }

    /**
     * Updates the HUD's position based on the anchor, width, and inital offset
     */
    private void update(){
        
        // Calculate the new width and height if changed
        if(this.changed){
            this.width = 0;
            this.height = 0;
            for(FontImageWrapper img : imgBuffer){
                this.width += img.getWidth();
                int height = img.getHeight();
                if(height>this.height)
                    this.height = height;
            }
            
        }


        // Update the offset based on the width and anchor
        this.realOffset = this.anchorOffset;
        if(this.anchor==ANCHOR.CENTER)
            this.realOffset -= this.width/2;
        else if(this.anchor==ANCHOR.RIGHT)
            this.realOffset -= this.width;

        // Update the offset in the actual hud
        super.setOffsetX(realOffset);
        
        // Make sure to mark as not changed anymore
        this.changed = false;

    }


    /**
     * Hides the hud for when it's not being used
     */
    public void hide(){

        // Hide the hud
        if(this.isVisible()){
            this.setVisible(false);
            try{this.clearFontImagesAndRefresh();}
            catch(Exception e){}
        }

        // Stop the auto hide task if any
        if(hudHideSchedule!=null){
            hudHideSchedule.cancel();
            hudHideSchedule = null;
        }

    }

    public void checkUI(){

        // Auto hide if stopped
        if(hudHideSchedule==null && hideTime>0)
                this.hide();

    }

    /**
     * Shows the hud for when it's being used
     */
    public void show(){
        // First check if the images changed and if so update the size and offset
        if(this.changed)
            update();

        // Then actually show the hud
        this.setVisible(true);

        // Then if a hide time was set cancel the previous if any and start a new task to auto hide the hud
        if(hideTime>0){
            if(hudHideSchedule!=null)
                hudHideSchedule.cancel();
            hudHideSchedule = Bukkit.getScheduler().runTaskLaterAsynchronously(WhatIsThis.instance, () -> {this.hide();}, this.hideTime);
        }
    }

    /**
     * Cleanup the HUD for removal
     */
    public void cleanup(){

        // Clear the images on the screen
        this.clearFontImagesAndRefresh();

        // Cancel the auto hide if any
        if(hudHideSchedule!=null){
            hudHideSchedule.cancel();
            hudHideSchedule = null;
        }

        // clear the img buffer
        imgBuffer.clear();
        this.changed = true;

    }

    public enum ANCHOR {
        /** The x position from the center of the screen to the left side of the HUD */
        LEFT, 
        
        /** The x offset is from the center of the screen to the center of the HUD */
        CENTER, 

        /** The x position from the center of the screen to the right side of the HUD */
        RIGHT;
    }

}
