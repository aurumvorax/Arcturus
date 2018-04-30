package aurumvorax.arcturus.artemis.factories;

import com.badlogic.gdx.math.Vector2;

public class TerrainData{

    //Common to all projectile types
    String imgName;
    Vector2 imgCenter;

    // Orbital parameters

    float semimajor;
    float eccentricity;
    float offset;
    float tilt;
    double sweep;

    // Dockables

    String dockName;
}
