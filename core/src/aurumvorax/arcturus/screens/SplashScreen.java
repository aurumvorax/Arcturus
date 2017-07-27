package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.Resources;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class SplashScreen extends ScreenAdapter{

    Core core;

    public SplashScreen(Core core){
        this.core = core;
        Resources.queueTextureAssets();
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(Resources.loadAssets()){
            lastLoad();
        }
    }

    private void lastLoad(){
        core.switchScreen(Core.ScreenType.MainMenu);
    }


}
