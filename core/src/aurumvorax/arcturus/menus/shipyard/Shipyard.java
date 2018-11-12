package aurumvorax.arcturus.menus.shipyard;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.PlayerData;
import aurumvorax.arcturus.artemis.factories.EntityData;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.factories.ShipData;
import aurumvorax.arcturus.artemis.factories.ShipProfile;
import aurumvorax.arcturus.inventory.Inventory;
import aurumvorax.arcturus.inventory.ShipDisplay;
import aurumvorax.arcturus.menus.MenuState;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

public class Shipyard extends MenuState{

    private String shipType;

    private ShipDisplay shipDisplay;
    private Inventory inventory;
    private DragAndDrop dragAndDrop = new DragAndDrop();
    private Array<String> shipListData = new Array<>();
    private TextButton confirmButton = new TextButton("Confirm", Services.MENUSKIN);
    private TextButton cancelButton = new TextButton("Cancel", Services.MENUSKIN);
    private SelectBox<String> shipList = new SelectBox<>(Services.MENUSKIN);
    private Table menuTable = new Table();


    public Shipyard(){

        confirmButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                shipDisplay.saveToPlayerShip();
                enterGame(Core.GameMode.Active);
            }
        });
        cancelButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                enterGame(Core.GameMode.Active);
            }
        });
        shipList.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                if((shipDisplay != null) && !(shipDisplay.getType().equals(shipList.getSelected()))){
                    shipDisplay.dumpTo(inventory);
                    ShipProfile p = new ShipProfile(shipDisplay.getName(), shipList.getSelected(), "");
                    shipDisplay = new ShipDisplay(dragAndDrop, p);
                }
            }
        });
    }

    @Override
    protected Actor build(Stage menuStage){

        shipListData.clear();
        for(String shipType : EntityData.getShipTypes())
            shipListData.add(shipType);
        shipList.setDisabled(true);
        shipList.setItems(shipListData);
        shipList.setSelected(PlayerData.GetPlayerShip().type);
        shipList.setDisabled(false);
        shipDisplay = new ShipDisplay(dragAndDrop, PlayerData.GetPlayerShip());
        inventory = new Inventory(dragAndDrop, Services.MENUSKIN);

        refresh();
        menuTable.reset();
        menuTable.setDebug(true);
        menuTable.setFillParent(true);
        menuTable.add(shipDisplay).width(400).height(400).expand().fill();
        menuTable.add(inventory).row();
        menuTable.add(confirmButton);
        menuTable.add(cancelButton);
        menuTable.add(shipList).row();
        Stage s = menuTable.getStage();
        return menuTable;
    }

    private void refresh(){

        inventory.update();
        shipDisplay.build(400, 400);
    }
}
