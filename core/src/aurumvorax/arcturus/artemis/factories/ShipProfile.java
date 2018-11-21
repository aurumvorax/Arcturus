package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.services.EntityData;

public class ShipProfile{

    public String name;
    public String type;
    public Loadout loadout;

    public ShipProfile(String n, String t, String l){
        name = n;
        type = t;
        if(l == null)
            loadout = new Loadout();
        else
            loadout = EntityData.getShipData(type).loadouts.get(l);
    }

    public ShipProfile(ShipProfile p){
        name = p.name;
        type = p.type;
        loadout = new Loadout(p.loadout);
    }
}
