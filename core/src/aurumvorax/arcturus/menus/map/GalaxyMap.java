package aurumvorax.arcturus.menus.map;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.galaxy.SolarSystemManager;
import aurumvorax.arcturus.galaxy.StellarData;
import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.services.EntityData;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class GalaxyMap extends MenuPage{

    private TextButton cancelButton = new TextButton("Cancel", Services.MENUSKIN);
    private static TextButton setCourseButton = new TextButton(null, Services.MENUSKIN);
    private HorizontalGroup buttonGroup = new HorizontalGroup();
    private ZoomPane map;
    private Group markerGroup = new Group();
    private Image background;
    private Stack stack = new Stack();
    private Array<MapMarker> mapItems = new Array<>();
    private static String tempNavTarget;


    @SuppressWarnings("unchecked")
    public GalaxyMap(MenuFramework frame){
        super(frame);

        map = new ZoomPane(stack, Services.MENUSKIN);

        background = new Image(Services.getTexture("MapBackgroundSmaller"));

        for(String system : EntityData.getSolarSystems()){
            StellarData.Base data = EntityData.getStellarData(system);

            if(data == null)
                break;

            MapMarker marker = new MapMarker(system, data.systemCoords, Services.MENUSKIN);

            marker.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    setNavTarget(marker.name);
                }
            });

            mapItems.add(marker);
        }

        stack.addActor(background);
        stack.addActor(markerGroup);
        map.setWidget(stack);

        cancelButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                transition(Core.GameMode.Active);
            }
        });

        setCourseButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                if(tempNavTarget != null){
                    SolarSystemManager.setNavigationTarget(tempNavTarget);
                    transition(Core.GameMode.Active);
                }
            }
        });

        buttonGroup.addActor(setCourseButton);
        buttonGroup.addActor(cancelButton);
    }

     void setNavTarget(String target){
        tempNavTarget = target;
        setCourseButton.getLabel().setText("Set course to " + target);
    }


    @Override
    protected void build(){
        reset();
        markerGroup.clear();
        for(MapMarker m : mapItems)
            markerGroup.addActor(m);

        clear();
        setFillParent(true);
        setDebug(true);
        add(map).expand().fill().row();
        add(buttonGroup);
        getStage().setScrollFocus(map);

        Gdx.app.log("", "image" + background.getPrefHeight());

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
