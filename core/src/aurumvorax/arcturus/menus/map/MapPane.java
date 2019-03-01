package aurumvorax.arcturus.menus.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

public class MapPane extends ScrollPane{

    private float zoom = 1;
    private float zoomScale = 0.05f;
    private float zoomMin = 0.5f;
    private float zoomMax = 3f;
    private Vector2 screen = new Vector2();
    private Vector2 local = new Vector2();
    private Group group;

    public MapPane(Skin skin){
        super(new Stack(), skin);
        build();
    }

    public MapPane(){
        super(new Stack());
        build();
    }

    private void build(){
        group = ((Group)getActor());
        group.setTransform(true);

        setOverscroll(false, false);
        setFlingTime(0.2f);
        setFadeScrollBars(false);

        removeListener(getListeners().get(1));

        addListener(new InputListener(){
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                zoom = MathUtils.clamp(zoom + (amount * zoomScale), zoomMin, zoomMax);

                screen.set(x, Gdx.graphics.getHeight() - y);
                local.set(getActor().screenToLocalCoordinates(screen));
                System.out.println(screen + " " + local);
                group.setOrigin(local.x, local.y);

                group.setScale(zoom);

                System.out.println(zoom);
                return true;
            }
        });
    }

    public void addStackActor(Actor actor){ group.addActor(actor); }
    public void clearStack(){ group.clear(); }




    // center zooming on mouse
     // - calculate origin point from mouse position


    // setters

    //smooth zoom

    // zoom - mouse wheel and +/- buttons
        // - need to  add zoom buttons

    // center/zoom to target
        // - just a public method
}
