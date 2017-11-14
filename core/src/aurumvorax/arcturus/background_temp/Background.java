package aurumvorax.arcturus.background_temp;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Background {

	Texture[] image = new Texture[4];
	Layer[] layer = new Layer[4];
	
	public Background(){
		image[0] = new Texture(Gdx.files.internal("img/background-2048.png"));
		image[1] = new Texture(Gdx.files.internal("img/starlayer-2048.png"));
		image[2] = new Texture(Gdx.files.internal("img/starlayer2-2048.png"));
		image[3] = new Texture(Gdx.files.internal("img/Nebula.png"));
		layer[0] = new Layer(image[0], 0.1f);
		layer[1] = new Layer(image[1], 0.2f);
		layer[2] = new Layer(image[2], 0.3f);
		layer[3] = new Layer(image[3], 0.8f);
	}
	
	public void draw(Batch b, float x, float y){
		for(Layer l : layer){ 
			l.draw(b, x, y); 
		}
	}
	
	public void dispose(){
		for(Texture i : image){
			i.dispose();
		}
	}
}
