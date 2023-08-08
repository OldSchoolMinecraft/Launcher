package me.moderator_man.osml.swing;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FontManager
{
    private final String fontBaseName;

    public FontManager(String fontName)
    {
        fontBaseName = fontName;
    }

    public Font getNormalFont()
    {
        return getFont(fontBaseName + "-Regular.ttf");
    }

    public Font getBoldFont()
    {
        return getFont(fontBaseName + "-Bold.ttf");
    }

    public Font getItalicFont()
    {
        return getFont(fontBaseName + "-Italic.ttf");
    }

    private Font getFont(String fontFileName)
    {
        try (InputStream is = FontManager.class.getResourceAsStream("/fonts/" + fontFileName))
        {
            return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(is));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        return new Font("Sans-Serif", Font.PLAIN, 14);
    }
}
