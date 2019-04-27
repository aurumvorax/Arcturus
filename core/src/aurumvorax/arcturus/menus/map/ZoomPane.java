package aurumvorax.arcturus.menus.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class ZoomPane extends Stack{

    private Button zoomInButton;
    private Button zoomOutButton;
    private Group buttonGroup;
    private ScrollPane scrollPane;
    private Actor widget;

    private float zoom = 1f;
    private float zoomMin = 0.5f;
    private float zoomMax = 2f;
    private float zoomScrollRate = 0.05f;
    private float zoomButtonRate = 0.1f;

    private Vector2 screen = new Vector2();
    private Vector2 local = new Vector2();


    public ZoomPane(Actor actor){
        this(actor, new ScrollPane.ScrollPaneStyle(), new TextButton.TextButtonStyle());
    }

    public ZoomPane(Actor actor, Skin skin){
        this(actor, skin.get(ScrollPane.ScrollPaneStyle.class), skin.get(TextButton.TextButtonStyle.class));
    }

    public ZoomPane(Actor actor, Skin skin, String styleName){
        this(actor, skin.get(styleName, ScrollPane.ScrollPaneStyle.class), skin.get(styleName, TextButton.TextButtonStyle.class));
    }

    public ZoomPane(Actor actor, ScrollPane.ScrollPaneStyle paneStyle, TextButton.TextButtonStyle buttonStyle){
        if(paneStyle == null)
            throw new IllegalArgumentException("ScrollPane Style cannot be null");
        if(buttonStyle == null)
            throw new IllegalArgumentException("TextButton Style cannot be null");

        zoomInButton = new TextButton("+", buttonStyle);
        zoomInButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                widget.setOrigin(Align.center);
                setZoom(zoom + zoomButtonRate);
            }
        });

        zoomOutButton = new TextButton("-", buttonStyle);
        zoomOutButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                widget.setOrigin(Align.center);
                setZoom(zoom - zoomButtonRate);
            }
        });

        buttonGroup = new Group();
        buttonGroup.setTouchable(Touchable.childrenOnly);
        buttonGroup.addActor(zoomInButton);
        buttonGroup.addActor(zoomOutButton);

        scrollPane = new ScrollPane(null, paneStyle);
        scrollPane.setFadeScrollBars(false);
        scrollPane.removeListener(scrollPane.getListeners().get(1));
        scrollPane.addListener(new InputListener(){
            public boolean scrolled(InputEvent event, float x, float y, int amount){
                screen.set(x, Gdx.graphics.getHeight() - y);
                local.set(widget.screenToLocalCoordinates(screen));
                widget.setOrigin(local.x, local.y);
                setZoom(zoom + (amount * zoomScrollRate));

                return true;
            }
        });

        setWidget(actor);
    }

    public void setWidget(Actor actor){
        widget = actor;
        clearChildren();

        if(actor != null){
            addActor(scrollPane);
            addActor(buttonGroup);

            scrollPane.setActor(widget);

            if(widget instanceof Group)
                ((Group)widget).setTransform(true);
        }
    }

    public void setZoom(float z){
        zoom = MathUtils.clamp(z, zoomMin, zoomMax);
        widget.setScale(zoom);
        validate();
    }

    public float getZoom(){ return zoom; }
    public float getZoomMin(){ return zoomMin; }
    public float getZoomMax(){ return zoomMax; }
    public ScrollPane getScrollPane(){ return scrollPane; }

    public void setZoomMin(float zMin){ zoomMin = zMin; }
    public void setZoomMax(float zMax){ zoomMax = zMax; }

    @Override
    public void layout(){
        super.layout();

        float x1 = scrollPane.getWidth() - scrollPane.getScrollBarWidth() - (zoomInButton.getWidth() * 3);
        float x2 = scrollPane.getWidth() - scrollPane.getScrollBarWidth() - (zoomInButton.getWidth() * 1.5f);
        float y = scrollPane.getHeight() - (zoomInButton.getHeight() * 1.5f);

        zoomInButton.setPosition(x1, y);
        zoomOutButton.setPosition(x2, y);
    }
}
