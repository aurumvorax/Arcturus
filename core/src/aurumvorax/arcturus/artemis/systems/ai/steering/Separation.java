package aurumvorax.arcturus.artemis.systems.ai.steering;

public class Separation{

    /*
    private static Separation INSTANCE = new Separation();
    private Separation(){} // Single static class with DI/callback

    private static Vector2 separation = new Vector2();
    private static Vector2 ownerPosition = new Vector2();
    private static Vector2 contactRelative = new Vector2();
    private static float decay;

    private static ComponentMapper<Physics2D> mPhysics;


    public static void initialize(World world){ world.inject(INSTANCE); }

    public static Vector2 calc(int owner, float decay){
        Separation.decay = decay;
        separation.setZero();
        ownerPosition.set(mPhysics.get(owner).p);
        Proximity.findContacts(owner, 1000, INSTANCE);
        return separation;
    }

    @Override
    public boolean reportContact(int contact){

        contactRelative.set(ownerPosition).sub(mPhysics.get(contact).p);
        float dist2 = contactRelative.len2();

        if(dist2 == 0)
            return true;

        float force = decay / dist2;
        separation.mulAdd(contactRelative.nor(), force);
        return true;
    }
    */
}
