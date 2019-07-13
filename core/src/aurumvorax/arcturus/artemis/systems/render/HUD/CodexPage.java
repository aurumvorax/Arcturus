package aurumvorax.arcturus.artemis.systems.render.HUD;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class CodexPage extends Table{


    abstract void show();

    abstract void hide();

    abstract String getTitle();
}
