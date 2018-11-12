package aurumvorax.arcturus.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

public class DragSource extends DragAndDrop.Source{

    private Draggable.Source source;

    DragSource(Actor actor){
        super(actor);
        source = (Draggable.Source)actor;
    }

    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer){
        DragAndDrop.Payload payload = null;
        Item.Stack sourceStack = source.getStack();

        if(source.take(sourceStack)){
            payload = new DragAndDrop.Payload();
            payload.setObject(sourceStack);
            TextureRegion icon = ((Item.Stack) payload.getObject()).item.getTexture();
            payload.setDragActor(new Image(icon));
        }
        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target){
        Item.Stack payloadStack = (Item.Stack)payload.getObject();

        if(!(target instanceof Draggable.Target)){
            source.add(payloadStack);
            return;
        }

        Draggable.Target t = (Draggable.Target)target.getActor();
        payloadStack.quantity = t.add(payloadStack);
        source.add(payloadStack);
    }
}
