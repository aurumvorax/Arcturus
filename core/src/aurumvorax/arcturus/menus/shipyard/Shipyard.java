package aurumvorax.arcturus.menus.shipyard;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.PlayerData;
import aurumvorax.arcturus.artemis.factories.EntityData;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.inventory.Inventory;
import aurumvorax.arcturus.inventory.InventoryActor;
import aurumvorax.arcturus.inventory.ShipyardDisplay;
import aurumvorax.arcturus.menus.MenuState;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

public class Shipyard extends MenuState{

    private String shipType;

    private InventoryActor inventoryActor;
    private ShipyardDisplay shipyardDisplay;
    private DragAndDrop dragAndDrop = new DragAndDrop();
    private Array<String> shipListData = new Array<>();
    private TextButton backButton = new TextButton("Back", Services.MENUSKIN);
    private TextButton changeButton = new TextButton("Change", Services.MENUSKIN);
    private SelectBox<String> shipList = new SelectBox<>(Services.MENUSKIN);
    private Table menuTable = new Table();


    public Shipyard(){

        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                shipyardDisplay.saveToPlayerShip();
                enterGame(Core.GameMode.Active);
            }
        });
        changeButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                PlayerData.playership.type = shipType;
                refresh();
            }
        });
        shipList.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                shipType = shipList.getSelected();
            }
        });


        inventoryActor = new InventoryActor(new Inventory(), dragAndDrop, Services.MENUSKIN);
        shipyardDisplay = new ShipyardDisplay(dragAndDrop);

    }

    @Override
    protected Actor build(Stage menuStage){

        shipListData.clear();
        for(String shipType : EntityData.getShipTypes())
            shipListData.add(shipType);
        shipList.setItems(shipListData);
        shipList.setSelected(PlayerData.playership.type);

        menuTable.reset();
        menuTable.setDebug(true);
        menuTable.setFillParent(true);
        menuTable.add(shipyardDisplay).width(400).height(400).expand().fill().row();
        menuTable.add(backButton);
        menuTable.add(changeButton);
        menuTable.add(shipList).row();
        stage.addActor(inventoryActor);

        refresh();

        return menuTable;
    }

    private void refresh(){
        dragAndDrop.clear();
        inventoryActor.build();
        shipyardDisplay.build(EntityData.getShipData(PlayerData.playership.type), 400, 400);
    }
}
