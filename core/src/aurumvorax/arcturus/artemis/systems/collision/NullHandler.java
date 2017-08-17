package aurumvorax.arcturus.artemis.systems.collision;

public enum NullHandler implements CollisionHandler{
    INSTANCE;

    @Override
    public void onCollide(int entity1, int entity2, Collision.Manifold m){
        System.out.println("Collision between object " + entity1 + " and object " + entity2);
    }
}
