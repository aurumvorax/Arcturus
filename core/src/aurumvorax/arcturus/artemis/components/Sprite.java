package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;

public class Sprite extends Component{
    public enum Layer{
        DEFAULT,
        BACKGROUND,
        ACTORS,
        MOUNTED,
        EFFECTS;

        public int getLayerID(){ return ordinal(); }
    }

    public String name;
    public Layer layer = Layer.DEFAULT;
}
