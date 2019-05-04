package aurumvorax.arcturus.artemis.systems.ai.utilityAI;

import com.badlogic.gdx.math.MathUtils;

public class Transform{

    Type type;
    float m, k, b, c;

    private static final float LOGIT_SAFETY_RANGE = 0.001f;

    public Transform(Type type, float m, float k, float b, float c){
        this.type = type;

        switch(type){

            // Boolean cut off mode.  If the input is below M, then B will be returned, otherwise, C will be returned.
            // M, B and C must be between 0 and 1.  K is not used.
            case CUTOFF:
                this.m = MathUtils.clamp(m, 0f, 1f);
                this.b = MathUtils.clamp(b, 0f, 1f);
                this.c = MathUtils.clamp(c, 0f, 1f);
                break;

            // Linear transform.  M is the slope, B is the vertical shift, C is the horizontal shift.
            // M must be finite and nonzero. K is not used.
            case LINEAR:
                this.m = MathUtils.clamp(m, -1000f, 1000f);
                this.b = MathUtils.clamp(b, -10f, 10f);
                this.c = MathUtils.clamp(c, -10f, 10f);
                break;

            // Polynomial transform.  M is the slope, K is the sharpness of the curve. B is the vertical shift, and C
            // is the horizontal shift.  M must be finite and nonzero. K must be positive and nonzero.
            case POLYNOMIAL:
                this.m = MathUtils.clamp(m, -1000f, 1000f);
                this.k = MathUtils.clamp(k, 0.001f, 1000f);
                this.b = MathUtils.clamp(b, -10f, 10f);
                this.c = MathUtils.clamp(c, -10f, 10f);
                break;

            // Horizontally oriented sigmoid curve.  M affects the slope at the inflection point, K is the scaling factor.
            // B is the vertical shift and C is the horizontal shift. K must be positive and nonzero.
            case LOGISTIC:
                this.m = MathUtils.clamp(m, -1000f, 1000f);
                this.k = MathUtils.clamp(k, 0.001f, 1000f);
                this.b = MathUtils.clamp(b, -10f, 10f);
                this.c = MathUtils.clamp(c, -10f, 10f);
                break;

            // Vertically oriented sigmoid curve.  M affects the slope at the inflection point, K is the sharpness and
            // direction of the curve.  M must be positive and nonzero.
            case LOGIT:
                this.m = MathUtils.clamp(m, 0.001f, 100f);
                this.k = MathUtils.clamp(k, -1000, 1000f);
                this.b = MathUtils.clamp(b, -10f, 10f);
                this.c = MathUtils.clamp(c, -10f, 10f);
                break;
        }
    }

    public enum Type{

        CUTOFF{
            @Override
            public float calc(float input, float m, float k, float b, float c){
                return (input <= m) ? b : c;
        }},

        LINEAR{
            @Override
            public float calc(float input, float m, float k, float b, float c){
                return (m * (input - c) + b);
        }},

        POLYNOMIAL{
            @Override
            public float calc(float input, float m, float k, float b, float c){
                if(input <= c) return 0;

                return (m * (float)Math.pow((double)(input - c), (double)k) + b);
        }},

        LOGISTIC{
            @Override
            public float calc(float input, float m, float k, float b, float c){
                return (k * (1 / (1 + (float)Math.pow(1000 * m * Math.E, -input + c + 0.5))) + b);
        }},

        LOGIT{
            @Override
            public float calc(float input, float m, float k, float b, float c){
                if(input <= b + LOGIT_SAFETY_RANGE) return 0;
                if(input >= b + 1 - LOGIT_SAFETY_RANGE) return 1;

                return (k * (float)(Math.log((input - c)/(1 - input - c)))/(float)(Math.log(1000 * Math.E * m)) + b);
        }};

        public abstract float calc(float input, float m, float k, float b, float c);
    }

    public float calcTransform(float input, Transform transform){
        input = MathUtils.clamp(input, 0, 1);
        input = transform.type.calc(input, transform.m, transform.k, transform.b, transform.c);
        return MathUtils.clamp(input, 0, 1);
    }
}
