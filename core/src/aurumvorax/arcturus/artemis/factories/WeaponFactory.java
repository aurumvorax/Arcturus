package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.Mount;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import aurumvorax.arcturus.services.EntityData;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;

public enum WeaponFactory{

    INSTANCE;

    private static World world;
    private static Archetype protoCannon;
    private static Archetype protoBeam;
    private static Archetype protoLauncher;

    private static ComponentMapper<Mounted> mMounted;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<Turret> mTurret;
    private static ComponentMapper<Cannon> mCannon;
    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Launcher> mLauncher;


    public static void init(World world){
        WeaponFactory.world = world;
        world.inject(INSTANCE);

        Archetype protoWeapon = new ArchetypeBuilder()
                .add(Mounted.class)
                .add(SimpleSprite.class)
                .add(Turret.class)
                .build(world);
        protoCannon = new ArchetypeBuilder(protoWeapon)
                .add(Cannon.class)
                .build(world);
        protoBeam = new ArchetypeBuilder(protoWeapon)
                .add(Beam.class)
                .build(world);
        protoLauncher = new ArchetypeBuilder(protoWeapon)
                .add(Launcher.class)
                .build(world);
    }

    static int create(String name, int ship, Mount.Weapon mount, int slot){
        WeaponData data = EntityData.getWeaponData(name);

        switch(data.type){
            case CANNON:
                int cannon = world.create(protoCannon);
                buildTurret(cannon, data, ship, mount);
                Cannon c = mCannon.get(cannon);
                c.name = name;
                c.slot = slot;
                c.launches = data.launches;
                c.burstTime = data.delay;
                c.reloadTime = data.reload;
                c.barrels = data.barrels;
                setCannonData(c, data.launches);
                c.threat = data.threat;
                return cannon;

            case BEAM:
                int beam = world.create(protoBeam);
                buildTurret(beam, data, ship, mount);
                Beam b = mBeam.get(beam);
                b.name = name;
                b.slot = slot;
                b.imgName = data.beamImgName;
                b.offsetY = data.beamImgCenter.y;
                b.range = data.maxRange;
                b.barrels = data.barrels;
                b.dps = data.dps;
                b.threat = data.threat;
                return beam;

            case LAUNCHER:
                int launcher = world.create(protoLauncher);
                buildTurret(launcher, data, ship, mount);
                Launcher l = mLauncher.get(launcher);
                l.name = name;
                l.slot = slot;
                l.launches = data.launches;
                l.burstTime = data.delay;
                l.reloadTime = data.reload;
                l.barrels = data.barrels;
                l.threat = data.threat;
                setCannonData(l, data.launches);
                return launcher;

            default:
                throw new IllegalArgumentException(data.type + " is not a known type");
        }
    }

    static void extract(ShipData.Loadout l, int weaponID){
        if(mBeam.has(weaponID)){
            Beam b = mBeam.get(weaponID);
            l.weapons.put(b.slot, b.name);
        }else if(mCannon.has(weaponID)){
            Cannon b = mCannon.get(weaponID);
            l.weapons.put(b.slot, b.name);
        }else if(mLauncher.has(weaponID)){
            Launcher m = mLauncher.get(weaponID);
            l.weapons.put(m.slot, m.name);
        }
    }

    private static void buildTurret(int entityID, WeaponData data, int ship, Mount.Weapon mount){
        Mounted m = mMounted.get(entityID);
        m.parent = ship;
        m.location = mount.location;
        m.theta = mount.angle;

        SimpleSprite s = mSprite.get(entityID);
        s.name = data.imgName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        s.layer = Renderer.Layer.DETAIL;

        Turret t = mTurret.get(entityID);
        t.omegaMax = data.rotationSpeed;
        t.setArcs(mount.angle, mount.arc);
    }

    private static void setCannonData(Cannon c, String type){
        ProjectileData data = EntityData.getProjectileData(type);
        c.speed = data.speed;
        c.range = data.speed * data.duration;

        if(c instanceof Launcher){
            ((Launcher) c).missileTurnRate = data.turnRate;
        }
    }
}
