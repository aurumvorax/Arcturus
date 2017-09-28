package aurumvorax.arcturus.savegame;

public interface SaveObserver {

    enum SaveEvent{
        SAVING, LOADING
    }

    void onNotify(SaveManager saveManager, SaveEvent saveEvent);
}
