package aurumvorax.arcturus.menus.shipyard;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.factories.ShipFactory;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.menus.MenuState;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class Shipyard extends MenuState{

    private Array<String> shipListData = new Array<>();
    private TextButton backButton = new TextButton("Back", Services.MENUSKIN);
    private TextButton loadout1 = new TextButton("Loadout 1", Services.MENUSKIN);
    private TextButton loadout2 = new TextButton("Loadout 2", Services.MENUSKIN);
    private SelectBox<String> shipList = new SelectBox<String>(Services.MENUSKIN);
    private Table menuTable = new Table();


    public Shipyard(){

        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                enterGame(Core.GameMode.Active);
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
        shipList.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                PlayerShip.type = shipList.getSelected();
            }
        });
    }

    @Override
    protected Actor build(Stage menuStage){

        shipListData.clear();
        for(String shipType : ShipFactory.getShipTypes())
            shipListData.add(shipType);
        shipList.setItems(shipListData);
        shipList.setSelected(PlayerShip.type);

        menuTable.reset();

        menuTable.setFillParent(true);
        menuTable.add(backButton);
        menuTable.add(loadout1);
        menuTable.add(loadout2);
        menuTable.add(shipList);
        return menuTable;
    }
}
