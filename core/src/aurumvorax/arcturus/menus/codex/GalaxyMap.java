package aurumvorax.arcturus.menus.codex;

import aurumvorax.arcturus.galaxy.StellarData;
import aurumvorax.arcturus.menus.ZoomPane;
import aurumvorax.arcturus.services.EntityData;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

public class GalaxyMap extends CodexPage{

    private ZoomPane map;
    private Group markerGroup = new Group();
    private Image background;
    private Stack stack = new Stack();


    GalaxyMap(){
        map = new ZoomPane(stack, Services.MENUSKIN);
        background = new Image(Services.getTexture("MapBackgroundSmaller"));
    }

    @Override
    public void show(){
        reset();
        setDebug(true, true);
        map.setWidget(stack);
        refresh();
        add(map).expand().fill();
    }

    public void refresh(){
        stack.clear();

        background = new Image(Services.getTexture("MapBackgroundSmaller"));

        for(String system : EntityData.getSolarSystems()){
            StellarData.Base data = EntityData.getStellarData(system);

            if(data == null)
                break;

            MapMarker marker = new MapMarker(system, data.systemCoords, Services.MENUSKIN);
            markerGroup.addActor(marker);
        }

        stack.addActor(background);
        stack.addActor(markerGroup);
    }

    private static class MapMarker extends ImageButton{
        private String name;

        private MapMarker(String name, Vector2 coords, Skin skin){
            super(skin);
            this.name = name;
            setPosition(coords.x, coords.y);
            //add listeners for click and mouseover
        }
    }
}
