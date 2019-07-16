package aurumvorax.arcturus.menus;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.artemis.systems.TransitionManager;
import aurumvorax.arcturus.menus.main_menu.*;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;



public class MenuFramework extends Window{

    private static final MenuFramework INSTANCE = new MenuFramework();

    private Deque<Page> stateStack = new ArrayDeque<>();
    private Page current;
    private EnumMap<Page, MenuPage> pages = new EnumMap<>(Page.class);

    public enum Page{
        Start, Game, Save, Load, Options, Keybinds,
        Shipyard,  // Dock, Missions, Commerce, InfoBroker, etc
        Dead,
        Codex, Map, Log
    }


    private MenuFramework(){
        super("", Services.MENUSKIN);


        pages.put(Page.Start, new StartMenu(this));
        pages.put(Page.Game, new GameMenu(this));
        pages.put(Page.Save, new Save(this));
        pages.put(Page.Load, new Load(this));
        pages.put(Page.Options, new Options());
        pages.put(Page.Keybinds, new Keybinds());

    }

    public void changePage(Page newPage){
        setColor(1,1,1,1);
        clear();

        stateStack.push(current);
        current = newPage;

        addActor(pages.get(newPage).show());
    }

    public void changeBack(){
        setColor(1,1,1,1);
        clear();

        if(stateStack.peek() == null)
            transition(Core.GameMode.Active);
        else
            changePage(stateStack.pop());
    }

    public void transition(Core.GameMode state){
        Gdx.input.setInputProcessor(null);
        setColor(1,1,1,1);
        clear();
        stateStack.clear();
        TransitionManager.resumeGame(state);
    }


}
