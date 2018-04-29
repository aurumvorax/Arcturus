package aurumvorax.arcturus.menus.shipyard;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.factories.ShipFactory;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.inventory.Inventory;
import aurumvorax.arcturus.inventory.InventoryActor;
import aurumvorax.arcturus.menus.MenuState;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class Shipyard extends MenuState{

    private InventoryActor inventoryActor;
    private ShipyardDisplay shipyardDisplay = new ShipyardDisplay();
    private Array<String> shipListData = new Array<>();
    private TextButton backButton = new TextButton("Back", Services.MENUSKIN);
    private SelectBox<String> shipList = new SelectBox<>(Services.MENUSKIN);
    private Table menuTable = new Table();


    public Shipyard(){

        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                enterGame(Core.GameMode.Active);
            }
        });
        shipList.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                PlayerShip.type = shipList.getSelected();
                refresh();
            }
        });

        DragAndDrop dragAndDrop = new DragAndDrop();
        inventoryActor = new InventoryActor(new Inventory(), dragAndDrop, Services.MENUSKIN);

    }

    @Override
    protected Actor build(Stage menuStage){

        shipListData.clear();
        for(String shipType : ShipFactory.getShipTypes())
            shipListData.add(shipType);
        shipList.setItems(shipListData);
        shipList.setSelected(PlayerShip.type);

        menuTable.reset();
        refresh();



        menuTable.setFillParent(true);
       menuTable.add(shipyardDisplay);
        menuTable.add(backButton);
        menuTable.add(shipList).row();
        stage.addActor(inventoryActor);
        return menuTable;
    }

    private void refresh(){
        shipyardDisplay.build(ShipFactory.getShipData(PlayerShip.type));
    }
}
