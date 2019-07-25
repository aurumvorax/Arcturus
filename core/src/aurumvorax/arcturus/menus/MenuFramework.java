package aurumvorax.arcturus.menus;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.artemis.systems.TransitionManager;
import aurumvorax.arcturus.menus.main_menu.*;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;



public class MenuFramework extends Window{

    private Deque<Page> stateStack = new ArrayDeque<>();
    private Page current;
    private static EnumMap<Page, MenuPage> pages;

    public enum Page{
        Start, Game, Save, Load, Options, Keybinds,
        Shipyard,  // Dock, Missions, Commerce, InfoBroker, etc
        Dead,
        Map // Codex
    }


    public MenuFramework(){
        super("", Services.MENUSKIN);

        if(pages == null){
            pages = new EnumMap<>(Page.class);

            pages.put(Page.Start, new StartMenu());
            pages.put(Page.Game, new GameMenu());
            pages.put(Page.Save, new Save());
            pages.put(Page.Load, new Load());
            pages.put(Page.Options, new Options());
            pages.put(Page.Keybinds, new Keybinds());
        }
        setMovable(true);
        setResizable(true);
        setDebug(true);
        layout();
        setSize(400,400);
        this.align(Align.center);
        setVisible(false);
    }

    public void openToPage(Page page){
        reset();
        setVisible(true);
        current = page;
        add(pages.get(page).show(this));
    }

    void changePage(Page page){
        reset();

        stateStack.push(current);
        current = page;

        add(pages.get(current).show(this));
    }

    void changeBack(){
        reset();

        if(stateStack.peek() == null)
            closeMenu();
        else{
            current = stateStack.pop();
            add(pages.get(current).show(this));
        }
    }

    void enterGame(Core.GameMode state){
        setVisible(false);
        setColor(1,1,1,1);
        reset();
        stateStack.clear();
        TransitionManager.enterGame(state);
    }

    void closeMenu(){
        setVisible(false);
        setColor(1,1,1,1);
        reset();
        stateStack.clear();
        TransitionManager.unpause();

      // batch alpha ends up being 0 here, not sure how/why
    }


}
