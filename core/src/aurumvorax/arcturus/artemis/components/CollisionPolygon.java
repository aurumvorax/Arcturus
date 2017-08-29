package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


// This component MUST be initialized with setVertices before use
public class CollisionPolygon extends Component{

    private Array<Array<Vector2>> vertices;
    private Array<Array<Vector2>> orientedVertices = new Array<>();

    public void setVertices(Array<Array<Vector2>> vertices){
        this.vertices = vertices;
        for(int i = 0; i < vertices.size; i++){
            orientedVertices.add(new Array<>());
            for(int j = 0; j < vertices.get(i).size; j++)
                orientedVertices.get(i).add(new Vector2(vertices.get(i).get(j)));
        }
    }

    // Returns objects vertices in world coordinates
    public Array<Array<Vector2>> getVertices(Physics2D p){
        for(int i = 0; i < vertices.size; i ++)
            for(int j = 0; j < vertices.get(i).size; j++)
                orientedVertices.get(i).get(j).set(vertices.get(i).get(j)).rotate(p.theta).add(p.p);
        return orientedVertices;
    }

}
