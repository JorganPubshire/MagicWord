import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.plugin.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;



public class MagicWord extends JavaPlugin{
	Configuration config;
	String word;
	ArrayList<Player> admins = new ArrayList<Player>();
	public static PermissionHandler permissionHandler;
	PluginManager pm;
	File permissionFile;
	Configuration permConfig;

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() {
		pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, new CommandListener(this), Priority.Normal, this);

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

		word = config.getString("Magic Word","");

		setupPermissions();

		if(permissionHandler != null){
			permissionFile = new File("plugins/Permissions/" + getServer().getWorlds().get(0).getName() + ".yml");
			permConfig = new Configuration(permissionFile);
		}

		System.out.println("MagicWord is working.");
		System.out.println("The Magic Word is: " + word);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
		Player player = (Player) sender;
		player.toString();
		if (cmdLabel.equalsIgnoreCase("group")){
			if(admins.contains(player)){
				if(args.length == 0){
					permConfig.load();
					List<String> keys = permConfig.getKeys("groups");
					for(int i = 0; i<keys.size();i++){
						player.sendMessage(keys.get(i));
					}
				}
				if(args.length == 1){
					permConfig.load();
					player.sendMessage(getServer().getPlayer(args[0]).getName() + " is in the group: " + permConfig.getString("users." + getServer().getPlayer(args[0]).getName() + ".group"));
				}
				if(args.length == 2){
					Player target = getServer().getPlayer(args[0]);
					String group = args[1];
					setGroup(player,target,group);
				}
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

	private void setupPermissions() {
		if (permissionHandler != null) {
			return;
		}

		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

		if (permissionsPlugin == null) {
			System.out.println("[MagicWord]Permission system not detected, defaulting to OP");
			return;
		}

		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		System.out.println("[MagicWord]Found and will use plugin "+((Permissions)permissionsPlugin).getDescription().getFullName());
	}

	public void setGroup(Player sender, Player player, String group){
		permConfig.load();
		if(permConfig.getKeys("users")!=null && !permConfig.getKeys("users").contains(player.getName())){
			permConfig.setProperty("users." + player.getName() + ".group", group);
			permConfig.setProperty("users." + player.getName() + ".permissions", "");
		}
		else{
			permConfig.setProperty("users." + player.getName() + ".group", group);
		}
		permConfig.save();
		sender.chat("/permissions -reload all");
		permissionHandler = null;
		setupPermissions();
	}
}
