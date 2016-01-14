package th.in.ahri.WhitelistPlus;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import th.in.ahri.WhitelistPlus.data.MySqlWhitelistEngine;
import th.in.ahri.WhitelistPlus.data.WhitelistEngine;
import th.in.ahri.WhitelistPlus.listener.WhitelistListener;

import java.io.File;
import java.util.logging.Logger;

public class WhitelistPlus extends JavaPlugin{

    public Logger logger;
    public static WhitelistPlus instance;
    private int joinLevel;
    private String kickMessage;
    private WhitelistEngine engine;

    public void onEnable(){
        WhitelistPlus.instance = this;
        logger = Bukkit.getLogger();
        if(!(new File(getDataFolder(), "config.yml").exists())) {
            saveDefaultConfig();
        }
        Configuration config = getConfig();
        if(config.getString("Engine").equalsIgnoreCase("MySQL")){
            engine = new MySqlWhitelistEngine(config.getString("MySQL.host", "localhost"), config.getInt("MySQL.port", 3306)
            , config.getString("MySQL.database"), config.getString("MySQL.username"), config.getString("MySQL.password"));
        }else{
            logger.severe("Unknown whitelist engine. Disabling WhitelistPlus.");
            this.setEnabled(false);
            return;
        }
        joinLevel = config.getInt("JoinLevel.Level");
        kickMessage = config.getString("JoinLevel.KickMessage");
        Bukkit.getPluginManager().registerEvents(new WhitelistListener(), this);
    }

    public int getJoinLevel(){
        return joinLevel;
    }

    public void setJoinLevel(int level){
        this.joinLevel = joinLevel;
    }

    public WhitelistEngine getWhitelistEngine(){
        return engine;
    }

    public String getKickMessage() {
        return kickMessage;
    }
}
