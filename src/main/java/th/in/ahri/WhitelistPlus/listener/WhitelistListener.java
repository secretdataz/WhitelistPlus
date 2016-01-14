package th.in.ahri.WhitelistPlus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import th.in.ahri.WhitelistPlus.WhitelistPlus;
import th.in.ahri.WhitelistPlus.data.WhitelistEngine;

import java.util.UUID;

public class WhitelistListener implements Listener {

    @EventHandler
    public void onPCLoginEvent(PlayerLoginEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        WhitelistEngine engine = WhitelistPlus.instance.getWhitelistEngine();
        if(engine.get(uuid).level < WhitelistPlus.instance.getJoinLevel()) {
            event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
            event.setKickMessage(WhitelistPlus.instance.getKickMessage());
        }
    }
}
