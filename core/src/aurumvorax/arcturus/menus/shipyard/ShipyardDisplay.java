package aurumvorax.arcturus.menus.shipyard;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.factories.ShipFactory;
import aurumvorax.arcturus.inventory.Slot;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class ShipyardDisplay extends Container<Stack>{

    private Stack displayStack = new Stack();
    private Image shipImage = new Image();
    private Table slotTable = new Table();
    private Array<Slot> weaponSlots = new Array<>();


    public ShipyardDisplay(){
        this.setActor(displayStack);
    }

    void build(ShipFactory.ShipData data){
        TextureRegion shipTexture = Services.getTexture(data.imgName);
        shipImage.setDrawable(new TextureRegionDrawable(shipTexture));
        shipImage.setScaling(Scaling.fill);
        displayStack.clear();
        displayStack.add(shipImage);
        displayStack.setBounds(200, 200, 600, 600);

    }
}
