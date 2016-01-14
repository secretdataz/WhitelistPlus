package th.in.ahri.WhitelistPlus.obj;

import th.in.ahri.WhitelistPlus.WhitelistPlus;

import java.util.UUID;

public class WLEntry {

    public int level;
    public UUID uuid;

    public WLEntry(UUID uuid){
        this(uuid, 0);
    }

    public WLEntry(UUID uuid, int level){
        this.uuid = uuid;
        this.level = level;
    }

    public boolean canJoin(){
        return this.level >= WhitelistPlus.instance.getJoinLevel();
    }
}
