package aurumvorax.arcturus.menus.shipyard;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.PlayerData;
import aurumvorax.arcturus.artemis.factories.ShipData;
import aurumvorax.arcturus.inventory.Inventory;
import aurumvorax.arcturus.inventory.ShipDisplay;
import aurumvorax.arcturus.menus.MenuState;
import aurumvorax.arcturus.services.EntityData;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

public class Shipyard extends MenuState{

    private ShipDisplay shipDisplay = null;
    private Inventory inventory;
    private Array<String> shipListData = new Array<>();
    private TextButton confirmButton = new TextButton("Confirm", Services.MENUSKIN);
    private TextButton cancelButton = new TextButton("Cancel", Services.MENUSKIN);
    private SelectBox<String> shipList = new SelectBox<>(Services.MENUSKIN);
    private Table menuTable = new Table();
    private Group buttonGroup = new HorizontalGroup();

    private int displayWidth, displayHeight;


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

                    ShipData.Unique playerShip = PlayerData.GetPlayerShip();
                    ShipData.Unique newShip = new ShipData.Unique(playerShip.name, shipList.getSelected());
                    newShip.x = playerShip.x;
                    newShip.y = playerShip.y;
                    newShip.t = playerShip.t;

                    shipDisplay.build(newShip, displayWidth, displayHeight);
                }
            }
        });

        DragAndDrop dragAndDrop = new DragAndDrop();
        shipDisplay = new ShipDisplay(dragAndDrop);
        inventory = new Inventory(dragAndDrop, Services.MENUSKIN);
        buttonGroup.addActor(confirmButton);
        buttonGroup.addActor(cancelButton);
    }

    @Override
    protected Actor build(Stage menuStage){

        displayWidth = Gdx.graphics.getWidth();
        displayHeight = Gdx.graphics.getHeight();

        shipDisplay.build(PlayerData.GetPlayerShip(), displayWidth, displayHeight);

        shipListData.clear();
        for(String shipType : EntityData.getShipTypes())
            shipListData.add(shipType);
        shipList.setItems(shipListData);
        shipList.setSelected(PlayerData.GetPlayerShip().type);

        refresh();

        return menuTable;
    }

    private void refresh(){
        inventory.update();
        shipDisplay.rebuild(displayWidth, displayHeight);

        menuTable.reset();
        menuTable.setDebug(true);
        menuTable.setFillParent(true);
        menuTable.add(shipDisplay).size(displayWidth, displayHeight).fill();
        menuTable.add(inventory).row();
        menuTable.add(buttonGroup).space(20).left();
        menuTable.add(shipList).right().row();
    }

    @Override
    public void resize(int width, int height){
        displayWidth = width - 400;
        displayHeight = height - 200;
        refresh();
    }
}
