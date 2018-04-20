package aurumvorax.arcturus;

import aurumvorax.arcturus.screens.GameScreen;
import aurumvorax.arcturus.screens.MenuScreen;
import aurumvorax.arcturus.screens.SplashScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.EnumMap;

public class Core extends Game{

    private static PlayerData playerData = new PlayerData();

    private GameMode gameMode = GameMode.New;
    public enum GameMode{
        New,
        Active,
        Dead
    }

    private EnumMap<ScreenType, Screen> screens;
    public enum ScreenType{
        Loading,
        Menu,
        Game
    }


    public GameMode getGameMode(){ return gameMode; }
    public void setGameMode(GameMode state){ gameMode = state; }

    public void setMenuMode(MenuScreen.MenuType mode){
        ((MenuScreen)screens.get(ScreenType.Menu)).setCurrent(mode);
    }

    public void switchScreen(ScreenType screen){
        setScreen(screens.get(screen));
    }

	@Override
	public void create(){           // Switch to the loading screen for asset loading
        screens = new EnumMap<>(ScreenType.class);
        screens.put(ScreenType.Loading, new SplashScreen(this));
        setScreen(screens.get(ScreenType.Loading));
	}

	public void initialize(){           // Called by LoadingScreen AFTER asset loading
	    screens.put(ScreenType.Menu, new MenuScreen(this));
	    screens.put(ScreenType.Game, new GameScreen(this));
    }

	@Override
	public void dispose(){
        for(Screen s : screens.values())
            s.dispose();
        screens.clear();
	}
}
