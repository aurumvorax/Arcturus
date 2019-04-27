package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.services.Services;
import aurumvorax.arcturus.menus.MenuState;
import aurumvorax.arcturus.savegame.SaveManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class SaveLoad extends MenuState{

    private Mode mode;
    public enum Mode{ SAVE, LOAD }

    private SaveManager manager = SaveManager.getInstance();
    private TextButton saveButton = new TextButton("Save", Services.MENUSKIN);
    private TextButton loadButton = new TextButton("Load", Services.MENUSKIN);
    private TextButton deleteButton = new TextButton("Delete", Services.MENUSKIN);
    private TextButton cancelButton = new TextButton("Cancel", Services.MENUSKIN);
    private Table outerTable = new Table();
    private Table innerTable = new Table();
    private HorizontalGroup buttonGroup = new HorizontalGroup();
    private List<String> savesList = new List<>(Services.MENUSKIN);
    private ScrollPane savesPane = new ScrollPane(savesList);
    private TextArea saveInfo = new TextArea("This is where the save file metadata will be displayed", Services.MENUSKIN);
    private TextField saveName = new TextField("", Services.MENUSKIN);
    private Dialog confirmOverwrite;
    private Dialog confirmDelete;
    private Label confirmOverwriteLabel = new Label("", Services.MENUSKIN);
    private Label confirmDeleteLabel = new Label("", Services.MENUSKIN);


    @SuppressWarnings("unchecked")
    public SaveLoad(){

        cancelButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeBack();
            }
        });

        loadButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                manager.loadGame(savesList.getSelected());
                enterGame(Core.GameMode.Active);
            }
        });

        saveButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                save(false);
            }
        });

        deleteButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                confirmDeleteLabel.setText("Delete " + saveName.getText() + ".\nAre you sure?");
                confirmDelete.show(stage);
            }
        });

        savesList.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                saveName.setText(((List<String>)actor).getSelected());
                refresh();
            }
        });

        saveName.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int key){
                if(key == Input.Keys.ENTER){
                    if(mode == Mode.SAVE)
                        save(false);
                    else if(mode == Mode.LOAD){
                        if(manager.isValid(saveName.getText())){
                            manager.loadGame(saveName.getText());
                            enterGame(Core.GameMode.Active);
                        }
                    }
                }
                refresh();
                return true;
            }
        });

        confirmOverwrite = new Dialog("Overwrite Save", Services.MENUSKIN){
            @Override
            protected void result(Object object){
                if(object.equals(true))
                    save(true);
            }
        };
        confirmOverwrite.getContentTable().add(confirmOverwriteLabel);
        confirmOverwrite.button("Yes", true);
        confirmOverwrite.button("No", false);

        confirmDelete = new Dialog("Delete Save", Services.MENUSKIN){
            @Override
            protected void result(Object object){
                if(object.equals(true)){
                    manager.deleteGame(saveName.getText());
                    savesList.clearItems();
                    savesList.setItems(manager.getSaveList());
                }
            }
        };
        confirmDelete.getContentTable().add(confirmDeleteLabel);
        confirmDelete.button("Yes", true);
        confirmDelete.button("No", false);
    }

    public void setMode(Mode mode){ this.mode = mode; }

    @Override
    public Actor build(Stage menuStage){
        outerTable.reset();
        innerTable.reset();
        buttonGroup.clear();
        savesList.clearItems();
        if(mode == Mode.SAVE)
            buttonGroup.addActor(saveButton);
        else if(mode == Mode.LOAD)
            buttonGroup.addActor(loadButton);

        buttonGroup.addActor(deleteButton);
        buttonGroup.addActor(cancelButton);

        savesList.setItems(SaveManager.getInstance().getSaveList());

        Drawable menuBG = new NinePatchDrawable(Services.MENUSKIN.getPatch("list"));
        innerTable.setBackground(menuBG);

        innerTable.add(savesPane);
        innerTable.add(saveInfo).row();
        innerTable.add(saveName);
        innerTable.add(buttonGroup);

        outerTable.setFillParent(true);
        outerTable.add(innerTable);
        return outerTable;
    }

    private void save(boolean overwrite){
        if(!overwrite){
            if(manager.saveGame(saveName.getText(), false))
                changeBack();
            else{
                confirmOverwriteLabel.setText(saveName.getText() + " already exists.\n Overwrite?");
                confirmOverwrite.show(stage);
            }
        }else{
            if(manager.saveGame(saveName.getText(), true))
                changeBack();
            else
                Gdx.app.debug("SaveManager", "ERROR - Saving Game");
        }
    }

    private void refresh(){
        if(manager.isValid(saveName.getText())){
            deleteButton.setDisabled(false);
            loadButton.setDisabled(false);
        }else{
            deleteButton.setDisabled(true);
            loadButton.setDisabled(true);
        }
    }
}
