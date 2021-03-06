package com.github.sirknighting.eatFlowers;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class FlowerEatingPlugin extends JavaPlugin implements Listener{
	@Override
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event){
		Player p=event.getPlayer();
		ItemStack item=p.getItemInHand();
		//item.getType()+""
		switch(item.getType()){
		case DOUBLE_PLANT: break;
		case YELLOW_FLOWER: break;
		case RED_ROSE: break;
		default: return;
		}

		Action action=event.getAction();
		switch(action){
		case RIGHT_CLICK_AIR:{
			eatFlowerInHand(event.getPlayer());
			//event.setCancelled(true);//NOT SURE IF THIS IS NECESSARY
			return;
		}

		case RIGHT_CLICK_BLOCK:{//TODO MAKE SURE TO DEAL WITH FLOWERPOTS TOO;
			Block bWBWG=event.getClickedBlock().getRelative(event.getBlockFace());//bWBWG=blockWhereBlockWouldGo
			if(bWBWG.getType()!=Material.AIR) return;
			if(event.getClickedBlock().getType().equals(Material.FLOWER_POT)){
				return;
			}//FLOWER POTS ARE ANNOYING,
			Material matUnder=bWBWG.getRelative(BlockFace.DOWN).getType();//IF WE're NOT DEALING WITH A FLOWERPOT,
			if((matUnder+"").equals("DIRT")||(matUnder+"").equals("GRASS")||(matUnder+"").equals("SOIL")){//THIS CONDITIONAL DEALS WITH IF THE FLOWER IS PLACEABLE, ignores light level.
				if(item.getType().equals(Material.DOUBLE_PLANT)){
					if(bWBWG.getRelative(BlockFace.UP).getType()==Material.AIR)return;
				}else return;

			}
			//TODO MAKE THIS PART PROPER AND WORKING
			eatFlowerInHand(event.getPlayer());
			return;
		}
		default:
			break;
		}

		//Result res=event.useItemInHand();
	}

	private void eatFlowerInHand(Player player){
		if(player.getFoodLevel()==20)return;
		ItemStack item=player.getItemInHand();
		int amount=item.getAmount();
		if(amount==0)return;
		//TODO maybe check to see if the item is a flower
		amount--;
		if(amount==0){
			item.setType(Material.AIR);
		}
		else item.setAmount(amount);

		player.setItemInHand(item);
		int newFoodLevel=player.getFoodLevel()+2;
		if(newFoodLevel>20)newFoodLevel=20;
		player.setFoodLevel(newFoodLevel);
		player.getWorld().playSound(player.getLocation(), Sound.EAT,(float) 1,(float) 1);//MODIFY THE MAGIC VALUES to fit a real eating sound

	}
}
