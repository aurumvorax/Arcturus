package aurumvorax.arcturus.inventory;

import aurumvorax.arcturus.PlayerData;
import aurumvorax.arcturus.artemis.components.Mount;
import aurumvorax.arcturus.artemis.factories.ShipData;
import aurumvorax.arcturus.services.EntityData;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class ShipDisplay extends Stack{

    private Image shipImage;
    private Group slotGroup = new Group();
    private Array<Slot> weaponSlots = new Array<>();
    private DragAndDrop dragAndDrop;
    private ShipData.Unique workingProfile;


    public ShipDisplay(DragAndDrop dragAndDrop){
        this.dragAndDrop = dragAndDrop;
    }

    public String getType(){ return (workingProfile == null) ? null : workingProfile.type; }
    public String getName(){ return workingProfile.name; }

    public void build(ShipData.Unique ship, float width, float height){
        workingProfile = ship;
        rebuild(width, height);
    }

    public void rebuild(float width, float height){
        clear();
        slotGroup.clear();
        weaponSlots.clear();

        ShipData.Stock data = EntityData.getShipData(workingProfile.type);
        shipImage = new Image(Services.getTexture(data.imgName));
        shipImage.setScaling(Scaling.fit);
        shipImage.setAlign(Align.center);
        setSize(width, height);
        addActor(shipImage);
        validate();

        float scale = shipImage.getImageHeight() / shipImage.getDrawable().getMinHeight();
        Vector2 origin = new Vector2(data.imgCenter);
        origin.scl(scale).add(shipImage.getImageX(), shipImage.getImageY());

        for(int i = 0; i < data.weaponMounts.size; i++){
            Mount.Weapon mount = data.weaponMounts.get(i);
            Slot weaponSlot = new Slot(null, Item.ItemType.Weapon, Services.MENUSKIN, 1);
            weaponSlot.setPosition((mount.location.x  * scale + origin.x), (mount.location.y * scale + origin.y), Align.center);
            weaponSlot.setRotation(mount.angle);
            weaponSlots.add(weaponSlot);
            dragAndDrop.addSource(new DragSource(weaponSlot));
            dragAndDrop.addTarget(new DragTarget(weaponSlot));
            slotGroup.addActor(weaponSlot);
        }

        addActor(slotGroup);
        validate();
        populateSlots();
    }

    private void populateSlots(){
        for(int i = 0; i < workingProfile.loadout.weapons.size; i++){
            String weapon = workingProfile.loadout.weapons.get(i);
            if(weapon != null)
                weaponSlots.get(i).add(new Item.Stack(new Item(Item.ItemType.Weapon, weapon) , 1));
        }
    }

    public void dumpTo(Inventory inventory){
        for(Slot ws : weaponSlots){
            if(ws != null){
                Item.Stack stack = ws.getStack();
                if(ws.take(stack))
                    inventory.add(stack);
            }
        }
    }

    public void saveToPlayerShip(){

        workingProfile.loadout.weapons.clear();
        for(int i = 0; i < weaponSlots.size; i++){

            if(weaponSlots.get(i).getStack() != null){
                Item weapon = weaponSlots.get(i).getStack().item;
                workingProfile.loadout.weapons.put(i, weapon.name);
            }
        }

        PlayerData.SetPlayerShip(workingProfile);

    }
}
