package aurumvorax.arcturus.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public enum PreferenceManager{
    INSTANCE;

    private static Preferences prefs = Gdx.app.getPreferences("options.cfg");
    private static String lastSave = null;


    public static String getLastSave(){ return PreferenceManager.lastSave; }
    public static void setLastString(String lastSave){ PreferenceManager.lastSave = lastSave; }

    public static void loadPreferences(){
        lastSave = prefs.getString("lastSave");

    }

    public static void savePrefereces(){

        prefs.putString("lastSave", lastSave);
        prefs.flush();
    }
}
