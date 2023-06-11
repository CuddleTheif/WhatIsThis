package io.github.steve4744.whatisthis.display;

/**
 * Thrown whenever a HUD can't find a font image to load from IA
 */
public class FontImageNotFoundException extends NullPointerException
{

    public FontImageNotFoundException(String fontImg, String hud)
    {
        super("Error: Couldn't find the font image "+fontImg+" for the HUD "+hud);
    }
}