package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.options.Keys;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.IntMap;

public class Keybinds extends MenuPage{


    private ScrollPane scrollPane;
    private Table paneContent;
    private Table buttonGroup;
    private Table menuTable;

    public Keybinds(MenuFramework frame){
        super(frame);

        buttonGroup = new Table();
        paneContent = new Table();
        scrollPane = new ScrollPane(paneContent, Services.MENUSKIN);
        menuTable = new Table();

        TextButton defaultsButton = new TextButton("Reset to Default", Services.MENUSKIN);
        defaultsButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Services.keys.setDefaults();
                refresh();
            }
        });

        TextButton acceptButton = new TextButton("Accept", Services.MENUSKIN);
        acceptButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Services.keys.saveKeybinds();
                changeBack();
            }
        });

        TextButton cancelButton = new TextButton("Cancel", Services.MENUSKIN);
        cancelButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Services.keys.loadKeybinds();
                changeBack();
            }
        });

        buttonGroup.add(defaultsButton).row();
        buttonGroup.add(acceptButton);
        buttonGroup.add(cancelButton);
    }

    @Override
    protected void build(){
        reset();
        menuTable.reset();

        refresh();
        menuTable.add(scrollPane);
        menuTable.add(buttonGroup);
        add(menuTable);

    }

    private void refresh(){
        paneContent.clear();

        IntMap<Keys.Command> keybinds = Services.keys.getAllCommands();

        for(Keys.Command c : Keys.Command.values()){
            paneContent.add(new Label(c.name(), Services.MENUSKIN)).padRight(20);
            paneContent.add(new TextButton(keybinds.findKey(c, true, 0) + " blah", Services.MENUSKIN)).padBottom(10).row();
        }

    }
}
