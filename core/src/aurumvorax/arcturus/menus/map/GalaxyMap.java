package aurumvorax.arcturus.menus.map;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.PlayerData;
import aurumvorax.arcturus.menus.MenuState;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GalaxyMap extends MenuState{

    private Table mapTable = new Table();
    private TextButton cancelButton = new TextButton("Cancel", Services.MENUSKIN);
    private static TextButton setCourseButton = new TextButton(null, Services.MENUSKIN);
    private HorizontalGroup buttonGroup = new HorizontalGroup();
    private MapPane mapPane = new MapPane(Services.MENUSKIN);
    private static String tempNavTarget;

    public GalaxyMap(){

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
                    PlayerData.navTarget = tempNavTarget;
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
        mapPane.build();

        mapTable.clear();
        mapTable.setFillParent(true);
        mapTable.setDebug(true);
        mapTable.add(mapPane).row();
        mapTable.add(buttonGroup);

        return mapTable;
    }
}
