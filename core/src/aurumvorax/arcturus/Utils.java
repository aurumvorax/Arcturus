package aurumvorax.arcturus;

public class Utils{

    private Utils(){}

    public static final float TWOPI = 6.283185307f;

    public static float normalize(float angle){
        while(angle < -180) angle += 360;
        while(angle > 180) angle -= 360;
        return angle;
    }

    public static float sign(float number){
        return (number == 0) ? 0f : (number < 0) ? -1f : 1f;
    }
}
