package aurumvorax.arcturus.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

public class DragTarget extends DragAndDrop.Target{

    private Draggable.Target target;

    DragTarget(Actor target){
        super(target);
        this.target = (Draggable.Target)target;
    }

    @Override
    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer){
        return (target.isValid((Item.Stack)payload.getObject()));
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer){}
}
