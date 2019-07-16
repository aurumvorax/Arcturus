package aurumvorax.arcturus.menus.codex;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Codex extends Table{

    private CodexPage currentPage;

    private Skin skin;
    private HorizontalGroup horizontalGroup;
    private ButtonGroup<Button> buttonGroup;


    public Codex(Skin skin){
        this.skin = skin;

        Button exitButton = new TextButton("X", skin);
        exitButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                close();
            }
        });
        add(exitButton);

        horizontalGroup = new HorizontalGroup();
        add(horizontalGroup).row();

        buttonGroup = new ButtonGroup<>();

        //addPage("Main Menu", new MenuPage());
    }

    public void open(CodexPage page){
        currentPage = page;
        currentPage.show();
        addActor(currentPage);
        //getTitleLabel().setText(currentPage.getTitle());
        buttonGroup.setChecked(currentPage.getName());
        setVisible(true);
    }

    void change(CodexPage page){
        if(currentPage != null)
            currentPage.hide();

        removeActor(currentPage);
        open(page);
    }

    void close(){
        this.setVisible(false);
        currentPage.hide();
    }

    void addPage(String name, CodexPage page){
        Button pageButton = new TextButton(name, skin);

        pageButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                change(page);
            }
        });

        horizontalGroup.addActor(pageButton);
        buttonGroup.add(pageButton);
    }

    public enum Pages{
        Menu, Inventory, Log
    }
}
