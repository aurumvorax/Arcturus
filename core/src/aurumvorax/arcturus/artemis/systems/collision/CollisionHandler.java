package aurumvorax.arcturus.artemis.systems.collision;

public interface CollisionHandler{


    void onCollide(int entity1, int entity2, Collision.Manifold m);
}
