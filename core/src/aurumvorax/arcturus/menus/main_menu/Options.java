package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Options extends MenuPage{

    private TextButton keysButton = new TextButton("Keybinds", Services.getSkin());
    private TextButton acceptButton = new TextButton("Accept", Services.getSkin());
    private TextButton cancelButton = new TextButton("Cancel", Services.getSkin());
    private Slider scaleSlider= new Slider(0.5f, 2.0f, 0.5f, false, Services.getSkin());
    private Table menuTable = new Table();

    public Options(){

        scaleSlider.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Services.prefs.setUiScale(((Slider)actor).getValue());
            }
        });
        acceptButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Services.prefs.savePrefereces();
                changeBack();
            }
        });

        keysButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuFramework.Page.Keybinds);
            }
        });

        cancelButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeBack();
            }
        });
    }

    @Override
    protected void build(){
        reset();
        menuTable.reset();

        scaleSlider.setValue(Services.prefs.getUiScale());
        menuTable.setFillParent(true);
        menuTable.add(scaleSlider).row();
        menuTable.add(keysButton).row();
        menuTable.add(acceptButton);
        menuTable.add(cancelButton);

        add(menuTable);
    }
}
