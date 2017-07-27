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

public class Core extends Game{

    private static GameMode mode;
    public enum GameMode{ NEW, LOAD, ACTIVE }

    public void setMode(GameMode mode){ Core.mode = mode; }
    public GameMode getMode(){ return mode; }

    private Screen splashScreen;
    private Screen mainMenuScreen;
    private Screen gameScreen;

    public enum ScreenType{
        Splash,
        MainMenu,
        Game
    }

    public void switchScreen(ScreenType screen){
        switch(screen){
            case Splash:
                setScreen(splashScreen);
                break;
            case MainMenu:
                setScreen(mainMenuScreen);
                break;
            case Game:
                setScreen(gameScreen);
                break;
        }
    }
	
	@Override
	public void create(){
        splashScreen = new SplashScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);
        setScreen(splashScreen);
	}

	@Override
	public void dispose(){
        splashScreen.dispose();
        mainMenuScreen.dispose();
        gameScreen.dispose();
	}
}
