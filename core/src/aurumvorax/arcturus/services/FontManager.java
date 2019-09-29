package aurumvorax.arcturus.services;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontManager{

    public FontManager(){
        System.out.println(Services.getSkin().getAll(BitmapFont.class).toString(" "));

    }
}
