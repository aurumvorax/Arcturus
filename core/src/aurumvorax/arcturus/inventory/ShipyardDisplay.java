package aurumvorax.arcturus.inventory;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.shipComponents.Mount;
import aurumvorax.arcturus.artemis.components.shipComponents.Player;
import aurumvorax.arcturus.artemis.factories.ShipData;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class ShipyardDisplay extends Stack{

    private Image shipImage = new Image();
    private Group slotGroup = new Group();
    private Array<SlotActor> weaponSlots = new Array<>();
    private DragAndDrop dragAndDrop;


    public ShipyardDisplay(DragAndDrop dragAndDrop){
        this.dragAndDrop = dragAndDrop;
    }

    public void build(ShipData data, float width, float height){
        clear();
        shipImage.clear();
        slotGroup.clear();
        weaponSlots.clear();

        TextureRegion shipTexture = Services.getTexture(data.imgName);

        shipImage.setDrawable(new TextureRegionDrawable(shipTexture));
        shipImage.setScaling(Scaling.fit);
        shipImage.setAlign(Align.center);
        setSize(width, height);
        addActor(shipImage);
        validate();

        float scale = shipImage.getImageHeight() / shipTexture.getRegionHeight();
        System.out.println(shipImage.getScaleX() + " " + scale);
        Vector2 origin = new Vector2(data.imgCenter);
        origin.scl(scale).add(shipImage.getImageX(), shipImage.getImageY());


        for(int i = 0; i < data.weaponMounts.size; i++){
          Mount.Weapon mount = data.weaponMounts.get(i);
           SlotActor weaponSlot = new SlotActor(Services.MENUSKIN, new Slot(null, 0));
            weaponSlot.setPosition((mount.location.x  * scale + origin.x), (mount.location.y * scale + origin.y), Align.center);
            weaponSlot.setRotation(mount.angle);
            weaponSlots.add(weaponSlot);
            dragAndDrop.addSource(new SlotSource(weaponSlot));
            dragAndDrop.addTarget(new SlotTarget(weaponSlot));
            slotGroup.addActor(weaponSlot);
        }
        addActor(slotGroup);
        validate();
        loadFromPlayerShip();

    }

    public void loadFromPlayerShip(){
        for(int i = 0; i < PlayerShip.weapons.size; i++){
            String weapon = PlayerShip.weapons.get(i);
            if(weapon == null)
                weaponSlots.get(i).getSlot().clear();
            else
                weaponSlots.get(i).getSlot().add(new Item(Item.ItemType.Weapon, weapon), 1);
        }
    }

    public void saveToPlayerShip(){
        for(int i = 0; i < weaponSlots.size; i++){
            String weapon = weaponSlots.get(i).getSlot().getItem().name;
            PlayerShip.weapons.put(i, weapon);
        }

    }
}
