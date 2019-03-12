package aurumvorax.arcturus.artemis.components;


import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.math.Vector2;

public class Trail extends Component{

    @EntityId public int parent;
    public boolean active = true;
    public String imgName;
    public Segment[] segments;
    public int size = 0;
    public int length;
    public int index = -1;
    public float texDiv;
    public float width;
    public float widen;

    public Vector2 point = new Vector2();
    public Vector2 lastPoint = new Vector2();
    public Vector2 offset = new Vector2();

    public static class Segment{
        public Vector2 left = new Vector2();
        public Vector2 right = new Vector2();
        public Vector2 offsetV = new Vector2();

        public void update(){
            right.add(offsetV);
            left.sub(offsetV);
        }
    }
}
