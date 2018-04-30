package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;
import java.util.Set;

public class EntityData{

    private static HashMap<String, ShipData> ships = new HashMap<>();
    private static HashMap<String, WeaponData> weapons = new HashMap<>();
    private static HashMap<String, ProjectileData> projectiles = new HashMap<>();
    private static HashMap<String, TerrainData> terrain = new HashMap<>();

    public static void load(){
        for(FileHandle entry : Services.SHIP_PATH.list()){
            ShipWrapper shipWrapper = Services.json.fromJson(ShipWrapper.class, entry);
            ships.put(shipWrapper.name, shipWrapper.data);
            Gdx.app.debug("INIT", "Registered Ship - " + shipWrapper.name);
        }

        for(FileHandle entry : Services.WEAPON_PATH.list()){
            WeaponWrapper weaponWrapper = Services.json.fromJson(WeaponWrapper.class, entry);
            weapons.put(weaponWrapper.name, weaponWrapper.data);
            Gdx.app.debug("INIT", "Registered Weapon - " + weaponWrapper.name);
        }

        for(FileHandle entry : Services.PROJECTILE_PATH.list()){
            ProjectileWrapper projectileWrapper = Services.json.fromJson(ProjectileWrapper.class, entry);
            projectiles.put(projectileWrapper.name, projectileWrapper.data);
            Gdx.app.debug("INIT", "Registered Projectile - " + projectileWrapper.name);
        }

        for(FileHandle entry : Services.TERRAIN_PATH.list()){
            TerrainWrapper terrainWrapper = Services.json.fromJson(TerrainWrapper.class, entry);
            terrain.put(terrainWrapper.name, terrainWrapper.data);
            Gdx.app.debug("INIT", "Registered Celestial Body - " + terrainWrapper.name);
        }
    }

    public static Set<String> getShipTypes(){ return ships.keySet(); }
    public static Set<String> getWeaponTypes(){ return weapons.keySet(); }
    public static Set<String> getProjectileData(){ return projectiles.keySet(); }

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

    public static TerrainData getTerrainData(String type){
        if(!terrain.containsKey(type))
            throw new IllegalArgumentException("Invalid terrain type = " + type);
        return terrain.get(type);
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

    private static class TerrainWrapper{
        String name;
        TerrainData data;
    }

    private class EffectWrapper{
        String name;
        EffectData data;
    }

}
