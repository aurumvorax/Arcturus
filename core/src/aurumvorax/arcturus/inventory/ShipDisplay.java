package aurumvorax.arcturus.inventory;

import aurumvorax.arcturus.PlayerData;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.shipComponents.Mount;
import aurumvorax.arcturus.artemis.factories.EntityData;
import aurumvorax.arcturus.artemis.factories.ShipData;
import aurumvorax.arcturus.artemis.factories.ShipProfile;
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

    private Image shipImage;
    private Group slotGroup = new Group();
    private Array<Slot> weaponSlots = new Array<>();
    private DragAndDrop dragAndDrop;
    private ShipProfile workingProfile;


    public ShipDisplay(DragAndDrop dragAndDrop, ShipProfile profile){
        this.dragAndDrop = dragAndDrop;
        workingProfile = profile;

        ShipData data = EntityData.getShipData(workingProfile.type);
        shipImage = new Image(Services.getTexture(data.imgName));
        shipImage.setScaling(Scaling.fit);
        shipImage.setAlign(Align.center);
    }

    public String getType(){ return (workingProfile == null) ? null : workingProfile.type; }
    public String getName(){ return workingProfile.name; }

    public void build(float width, float height){
        clear();
        slotGroup.clear();
        weaponSlots.clear();

        setSize(width, height);
        addActor(shipImage);
        validate();

        ShipData data = EntityData.getShipData(workingProfile.type);

        float scale = shipImage.getImageHeight() / shipImage.getDrawable().getMinHeight();
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
    }

    public void dumpTo(Inventory inventory){
        for(Slot ws : weaponSlots){
            Item.Stack stack = ws.getStack();
            if(ws.take(stack))
                inventory.add(stack);
        }
    }

    public void saveToPlayerShip(){

        workingProfile.loadout.weapons.clear();
        for(int i = 0; i < weaponSlots.size; i++){
            Item weapon = weaponSlots.get(i).getStack().item;
            if(weapon != null)
                workingProfile.loadout.weapons.put(i, weapon.name);
        }

        PlayerData.SetPlayerShip(workingProfile);

    }
}
