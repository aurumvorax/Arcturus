package aurumvorax.arcturus.artemis.systems.collision;

public interface CollisionHandler{


    void onCollide(int entityA, int entityB, Collision.Manifold m);
}
