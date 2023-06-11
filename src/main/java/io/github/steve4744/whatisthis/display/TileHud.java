package io.github.steve4744.whatisthis.display;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerHudsHolderWrapper;

/**
 * A Basic HUD that draws imgs tiled as the HUD grows and shrinks in size
 */
public class TileHud extends Hud{

    // The font images to use for the tiles that start the pattern
    private FontImageWrapper[] startTileImgs;
    // The font images to use for the tiles that repeat until reached width
    private FontImageWrapper[] repeatTileImgs;
    // The font images to use for the tiles that end the pattern
    private FontImageWrapper[] endTileImgs;

    // The total width of the starting and ending tiles since it shouldn't change
    private int startWidth;
    private int endWidth;

    /**
     * Creates a HUD for displaying text
     * 
     * @param holder The holder of the HUD
     * @param namespaceID The id in IA of the HUD object
     * @param startTiles The font images to use for the tiles that start the pattern
     * @param repeatTiles The font images to use for the tiles that repeat in order given
     * @param endTiles The font images to use for the tiles that end the pattern
     * @param anchor The anchor of the offset of this HUD
     * @param offsetX The offset of this HUD
     * @param width The starting width of the HUD
     * @param hideTime The time the HUD stays visible until it auto hides (set to -1 for never hide)
     */
    public TileHud(PlayerHudsHolderWrapper holder, String namespaceID, String[] startTiles, String[] repeatTiles, String[] endTiles, ANCHOR anchor, int offsetX, int width, int hideTime) {

        // Create the hud with a 0 width and no images because width will change with the content
        super(holder, namespaceID, null, anchor, offsetX, hideTime);

        // Load the font images for the tiles of the HUD
        this.startTileImgs = this.getFontImages(startTiles, namespaceID);
        this.repeatTileImgs = this.getFontImages(repeatTiles, namespaceID);
        this.endTileImgs = this.getFontImages(endTiles, namespaceID);

        // Get and calculate the width of the starting and ending tiles
        this.startWidth = 0;
        for(FontImageWrapper img : startTileImgs)
            this.startWidth += img.getWidth();
        this.endWidth = 0;
        for(FontImageWrapper img : endTileImgs)
            this.endWidth += img.getWidth();

        // Set the width from the given value
        this.setWidth(width);

    }

    /**
     * Gets the font images for the given array of font image names
     * 
     * @param imgs The font image names
     * @param namespaceID The namespace id of the IA HUD
     */
    private FontImageWrapper[] getFontImages(String[] imgs, String namespaceID){
        FontImageWrapper[] fontImgs = new FontImageWrapper[imgs.length];
        for(int i=0;i<fontImgs.length;i++){
            fontImgs[i] = new FontImageWrapper(imgs[i]);
            if(!fontImgs[i].exists()){
                throw new FontImageNotFoundException(imgs[i], namespaceID);
            }
        }
        return fontImgs;
    }


    /**
     * Sets the width of the HUD updating the images. NOTE: Does NOT actually draw the new images. Call draw after this to do that
     * 
     * @param width The new width of this HUD
     */
    public void setWidth(int width){

        // First clear the images
        this.clearImgs();

        // Then add the start tiles and set them as the width
        int curWidth = this.startWidth;
        for(FontImageWrapper img : startTileImgs)
            this.addImage(img);

        // Now add the repeating tiles until you have reached just before the end
        int i = 0;
        while(curWidth+this.endWidth<width){
            curWidth += repeatTileImgs[i].getWidth();
            this.addImage(repeatTileImgs[i]);
            if(++i>=repeatTileImgs.length)
                i = 0;
        }

        // Then add the end tiles
        for(FontImageWrapper img : endTileImgs)
            this.addImage(img);

    }

}
