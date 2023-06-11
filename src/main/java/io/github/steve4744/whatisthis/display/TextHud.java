package io.github.steve4744.whatisthis.display;

import java.util.HashMap;
import java.util.Map;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerHudsHolderWrapper;

/**
 * A Basic HUD that draws text in a straight line
 */
public class TextHud extends Hud{

    // All the possible characters to write (Note, font_image frontPrefix_blank will be used if one is not found)
    private static final char[] CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '_', ' ', '-', '=', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '=', '+', '[', ']', '{', '}', ';', '\'', '"', '|', '\\', ',', '.', '<', '>', '/', '?'};

    /**
     * The font images of the characters used in this HUD
     * If a font_image is not found will use the font_image with the name fontPrefix+blank
     */
    private Map<Character, FontImageWrapper> charImgs;

    /**
     * Creates a HUD for displaying text
     * 
     * @param holder The holder of the HUD
     * @param namespaceID The id in IA of the HUD object
     * @param fontPrefix The prefix of font images to use for the font
     * @param anchor The anchor of the offset of this HUD
     * @param offsetX The offset of this HUD
     * @param hideTime The time the HUD stays visible until it auto hides (set to -1 for never hide)
     */
    public TextHud(PlayerHudsHolderWrapper holder, String namespaceID, String fontPrefix, ANCHOR anchor, int offsetX, int hideTime) {

        // Create the hud with a 0 width and no images because width will change with the content
        super(holder, namespaceID, null, anchor, offsetX, hideTime);

        // Load the font images for the font of the HUD of the given fontPrefix
        charImgs = new HashMap<Character, FontImageWrapper>();
        FontImageWrapper noChar = new FontImageWrapper(fontPrefix+"blank");
        if(!noChar.exists())
            throw new FontImageNotFoundException(fontPrefix+"blank", namespaceID);
        for(char c : CHARS){
            FontImageWrapper img = new FontImageWrapper(fontPrefix+c);
            if(img.exists())
                charImgs.put(c, img);
            else
                charImgs.put(c, noChar);
        }

    }


    /**
     * Adds the given text to the hud. NOTE: THIS DOES NOT CLEAR THE HUD FIRST OR DRAWS THE UPDATE. call clearImgs or draw for those after or before this
     * 
     * @param text The text to add to this HUD
     */
    public void addText(String text){

        for(int i=0;i<text.length();i++)
            this.addImage(charImgs.get(text.charAt(i)));

    }

    /**
     * Adds space to the hud of the given amount
     * @param space The amount of space to add
     */
    public void addSpace(int space){

        for(int i=0;i<space;i++)
            this.addImage(charImgs.get(' '));

    }
    
}
