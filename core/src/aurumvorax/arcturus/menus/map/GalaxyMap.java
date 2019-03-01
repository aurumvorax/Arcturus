package aurumvorax.arcturus.menus.map;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.galaxy.SolarSystemManager;
import aurumvorax.arcturus.menus.MenuState;
import aurumvorax.arcturus.services.EntityData;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class GalaxyMap extends MenuState{

    private Table mapTable = new Table();
    private TextButton cancelButton = new TextButton("Cancel", Services.MENUSKIN);
    private static TextButton setCourseButton = new TextButton(null, Services.MENUSKIN);
    private HorizontalGroup buttonGroup = new HorizontalGroup();
    private MapPane map;
    private Group markerGroup = new Group();
    private Image background;
    private Array<MapMarker> mapItems = new Array<>();
    private static String tempNavTarget;

    public GalaxyMap(){
        map = new MapPane(Services.MENUSKIN);

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
        map.addStackActor(background);
        map.addStackActor(markerGroup);

        cancelButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                enterGame(Core.GameMode.Active);
            }
        });

        setCourseButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                if(tempNavTarget != null){
                    SolarSystemManager.setNavigationTarget(tempNavTarget);
                    enterGame(Core.GameMode.Active);
                }
            }
        });

        buttonGroup.addActor(setCourseButton);
        buttonGroup.addActor(cancelButton);
    }

    static void setNavTarget(String target){
        tempNavTarget = target;
        setCourseButton.getLabel().setText("Set course to " + target);
    }


    @Override
    protected Actor build(Stage menuStage){
        markerGroup.clear();
        background.layout();
        for(MapMarker m : mapItems)
            markerGroup.addActor(m);

        mapTable.clear();
        mapTable.setFillParent(true);
        mapTable.setDebug(true);
        mapTable.add(map).row();
        mapTable.add(buttonGroup);
        menuStage.setScrollFocus(map);

        return mapTable;
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
