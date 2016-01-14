package th.in.ahri.WhitelistPlus.command;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.ProfileRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import th.in.ahri.WhitelistPlus.WhitelistPlus;

import java.util.UUID;

public class WhitelistCommand implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("wlsetjoinlevel")) {
            return parseSetLevel(commandSender, command, s, strings);
        }else{
            return parseModify(commandSender, command, s, strings);
        }
    }

    private boolean parseModify(CommandSender sender, Command command, String s, String[] args){
        int level = 0;
        if(command.getName().equalsIgnoreCase("wldelete")){
            if(args.length < 1) return false;
        }else{
            if(args.length < 2) return false;
            try{
                level = Integer.parseInt(args[1]);
            }catch(Exception e){
                return false;
            }
        }
        String name = args[0];
        ProfileRepository profileRepo = new HttpProfileRepository("minecraft");
        Profile[] profiles = profileRepo.findProfilesByNames(name);
        if(profiles.length < 1){
            sender.sendMessage("Can't resolve UUID from name given.");
            return true;
        }
        UUID uuid = UUID.fromString(profiles[0].getId());
        if(command.getName().equalsIgnoreCase("wldelete")) {
            if(WhitelistPlus.instance.getWhitelistEngine().delete(uuid)){
                sender.sendMessage("Removed " + name + " (" + profiles[0].getId() + " from whitelist.");
            }else{
                sender.sendMessage("Error removing " + name + " from whitelist. Maybe " + name + " isn't in the whitelist already?");
            }
        }else if(command.getName().equalsIgnoreCase("wladd") || command.getName().equalsIgnoreCase("wledit")){
            if(WhitelistPlus.instance.getWhitelistEngine().set(uuid, level)){
                sender.sendMessage("Added " + name + " (" + profiles[0].getId() + " to whitelist with level " + args[1] + ".");
            }else{
                sender.sendMessage("Error adding " + name + " to whitelist.");
            }
        }
        return true;
    }

    private boolean parseSetLevel(CommandSender sender, Command command, String s, String[] args){
        try{
            int level = Integer.parseInt(args[0]);
            WhitelistPlus.instance.setJoinLevel(level);
            sender.sendMessage("Set join level to " + args[0]);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
