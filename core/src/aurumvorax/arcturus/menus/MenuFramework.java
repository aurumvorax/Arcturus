package aurumvorax.arcturus.menus;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.artemis.systems.TransitionManager;
import aurumvorax.arcturus.menus.main_menu.*;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

            pages.put(Page.Start, new StartMenu(this));
            pages.put(Page.Game, new GameMenu(this));
            pages.put(Page.Save, new Save(this));
            pages.put(Page.Load, new Load(this));
            pages.put(Page.Options, new Options(this));
            pages.put(Page.Keybinds, new Keybinds(this));
        }
        setMovable(true);
        setResizable(true);
        setDebug(true);
        layout();
        setSize(400,400);
        this.align(Align.center);
    }

    public void setupFrame(Stage stage){


    }

    public void openToPage(Page page){
        reset();

        current = page;
        add(pages.get(page).show());
    }

    void changePage(Page page){
        reset();

        stateStack.push(current);
        current = page;

        add(pages.get(current).show());
    }

    void changeBack(){
        clear();

        if(stateStack.peek() == null)
            transition(Core.GameMode.Active);
        else{
            current = stateStack.pop();
            add(pages.get(current).show());
        }
    }

    void transition(Core.GameMode state){
        setColor(1,1,1,1);
        clear();
        stateStack.clear();
        TransitionManager.resumeGame(state);
    }


}
