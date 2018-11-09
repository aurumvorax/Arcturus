package aurumvorax.arcturus.inventory;

import aurumvorax.arcturus.PlayerData;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.shipComponents.Mount;
import aurumvorax.arcturus.artemis.factories.Ships;
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

public class ShipDisplay extends Stack{

    private Image shipImage = new Image();
    private Group slotGroup = new Group();
    private Array<Slot> weaponSlots = new Array<>();
    private DragAndDrop dragAndDrop;


    public ShipDisplay(DragAndDrop dragAndDrop){
        this.dragAndDrop = dragAndDrop;
    }

    public void build(Ships.Data data, float width, float height){
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
            Slot weaponSlot = new Slot(null, Item.ItemType.Weapon, Services.MENUSKIN, 0);
            weaponSlot.setPosition((mount.location.x  * scale + origin.x), (mount.location.y * scale + origin.y), Align.center);
            weaponSlot.setRotation(mount.angle);
            weaponSlots.add(weaponSlot);
            dragAndDrop.addSource(new DragSource(weaponSlot));
            dragAndDrop.addTarget(new DragTarget(weaponSlot));
            slotGroup.addActor(weaponSlot);
        }
        addActor(slotGroup);
        validate();
        loadFromPlayerShip();

    }

    public void loadFromPlayerShip(){
        Ships.Profile p = PlayerData.playership;
        for(int i = 0; i < p.loadout.weapons.size; i++){
            String weapon = p.loadout.weapons.get(i);
            weaponSlots.get(i).empty();
            if(!(weapon == null))
                weaponSlots.get(i).add(new Item.Stack(new Item(Item.ItemType.Weapon, weapon), 1));
        }
    }

    public void saveToPlayerShip(){
        Ships.Profile p = PlayerData.playership;
        p.loadout.weapons.clear();
        for(int i = 0; i < weaponSlots.size; i++){
            Item weapon = weaponSlots.get(i).getStack().item;
            if(weapon != null)
                p.loadout.weapons.put(i, weapon.name);
        }

    }
}
