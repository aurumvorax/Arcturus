package aurumvorax.arcturus.artemis.components;

import aurumvorax.arcturus.artemis.systems.Renderer;
import com.artemis.Component;

public class SimpleSprite extends Component{

    public String name;
    public float offsetX;
    public float offsetY;
    public Renderer.Layer layer = Renderer.Layer.DEFAULT;
}
