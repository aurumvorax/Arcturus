package aurumvorax.arcturus.artemis.systems.render.hudElements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayDeque;
import java.util.Deque;

public class Messenger{

    private static float fadeAge;
    private static float fadeTime;
    private static Deque<Message> queue = new ArrayDeque<>();
    private static BitmapFont font;

    public static void add(String message){
        Message m = new Message();
        m.string = message;
        queue.push(m);
    }

    public static void draw(){

    }

    public static void update(Message m, float delta){
        m.age += delta;

        if(m.age >= fadeAge + fadeTime)
            queue.pop();
        else if(m.age >= fadeAge){
            m.fade = (fadeAge - m.age) / fadeTime;
        }
    }


    private static class Message{

        public String string;
        public float age = 0;
        public float fade = 0;

    }
}
