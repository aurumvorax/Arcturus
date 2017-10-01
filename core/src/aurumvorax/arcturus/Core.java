package aurumvorax.arcturus;

import aurumvorax.arcturus.screens.GameScreen;
import aurumvorax.arcturus.screens.MainMenuScreen;
import aurumvorax.arcturus.screens.SplashScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.EnumMap;

public class Core extends Game{

    private static boolean gameActive = false;

    private EnumMap<ScreenType, Screen> screens;
    public enum ScreenType{
        Loading,
        MainMenu,
        Game
    }

    public void setActive(boolean active){Core.gameActive = active; }
    public boolean getActive(){ return gameActive; }

    public void switchScreen(ScreenType screen){ setScreen(screens.get(screen)); }
	
	@Override
	public void create(){           // Switch to the loading screen for asset loading
        screens = new EnumMap<>(ScreenType.class);
        screens.put(ScreenType.Loading, new SplashScreen(this));
        setScreen(screens.get(ScreenType.Loading));
	}

	public void initialize(){           // Called by LoadingScreen AFTER asset loading
	    screens.put(ScreenType.MainMenu, new MainMenuScreen(this));
	    screens.put(ScreenType.Game, new GameScreen(this));
    }

	@Override
	public void dispose(){
        for(Screen s : screens.values())
            s.dispose();
        screens.clear();
	}
}
