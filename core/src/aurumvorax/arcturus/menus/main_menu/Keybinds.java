package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.options.Keys;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntMap;

public class Keybinds extends MenuPage{


    private ScrollPane scrollPane;
    private Table paneContent;
    private Table buttonGroup;
    private Table menuTable;

    private EntryRow currentlyMapping;


    public Keybinds(){
        buttonGroup = new Table();
        paneContent = new Table();
        scrollPane = new ScrollPane(paneContent, Services.MENUSKIN);
        menuTable = new Table();

        addCaptureListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode){
                if(currentlyMapping == null)
                    return false;

                currentlyMapping.set(keycode);
                return true;
            }
        });

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
        buttonGroup.add(acceptButton).row();
        buttonGroup.add(cancelButton);

        setTouchable(Touchable.enabled);
    }

    @Override
    protected void build(){
        reset();
        menuTable.reset();

        refresh();
        menuTable.add(scrollPane).colspan(120);
        menuTable.add(buttonGroup);
        add(menuTable);
    }

    private void refresh(){
        paneContent.reset();

        IntMap<Keys.Command> keybinds = Services.keys.getAllCommands();

        for(Keys.Command c : Keys.Command.values()){
           paneContent.add(new EntryRow(c)).row();
        }
    }

    private class EntryRow extends Button{

        private Keys.Command command;
        private int keycode;


        public EntryRow(Keys.Command command){
            super(Services.MENUSKIN);

            this.command = command;

            add(new Label(command.name(), Services.MENUSKIN)).colspan(80).align(Align.left);
            keycode = Services.keys.getKey(command);
            add(new Label((keycode == -1) ? "-" : Input.Keys.toString(keycode), Services.MENUSKIN));

            addListener(new ChangeListener(){
                @Override
                public void changed(ChangeEvent event, Actor actor){
                    requestMapKey();
                }
            });
        }

        private void set(int keycode){
            Services.keys.setCommand(currentlyMapping.keycode, null);
            Services.keys.setCommand(keycode, command);
            currentlyMapping = null;
            refresh();
        }

        private void requestMapKey(){
            currentlyMapping = this;
        }

    }
}
