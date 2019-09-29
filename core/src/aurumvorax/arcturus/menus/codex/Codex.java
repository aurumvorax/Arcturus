package aurumvorax.arcturus.menus.codex;

import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.EnumMap;

public class Codex extends MenuPage{

    private EnumMap<Page, CodexPage> codexPages= new EnumMap<>(Page.class);

    public enum Page{
        GalaxyMap // TODO SystemMap, Mission Log, Cargo Manifest, Intel
    }

    private CodexPage current;
    private ButtonGroup<Button> buttonGroup;
    private VerticalGroup tabGroup = new VerticalGroup();


    public Codex(){
        codexPages.put(Page.GalaxyMap, new GalaxyMap());
        //codexPages.put(Page.SystemMap, new SystemMap());
        //codexPages.put(Page.Missions, new Missions());
        //codexPages.put(Page.Manifest, new Manifest());
        //codexPages.put(Page.Intel, new Intel());



        ImageButton menuButton = new ImageButton(Services.getSkin());
        menuButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){

            }
        });
        tabGroup.addActor(menuButton);

        ImageButton galaxyButton = new ImageButton(Services.getSkin());
        galaxyButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changePage(Page.GalaxyMap);
            }
        });
        tabGroup.addActor(galaxyButton);

        ImageButton systemButton = new ImageButton(Services.getSkin());
        systemButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){

            }
        });
        tabGroup.addActor(systemButton);

        ImageButton missionButton = new ImageButton(Services.getSkin());
        missionButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){

            }
        });
        tabGroup.addActor(missionButton);

        ImageButton manifestButton = new ImageButton(Services.getSkin());
        manifestButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){

            }
        });
        tabGroup.addActor(manifestButton);

        ImageButton intelButton = new ImageButton(Services.getSkin());
        intelButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){

            }
        });
        tabGroup.addActor(intelButton);

        ImageButton closeButton = new ImageButton(Services.getSkin());
        closeButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                closeMenu();
            }
        });
        tabGroup.addActor(closeButton);

        buttonGroup = new ButtonGroup<>(galaxyButton, systemButton, missionButton, manifestButton, intelButton);
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setUncheckLast(true);
    }


    @Override
    protected void build(){
        reset();
        add(tabGroup).left();
        current.show();
        add(current);
    }

    public void changePage(Page page){
        if(current != null)
            current.addAction(Actions.fadeOut(FADETIME));

        current = codexPages.get(page);
        current.setColor(1,1,1,0);
        current.show();
        current.addAction(Actions.fadeIn(FADETIME));
        buttonGroup.getButtons().get(page.ordinal()).setChecked(true);
    }
}
