package aurumvorax.arcturus.menus.shipyard;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.artemis.systems.TransitionManager;
import aurumvorax.arcturus.menus.MenuState;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Shipyard extends MenuState{

    private static Shipyard INSTANCE = new Shipyard();
    public static Shipyard getInstance(){ return INSTANCE; }

    private TextButton backButton = new TextButton("Back", Services.MENUSKIN);
    private TextButton loadout1 = new TextButton("Loadout 1", Services.MENUSKIN);
    private TextButton loadout2 = new TextButton("Loadout 2", Services.MENUSKIN);
    private TextButton loadout3 = new TextButton("Loadout 3", Services.MENUSKIN);
    private Table menuTable = new Table();


    private Shipyard(){
        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                TransitionManager.resumeGame();
            }
        });
        loadout1.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                PlayerShip.weapons.put(0, "MultiBarrel");

            }
        });
        loadout2.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                PlayerShip.weapons.put(0, "TestBeam");
            }
        });
        loadout3.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                PlayerShip.type = "OtherShip";
            }
        });
    }

    @Override
    protected Actor build(Stage menuStage){
        menuTable.reset();

        menuTable.setFillParent(true);
        menuTable.add(backButton);
        menuTable.add(loadout1);
        menuTable.add(loadout2);
        menuTable.add(loadout3);
        return menuTable;
    }
}
