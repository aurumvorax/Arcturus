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


        pages.put(Page.Start, new StartMenu());
        pages.put(Page.Game, new GameMenu());
        pages.put(Page.Save, new Save());
        pages.put(Page.Load, new Load());
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
            enterGame(Core.GameMode.Active);
        else
            changePage(stateStack.pop());
    }

    public void enterGame(Core.GameMode state){
        Gdx.input.setInputProcessor(null);
        setColor(1,1,1,1);
        clear();
        stateStack.clear();
        TransitionManager.resumeGame(state);
    }


}
