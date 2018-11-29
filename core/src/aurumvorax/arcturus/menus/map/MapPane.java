package aurumvorax.arcturus.menus.map;

import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;

public class MapPane extends ScrollPane{

    private Stack mapStack = new Stack();
    private Group markerGroup = new Group();
    private Image background;
    private Array<MapMarker> mapItems = new Array<>();

    MapPane(Skin skin){
        super(null, skin);
        setActor(mapStack);

        background = new Image(Services.getTexture("MapBackgroundSmaller"));
        mapItems.add(new MapMarker("Omicron Persei", new Vector2(0,0), Services.getTexture("MapIcon")));
        mapItems.add(new MapMarker("Pirates Nest", new Vector2(100,200), Services.getTexture("MapIcon")));
    }

    public void build(){
        mapStack.clear();
        markerGroup.clear();
        background.layout();

        for(MapMarker m : mapItems)
            markerGroup.addActor(m);

        mapStack.addActor(background);
        mapStack.addActor(markerGroup);
    }



    private static class MapMarker extends Image{
        private String name;
        private Vector2 coords;

        private MapMarker(String name, Vector2 coords, TextureRegion region){
            super(region);
            this.name = name;
            this.coords = coords;
        }

    }

}
