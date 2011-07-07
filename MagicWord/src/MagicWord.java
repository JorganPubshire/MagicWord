import java.io.File;
import java.util.ArrayList;
import java.util.List;
//import java.util.List;
//import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
//import org.bukkit.util.config.ConfigurationNode;


public class MagicWord extends JavaPlugin{
	Configuration config;
	String word;
	ArrayList<Player> builders = new ArrayList<Player>();
	ArrayList<Player> admins = new ArrayList<Player>();

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, new CommandListener(this), Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, new BuildListener(this), Priority.High, this);
		pm.registerEvent(Event.Type.BLOCK_DAMAGE, new BuildListener(this), Priority.High, this);

		new File("plugins/MagicWord").mkdir();
		File data = new File("plugins/MagicWord/config.yml");
		if(!data.exists()){
			try{
				data.createNewFile();
			}
			catch(Exception e){
				e.printStackTrace();
				return;
			}
		}

		config = new Configuration(data);

		config.load();

		if(!config.getKeys().contains("Magic Word")){
			config.setProperty("Magic Word", "Please");
		}
		
		if(!config.getKeys().contains("Allowed Players")){
			List<String> name = new ArrayList<String>();
			name.add("JorganPubshire");
			config.setProperty("Allowed Players", name);
		}
		if(!config.getKeys().contains("Admins/Mods")){
			List<String> name = new ArrayList<String>();
			name.add("JorganPubshire");
			config.setProperty("Admins/Mods", name);
		}
		
		config.save();

		List<String> names = config.getStringList("Admins/Mods", null);
		
		for(String name : names){
			admins.add(getServer().getPlayer(name));
		}
		
		names = config.getStringList("Allowed Players", null);
		
		for(String name : names){
			builders.add(getServer().getPlayer(name));
		}
		

		word = config.getString("Magic Word","");


		System.out.println("MagicWord is working.");
		System.out.println("The Magic Word is: " + word);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
		Player player = (Player) sender;
		player.toString();
		if(cmdLabel.equalsIgnoreCase("allow")){
			if(args.length == 1){
				if(admins.contains(player)){
					Player target = getServer().getPlayer(args[0]);
					allowPlayer(target);
					getServer().broadcastMessage(ChatColor.GREEN + player.getName() + " has granted building rights to " + target.getName());
				}
			}
		}
		else if(cmdLabel.equalsIgnoreCase("deny")){
			if(args.length == 1){
				Player target = getServer().getPlayer(args[0]);
				denyPlayer(target);
				getServer().broadcastMessage(ChatColor.RED + target.getName() + " has lost building rights!");
			}
		}
		
		else if(cmdLabel.equalsIgnoreCase("word")){
			if(admins.contains(player)){
				if(args.length == 0){
					player.sendMessage(ChatColor.YELLOW + "The Magic Word is: " + getWord());
				}
				else if(args.length == 1){
					setWord(args[0]);
					player.chat("/word");
				}
				
			}
		}
		return true;
	}
	
	public void allowPlayer(Player player){
		config.load();
		if(!builders.contains(player)){
			builders.add(player);
			config.setProperty("Allowed Players", config.getStringList("Allowed Players", null).add(player.getName()));
		}
			config.save();
	}
	
	public void denyPlayer(Player player){
		config.load();
		if(builders.contains(player)){
			builders.remove(player);
			config.setProperty("Allowed Players", config.getStringList("Allowed Players", null).remove(player.getName()));
		}
		config.save();
	}
	
	public void setWord(String word){
		config.load();
		config.setProperty("Magic Word", word);
		config.save();
		this.word = word;
		
	}
	
	public String getWord(){
		return word;
	}
	
	public void messageAdmins(String message){
		for(Player name : admins){
			name.sendMessage(ChatColor.YELLOW + message);
		}
	}

}
