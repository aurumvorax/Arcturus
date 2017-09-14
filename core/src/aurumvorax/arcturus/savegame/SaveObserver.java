package aurumvorax.arcturus.savegame;

public interface SaveObserver {

    enum SaveEvent{
        SAVING, LOADING
    }

    void onNotify(SaveSubject saveManager, SaveEvent saveEvent);
}
