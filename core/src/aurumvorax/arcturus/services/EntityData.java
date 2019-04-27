package aurumvorax.arcturus.services;

import aurumvorax.arcturus.artemis.factories.*;
import aurumvorax.arcturus.galaxy.StellarData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SerializationException;

import java.util.HashMap;
import java.util.Set;

public class EntityData{

    private static HashMap<String, EffectData> effects = new HashMap<>();
    private static HashMap<String, ShipData.Stock> ships = new HashMap<>();
    private static HashMap<String, WeaponData> weapons = new HashMap<>();
    private static HashMap<String, ProjectileData> projectiles = new HashMap<>();
    private static HashMap<String, StellarData.Base> systems = new HashMap<>();

    public static void load(){
        FileList dataFiles;

        try{
            FileHandle listFile = Gdx.files.internal("DataFileList.json");
            dataFiles = Services.json.fromJson(FileList.class, listFile);

        }catch(Exception e){
            Gdx.app.error("EntityData", "Invalid/Missing master data index file", e);
            throw e;
        }

        for(String fileName : dataFiles.effects){
            try{
                FileHandle dataFile = Gdx.files.internal(Services.EFFECT_PATH + fileName);
                EffectWrapper effectWrapper = Services.json.fromJson(EffectWrapper.class, dataFile);

                if(EffectData.verify(effectWrapper.data)){
                    effects.put(effectWrapper.name, effectWrapper.data);
                    Gdx.app.debug("INIT", "Registered effect - " + effectWrapper.name);
                }else
                    throw new SerializationException("Invalid or incomplete data file");

            }catch(SerializationException e){
                Gdx.app.error("EntityData", "Unable to register effect - " + fileName, e);
            }
        }

        for(String fileName : dataFiles.ships){
            try{
                FileHandle dataFile = Gdx.files.internal(Services.SHIP_PATH + fileName);
                ShipWrapper shipWrapper = Services.json.fromJson(ShipWrapper.class, dataFile);

                if(ShipData.verifyStock(shipWrapper.data)){
                    ships.put(shipWrapper.name, shipWrapper.data);
                    Gdx.app.debug("INIT", "Registered ship - " + shipWrapper.name);
                }else
                    throw new SerializationException("Invalid or incomplete data file");

            }catch(SerializationException e){
                Gdx.app.error("EntityData", "Unable to register ship - " + fileName);
            }
        }

        for(String fileName : dataFiles.weapons){
            try{
                FileHandle dataFile = Gdx.files.internal(Services.WEAPON_PATH + fileName);
                WeaponWrapper weaponWrapper = Services.json.fromJson(WeaponWrapper.class, dataFile);

                if(WeaponData.verify(weaponWrapper.data)){
                    weapons.put(weaponWrapper.name, weaponWrapper.data);
                    Gdx.app.debug("INIT", "Registered Weapon - " + weaponWrapper.name);
                }else
                    throw new SerializationException("Invalid or incomplete data file");

            }catch(SerializationException e){
                Gdx.app.error("EntityData", "Unable to register weapon - " + fileName);
            }
        }

        for(String fileName : dataFiles.projectiles){
            try{
                FileHandle dataFile = Gdx.files.internal(Services.PROJECTILE_PATH + fileName);
                ProjectileWrapper projectileWrapper = Services.json.fromJson(ProjectileWrapper.class, dataFile);
                if(ProjectileData.verify(projectileWrapper.data)){
                    projectiles.put(projectileWrapper.name, projectileWrapper.data);
                    Gdx.app.debug("INIT", "Registered Projectile - " + projectileWrapper.name);
                }else
                    throw new SerializationException("Invalid or incomplete data file");

            }catch(SerializationException e){
                Gdx.app.error("EntityData", "Unable to register projectile - " + fileName);
            }
        }

        for(String fileName : dataFiles.systems){
            try{
                FileHandle dataFile = Gdx.files.internal(Services.TERRAIN_PATH + fileName);
                SystemWrapper systemWrapper = Services.json.fromJson(SystemWrapper.class, dataFile);
                if(StellarData.verify(systemWrapper.data)){
                    systems.put(systemWrapper.name, systemWrapper.data);
                    Gdx.app.debug("INIT", "Registered Celestial Body - " + systemWrapper.name);
                }else
                    throw new SerializationException("Invalid of incomplete data file");

            }catch(SerializationException e){
                Gdx.app.error("EntityData", "Unable to register system - " + fileName);
            }
        }
    }

    public static Set<String> getShipTypes(){ return ships.keySet(); }
    public static Set<String> getSolarSystems(){ return systems.keySet(); }

    public static EffectData getEffectData(String type){
        if(!effects.containsKey(type)){
            Gdx.app.error("EntityData", "Effect data not found - " + type);
            return null;
        }
        return effects.get(type);
    }

    public static ShipData.Stock getShipData(String type){
        if(!ships.containsKey(type)){
            Gdx.app.error("EntityData", "Ship data not found - " + type);
            return null;
        }
        return ships.get(type);
    }

    public static WeaponData getWeaponData(String type){
        if(!weapons.containsKey(type)){
            Gdx.app.error("EntityData", "Weapon data not found - " + type);
            return null;
        }
        return weapons.get(type);
    }

    public static ProjectileData getProjectileData(String type){
        if(!projectiles.containsKey(type)){
            Gdx.app.error("EntityData", "Projectile data not found - " + type);
            return null;
        }
        return projectiles.get(type);
    }

    public static StellarData.Base getStellarData(String type){
        if(!systems.containsKey(type)){
            Gdx.app.error("EntityData", "Stellar body data not found - " + type);
            return null;
        }
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
        ShipData.Stock data;
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
        StellarData.Base data;
    }

    private static class EffectWrapper{
        String name;
        EffectData data;
    }

}
