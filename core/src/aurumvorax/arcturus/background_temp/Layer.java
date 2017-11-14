package aurumvorax.arcturus.background_temp;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Layer {

	private int SIZE = 2048;
	
	TextureRegion[] image = new TextureRegion[4];
	float ratio;
	float px, py, ps;
	float x1, y1, x2, y2;
	float x1m, x2m, y1m, y2m;
	
	public Layer(Texture i, float r){
		image[0] = new TextureRegion(i, 0, 0, 0.5f, 0.5f);
		image[1] = new TextureRegion(i, 0.5f, 0, 1, 0.5f);
		image[2] = new TextureRegion(i, 0, 0.5f, 0.5f, 1);
		image[3] = new TextureRegion(i, 0.5f, 0.5f, 1, 1);
		ratio = r;
		ps = SIZE / ratio;
	}
	
	public void draw(Batch b, float x, float y){
		px = x * (1 - ratio);
		py = y * (1 - ratio);
		if(ratio == 0){ 
			x1 = -SIZE / 2 + x;
			y1 = -SIZE / 2 + y;
			x2 = SIZE / 2 + x;
			y2 = SIZE / 2 + y;
		}else{
			x1 = (float) Math.floor((x - ps) / (2 * ps)) * 2 * SIZE + (SIZE * 3 / 2) + px;
			y1 = (float) Math.floor((y - ps) / (2 * ps)) * 2 * SIZE + (SIZE * 3 / 2) + py;
			x2 = (float) Math.floor(x / (2 * ps)) * 2 * SIZE + (SIZE / 2) + px;
			y2 = (float) Math.floor(y / (2 * ps)) * 2 * SIZE + (SIZE / 2) + py;
		}		
		b.draw(image[0],  x1,  y1, SIZE, SIZE);
		b.draw(image[1],  x2,  y1, SIZE, SIZE);
		b.draw(image[2],  x1,  y2, SIZE, SIZE);
		b.draw(image[3],  x2,  y2, SIZE, SIZE);		
	}
}
