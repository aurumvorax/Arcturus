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

    private static GameMode mode;
    public enum GameMode{ NEW, LOAD, ACTIVE }

    private EnumMap<ScreenType, Screen> screens;
    public enum ScreenType{
        Splash,
        MainMenu,
        Game
    }

    public void setMode(GameMode mode){ Core.mode = mode; }
    public GameMode getMode(){ return mode; }

    public void switchScreen(ScreenType screen){ setScreen(screens.get(screen)); }
	
	@Override
	public void create(){           // Only initialize the loading screen, then let it load assets
        screens = new EnumMap<>(ScreenType.class);
        screens.put(ScreenType.Splash, new SplashScreen(this));
        setScreen(screens.get(ScreenType.Splash));
	}

	public void initialize(){           // Initialize everything else AFTER asset loading
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
