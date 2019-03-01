package aurumvorax.arcturus.artemis.components;

import aurumvorax.arcturus.artemis.systems.render.Renderer;

import static aurumvorax.arcturus.artemis.systems.render.Renderer.Layer.EFFECT;

public class AnimatedSprite extends SimpleSprite{

    public float time;
    public Renderer.Layer layer = EFFECT;
}
