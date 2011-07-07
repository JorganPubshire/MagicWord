import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;


public class BuildListener extends BlockListener{
	MagicWord plugin;
	
	public BuildListener(MagicWord instance){
		plugin = instance;
	}
	
	public void onBlockPlace(BlockPlaceEvent event){
		if(!plugin.builders.contains(event.getPlayer())){
			event.setCancelled(true);
		}
	}
	
	public void onBlockDamage(BlockDamageEvent event){
		if(!plugin.builders.contains(event.getPlayer())){
			event.setCancelled(true);
		}
	}
}
