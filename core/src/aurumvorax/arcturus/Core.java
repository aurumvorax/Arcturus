package aurumvorax.arcturus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Core extends ApplicationAdapter {

    private static GameMode mode;
    public enum GameMode{ NEW, LOAD, ACTIVE }

    public void setMode(GameMode mode){ Core.mode = mode; }
    public GameMode getMode(){ return mode; }
	
	@Override
	public void create () {

	}

	@Override
	public void dispose () {

	}
}
