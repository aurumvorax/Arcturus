package aurumvorax.arcturus;

import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.screens.DockScreen;
import aurumvorax.arcturus.screens.GameScreen;
import aurumvorax.arcturus.screens.MainMenuScreen;
import aurumvorax.arcturus.screens.SplashScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.EnumMap;

public class Core extends Game{

    private static boolean gameActive = false;
    private static PlayerData playerData = new PlayerData();



    private EnumMap<ScreenType, Screen> screens;
    public enum ScreenType{
        Loading,
        MainMenu,
        Game,
        Dock
    }

    public static PlayerShip getPlayerShip(){ return playerData.playership; }
    public static void setActive(boolean active){Core.gameActive = active; }
    public static boolean getActive(){ return gameActive; }

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
	    screens.put(ScreenType.Dock, new DockScreen(this));
    }

	@Override
	public void dispose(){
        for(Screen s : screens.values())
            s.dispose();
        screens.clear();
	}
}
