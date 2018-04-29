package aurumvorax.arcturus.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SlotActor extends ImageButton implements SlotListener{

    private Slot slot;
    private Skin skin;

    public SlotActor(Skin skin, Slot slot) {
        super(createStyle(skin, slot));
        this.slot = slot;
        this.skin = skin;
        slot.addListener(this);
    }

    private static ImageButtonStyle createStyle(Skin skin, Slot slot) {
        TextureRegion image = slot.getTexture();
        ImageButtonStyle style = new ImageButtonStyle(skin.get(ButtonStyle.class));
        style.imageUp = new TextureRegionDrawable(image);
        style.imageDown = new TextureRegionDrawable(image);
        return style;
    }

    public Slot getSlot() {
        return slot;
    }

    @Override
    public void hasChanged(Slot slot) {
        setStyle(createStyle(skin, slot));
    }

}

