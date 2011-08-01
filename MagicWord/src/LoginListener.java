import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;


public class LoginListener extends PlayerListener{
	MagicWord plugin;
	
	public LoginListener(MagicWord instance){
		plugin = instance;
	}
	
	public void onPlayerLogin(PlayerLoginEvent event){
		plugin.permConfig.load();
		if(!plugin.permConfig.getKeys("users").contains(event.getPlayer().getName())){
			plugin.permConfig.setProperty("users." + event.getPlayer().getName() + ".group", "Default");
			plugin.permConfig.setProperty("users." + event.getPlayer().getName() + ".permissions", "");
			plugin.messageAdmins(ChatColor.YELLOW + event.getPlayer().getName() + " has joined the server and been set to the Default group");
		}
		plugin.permConfig.save();
	}
}
