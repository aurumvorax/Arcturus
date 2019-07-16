package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.savegame.SaveManager;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public abstract class SaveLoad extends MenuPage{

    protected boolean saveMode;

    private SaveManager manager = SaveManager.INSTANCE;
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
    public SaveLoad(MenuFramework frame){
        super(frame);

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
                transition(Core.GameMode.Active);
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
                confirmDelete.show(getStage());
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
                    if(saveMode)
                        save(false);
                    else{
                        if(manager.isValid(saveName.getText())){
                            manager.loadGame(saveName.getText());
                            transition(Core.GameMode.Active);
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

    @Override
    public Actor build(){
        outerTable.reset();
        innerTable.reset();
        buttonGroup.clear();
        savesList.clearItems();
        if(saveMode)
            buttonGroup.addActor(saveButton);
        else
            buttonGroup.addActor(loadButton);

        buttonGroup.addActor(deleteButton);
        buttonGroup.addActor(cancelButton);

        savesList.setItems(manager.getSaveList());

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
                confirmOverwrite.show(getStage());
            }
        }else{
            if(manager.saveGame(saveName.getText(), true))
                changeBack();
            else
                Gdx.app.error("SaveManager", "ERROR - Saving Game");
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
