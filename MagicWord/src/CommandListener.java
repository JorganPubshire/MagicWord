import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;


public class CommandListener extends PlayerListener{
	MagicWord plugin;
	
	public CommandListener(MagicWord instance){
		plugin = instance;
	}
	
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		if(event.getMessage().equalsIgnoreCase("/" + plugin.word)){
			event.getPlayer().sendMessage(ChatColor.YELLOW + "You said the magic word! The Admins and Mods will now decide if you are worthy to build.");
			String message = event.getPlayer().getName() + " wants building rights and has said the magic word";  
			plugin.messageAdmins(message);
			event.setCancelled(true);
		}
	}
}
