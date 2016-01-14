package th.in.ahri.WhitelistPlus.data;

import th.in.ahri.WhitelistPlus.obj.WLEntry;

import java.util.UUID;

public interface WhitelistEngine {

    WLEntry get(UUID uuid);
    boolean set(UUID uuid, int level);
    boolean delete(UUID uuid);
    boolean hasDatabase();
    void createDatabase();
}
