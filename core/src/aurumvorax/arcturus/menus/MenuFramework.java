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

    public static final MenuFramework INSTANCE = new MenuFramework();

    private Deque<Page> stateStack = new ArrayDeque<>();
    private Page current;
    private EnumMap<Page, MenuPage> pages = new EnumMap<>(Page.class);

    public enum Page{
        Start, Game, Save, Load, Options, Keybinds,
        Shipyard,  // Dock, Missions, Commerce, InfoBroker, etc
        Dead,
        Map
    }


    private MenuFramework(){
        super("", Services.MENUSKIN);

        pages.put(Page.Start, new StartMenu(this));
        pages.put(Page.Game, new GameMenu(this));
        pages.put(Page.Save, new Save(this));
        pages.put(Page.Load, new Load(this));
        pages.put(Page.Options, new Options(this));
        pages.put(Page.Keybinds, new Keybinds(this));

        setPosition(100,100);
        setSize(200,400);
        setDebug(true);
    }

    public void openToPage(Page page){
        setColor(1,1,1,1);
        clear();

        current = page;
        add(pages.get(page).show());

        stateStack.push(Page.Dead);
        stateStack.push(Page.Keybinds);
        stateStack.push(Page.Map);
        System.out.println(stateStack.peek().toString());
        System.out.println(stateStack.peek().toString());

    }

    void changePage(Page page){
        setColor(1,1,1,1);
        reset();

        stateStack.push(current);
        current = page;

        add(pages.get(current).show());
    }

    void changeBack(){
        setColor(1,1,1,1);
        clear();

        if(stateStack.peek() == null)
            transition(Core.GameMode.Active);
        else{
            current = stateStack.pop();
            add(pages.get(current).show());
        }
    }

    void transition(Core.GameMode state){
        Gdx.input.setInputProcessor(null);
        setColor(1,1,1,1);
        clear();
        stateStack.clear();
        TransitionManager.resumeGame(state);
    }


}
