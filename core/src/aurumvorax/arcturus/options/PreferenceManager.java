package aurumvorax.arcturus.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferenceManager{

    private Preferences prefs = Gdx.app.getPreferences("options.cfg");

    private String lastSave;
    private float uiScale;


    public String getLastSave(){ return lastSave; }
    public float getUiScale(){ return uiScale; }

    public void setLastSave(String lastSave){ lastSave = lastSave; }
    public void setUiScale(float scale){ uiScale = scale; }

    public void loadPreferences(){
        lastSave = prefs.getString("lastSave", null);
        uiScale = prefs.getFloat("ui_scale", 1f);
    }

    public void savePrefereces(){
        if(lastSave != null)
            prefs.putString("lastSave", lastSave);

        prefs.putFloat("ui_scale", uiScale);

        prefs.flush();
    }
}
