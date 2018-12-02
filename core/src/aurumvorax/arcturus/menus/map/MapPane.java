package aurumvorax.arcturus.menus.map;

import aurumvorax.arcturus.services.EntityData;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class MapPane extends ScrollPane{

    private Stack mapStack = new Stack();
    private Group markerGroup = new Group();
    private Image background;
    private Array<MapMarker> mapItems = new Array<>();

    MapPane(Skin skin){
        super(null, skin);
        setActor(mapStack);
        setOverscroll(false, false);
        setFlingTime(0.2f);
        setFadeScrollBars(false);

        background = new Image(Services.getTexture("MapBackgroundSmaller"));
        for(String system : EntityData.getSolarSystems()){
            MapMarker marker = new MapMarker(system, EntityData.getStellarData(system).systemCoords, Services.MENUSKIN);

            marker.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    GalaxyMap.setNavTarget(marker.name);
                }
            });

            mapItems.add(marker);
        }
    }

    public void build(){
        mapStack.clear();
        markerGroup.clear();
        background.layout();

        for(MapMarker m : mapItems){
            markerGroup.addActor(m);
        }

        mapStack.addActor(background);
        mapStack.addActor(markerGroup);
    }

    private static class MapMarker extends ImageButton{
        private String name;

        private MapMarker(String name, Vector2 coords, Skin skin){
            super(skin);
            this.name = name;
            setPosition(coords.x, coords.y);
        }
    }
}
