package aurumvorax.arcturus.services;

import aurumvorax.arcturus.artemis.factories.*;
import aurumvorax.arcturus.artemis.factories.SolarData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Set;

public class EntityData{

    private static HashMap<String, EffectData> effects = new HashMap<>();
    private static HashMap<String, ShipData> ships = new HashMap<>();
    private static HashMap<String, WeaponData> weapons = new HashMap<>();
    private static HashMap<String, ProjectileData> projectiles = new HashMap<>();
    private static HashMap<String, SolarData> systems = new HashMap<>();

    public static void load(){
        FileHandle listFile = Gdx.files.internal("DataFileList.json");
        FileList dataFiles = Services.json.fromJson(FileList.class, listFile);

        for(String fileName : dataFiles.effects){
            FileHandle dataFile = Gdx.files.internal(Services.EFFECT_PATH + fileName);
            EffectWrapper effectWrapper = Services.json.fromJson(EffectWrapper.class, dataFile);
            effects.put(effectWrapper.name, effectWrapper.data);
            Gdx.app.debug("INIT", "Registered Effect - " + effectWrapper.name);
        }

        for(String fileName : dataFiles.ships){
            FileHandle dataFile = Gdx.files.internal(Services.SHIP_PATH + fileName);
            ShipWrapper shipWrapper = Services.json.fromJson(ShipWrapper.class, dataFile);
            ships.put(shipWrapper.name, shipWrapper.data);
            Gdx.app.debug("INIT", "Registered Ship - " + shipWrapper.name);
        }

        for(String fileName : dataFiles.weapons){
            FileHandle dataFile = Gdx.files.internal(Services.WEAPON_PATH + fileName);
            WeaponWrapper weaponWrapper = Services.json.fromJson(WeaponWrapper.class, dataFile);
            weapons.put(weaponWrapper.name, weaponWrapper.data);
            Gdx.app.debug("INIT", "Registered Weapon - " + weaponWrapper.name);
        }

        for(String fileName : dataFiles.projectiles){
            FileHandle dataFile = Gdx.files.internal(Services.PROJECTILE_PATH + fileName);
            ProjectileWrapper projectileWrapper = Services.json.fromJson(ProjectileWrapper.class, dataFile);
            projectiles.put(projectileWrapper.name, projectileWrapper.data);
            Gdx.app.debug("INIT", "Registered Projectile - " + projectileWrapper.name);
        }

        for(String fileName : dataFiles.systems){
            FileHandle dataFile = Gdx.files.internal(Services.TERRAIN_PATH + fileName);
            SystemWrapper systemWrapper = Services.json.fromJson(SystemWrapper.class, dataFile);
            systems.put(systemWrapper.name, systemWrapper.data);
            Gdx.app.debug("INIT", "Registered Celestial Body - " + systemWrapper.name);
        }
    }

    public static Set<String> getShipTypes(){ return ships.keySet(); }
    public static Set<String> getWeaponTypes(){ return weapons.keySet(); }

    public static EffectData getEffectData(String type){
        if(!effects.containsKey(type))
            throw new IllegalArgumentException("Invalid effect type - " + type);
        return effects.get(type);
    }

    public static ShipData getShipData(String type){
        if(!ships.containsKey(type))
            throw new IllegalArgumentException("Invalid ship type - " + type);
        return ships.get(type);
    }

    public static WeaponData getWeaponData(String type){
        if(!weapons.containsKey(type))
            throw new IllegalArgumentException("Invalid weapon type - " + type);
        return weapons.get(type);
    }

    public static ProjectileData getProjectileData(String type){
        if(!projectiles.containsKey(type))
            throw new IllegalArgumentException("Invalid weapon type - " + type);
        return projectiles.get(type);
    }

    public static SolarData getSystemData(String type){
        if(!systems.containsKey(type))
            throw new IllegalArgumentException("Invalid solar system - " + type);
        return systems.get(type);
    }


    public static class FileList{       // Disposable class used for loading all data file names
        public Array<String> effects = new Array<>();
        public Array<String> projectiles = new Array<>();
        public Array<String> ships = new Array<>();
        public Array<String> systems = new Array<>();
        public Array<String> weapons = new Array<>();
    }

    private static class ShipWrapper{
        String name;
        ShipData data;
    }

    private static class WeaponWrapper{
        String name;
        WeaponData data;
    }

    private static class ProjectileWrapper{
        String name;
        ProjectileData data;
    }

    private static class SystemWrapper{
        String name;
        SolarData data;
    }

    private static class EffectWrapper{
        String name;
        EffectData data;
    }

}
