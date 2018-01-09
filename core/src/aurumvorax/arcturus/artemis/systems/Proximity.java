package aurumvorax.arcturus.artemis.systems;


import aurumvorax.arcturus.artemis.components.CollisionRadius;
import aurumvorax.arcturus.artemis.components.Physics2D;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
public class Proximity extends BaseEntitySystem{

    private static IntBag agents = new IntBag();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<CollisionRadius> mRadius;

    public Proximity(){
        super(Aspect.all(CollisionRadius.class, Physics2D.class));
    }

    @Override protected void processSystem(){}
    @Override protected void inserted(int entityID){ agents.add(entityID); }
    @Override protected void removed(int entityID){ agents.removeValue(entityID); }

    public static int findContacts(int subject, float range, Callback callback){
        int neighbours = 0;
        if(range <= 0){
            for(int i = 0; i < agents.size(); i++){
                int agent = agents.get(i);
                if((agent != subject) && (callback.reportContact(agent)))
                    neighbours++;
            }
            return neighbours;
        }else{
            for(int i = 0; i < agents.size(); i++){
                int agent = agents.get(i);
                if(agent != subject){
                    float distance2 = mPhysics.get(subject).p.dst2(mPhysics.get(agent).p);
                    if((distance2 <= range * range) && (callback.reportContact(agent)))
                        neighbours++;
                }
            }
            return neighbours;
        }
    }

    public interface Callback{
        boolean reportContact(int contact);
    }
}
