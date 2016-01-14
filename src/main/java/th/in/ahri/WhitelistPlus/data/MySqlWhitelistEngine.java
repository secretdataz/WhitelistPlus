package th.in.ahri.WhitelistPlus.data;

import lib.PatPeter.SQLibrary.MySQL;
import th.in.ahri.WhitelistPlus.WhitelistPlus;
import th.in.ahri.WhitelistPlus.obj.WLEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.logging.Logger;

public class MySqlWhitelistEngine implements WhitelistEngine {

    private final String host;
    private final int port;
    private final String db;
    private final String user;
    private final String password;
    private final Logger logger;
    public MySQL sql;

    public MySqlWhitelistEngine(String host, int port, String db, String user, String password){
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.password = password;
        this.logger = WhitelistPlus.instance.logger;
        sql = new MySQL(logger, "[WhitelistPlus]", host, port, db, user, password);
    }
    public WLEntry get(UUID uuid) {
        try{
            if(!sql.isOpen()){
                if(!sql.open()){
                    logger.severe("Can't get UUID. Connection to MySQL server failed.");
                    return new WLEntry(UUID.fromString(uuid.toString()), -1);
                }
            }
            ResultSet resultSet = sql.query("SELECT * FROM 'whitelistplus' WHERE uuid = " + uuid.toString());
            if(!resultSet.next()) return new WLEntry(UUID.fromString(uuid.toString()), -1);
            UUID newUUID = UUID.fromString(resultSet.getString("uuid"));
            WLEntry entry = new WLEntry(newUUID, resultSet.getInt("level"));
            resultSet.close();
            return entry;
        }catch(Exception e){
            logger.severe("Error opening connection to MySQL server at " + host + ":" + Integer.toString(port));
            e.printStackTrace();
            return new WLEntry(UUID.fromString(uuid.toString()), -1);
        }
    }

    public boolean set(UUID uuid, int level) {
        try {
            if (!sql.isOpen()) {
                if (!sql.open()) {
                    logger.severe("Can't get UUID. Connection to MySQL server failed.");
                    return false;
                }
            }
            PreparedStatement statement;
            if(get(uuid).level > -1){
                statement = sql.prepare("UPDATE whitelistplus SET level = ? WHERE uuid = ?");
                statement.setInt(1, level);
                statement.setString(2, uuid.toString());
            }else{
                statement = sql.prepare("INSERT INTO whitelistplus values(?,?)");
                statement.setString(1, uuid.toString());
                statement.setInt(2, level);
            }
            return statement.executeUpdate() > 0;

        }catch (Exception e){
            logger.severe("Error opening connection to MySQL server at " + host + ":" + Integer.toString(port));
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(UUID uuid) {
        try {
            if (!sql.isOpen()) {
                if (!sql.open()) {
                    logger.severe("Can't get UUID. Connection to MySQL server failed.");
                    return false;
                }
            }
            PreparedStatement statement;
            statement = sql.prepare("DELETE FROM whitelistplus WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            return statement.executeUpdate() > 0;
        }catch (Exception e){
            logger.severe("Error opening connection to MySQL server at " + host + ":" + Integer.toString(port));
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasDatabase() {
        try{
            if(!sql.isOpen()){
                if(!sql.open()){
                    logger.severe("Can't get UUID. Connection to MySQL server failed.");
                    return false;
                }
            }
            ResultSet resultSet = sql.query("SHOW TABLES LIKE 'whitelistplus'");
            int size = 0;
            size = resultSet.getRow();
            resultSet.close();
            return size > 0;
        }catch (Exception e){
            logger.severe("Error opening connection to MySQL server at " + host + ":" + Integer.toString(port));
            e.printStackTrace();
            return false;
        }
    }

    public void createDatabase() {
        try{
            if(!sql.isOpen()){
                if(!sql.open()){
                    logger.severe("Can't create database. Connection to MySQL server failed.");
                    return;
                }
            }
            sql.insert("CREATE TABLE IF NOT EXISTS `whitelistplus` (" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                    "  `uuid` char(36) COLLATE utf8_unicode_ci NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'," +
                    "  `level` tinyint(4) NOT NULL DEFAULT '0'," +
                    "  PRIMARY KEY (`id`)," +
                    "  UNIQUE KEY `uuid` (`uuid`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;");
        }catch(Exception e){
            logger.severe("Error opening connection to MySQL server at " + host + ":" + Integer.toString(port));
            e.printStackTrace();
        }
    }
}
