package nl.makertim.MMOmain;

import nl.makertim.MMOmain.PlayerUUID.PlayerUUIDLookup;
import nl.makertim.MMOmain.command.CommandHandler;
import nl.makertim.MMOmain.lib.MMOOutlaws;
import nl.makertim.MMOmain.lib.Mission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MKTEventHandler implements Listener{
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		PlayerStats pls = PlayerStats.getPlayerStats(e.getEntity());
		if(pls.isInMission){
			for(Mission mission : GameWorld.getGameWorld().missions){
				for(Player pl : mission.getAllPlayers()){
					if(Mission.getMissionFromPlayer(e.getEntity()) == mission){
						MMOOutlaws.sendActionMessage(pl, ChatColor.BOLD + e.getDeathMessage());
					}
				}
			}
			e.setDeathMessage("");
		}
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e){
		updatePlayerUUID(e.getPlayer());
		new PlayerStats(new PlayerUUID(e.getPlayer()));
		if(!MMOOutlaws.isAdmin(e.getPlayer())){
			Bukkit.getScheduler().runTaskLater(Refrence.main, new Runnable(){
				Player pl;
				public Runnable setPlayer(Player newPL){
					pl = newPL;
					return this;
				}
				@Override
				public void run(){
					MKTEventHandler.cleanupPlayerInventory(pl);
					for(PotionEffect effect : pl.getActivePotionEffects()){
						pl.removePotionEffect(effect.getType());
					}
				}
			}.setPlayer(e.getPlayer()), 10);
		}		
		for(Mission mission : GameWorld.getGameWorld().missions){
			mission.updateServerJoinEvent(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onDropEvent(PlayerDropItemEvent e){
		if(!MMOOutlaws.isAdmin(e.getPlayer())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInvChange(InventoryClickEvent e){
		if(!MMOOutlaws.isAdmin((Player)e.getWhoClicked())){
			e.setResult(Result.DENY);
		}
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		String command = e.getMessage().replaceFirst("/", "").split(" ")[0];
		if(command.equalsIgnoreCase("reload")){
			e.getPlayer().sendMessage("JE MAG NIET RELOADEN");
			e.setCancelled(true);
		}
		String[] rawArgs = e.getMessage().split(" ", 2);
		String[] args = null;
		if(rawArgs.length > 1){
			args = rawArgs[1].split(" ");
		}else{
			args = new String[0];
		}
		if(CommandHandler.getInstance().triggerCommand(command, args, e.getPlayer())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPleayerLeave(PlayerQuitEvent e){
		Mission mission;
		if((mission = Mission.getMissionFromPlayer(e.getPlayer())) != null){
			mission.leavePlayer(PlayerStats.getPlayerStats(e.getPlayer()));
		}
		PlayerStats toRemove = PlayerStats.getPlayerStats(e.getPlayer());
		toRemove.isInMission = false;
		toRemove.save();
		Refrence.playerStats.remove(toRemove);
	}
	
	@EventHandler
	public void onPlayerDammage(EntityDamageEvent e){
		if(e.getCause().equals(DamageCause.FALL) || e.getCause().equals(DamageCause.FALLING_BLOCK)){
			Block a = e.getEntity().getLocation().add(0, -1, 0).getBlock();
			Block b = e.getEntity().getLocation().add(0, -2, 0).getBlock();
			if(a.getType().equals(Material.HAY_BLOCK) || b.getType().equals(Material.HAY_BLOCK)){
				e.setDamage(0D);
			}else{
				if(e.getEntity() instanceof Player){
					Player pl = (Player)e.getEntity();
					if(pl.isSneaking()){
						pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Refrence.random.nextInt((int)(e.getDamage()*40+1)), 0, false));
						e.setDamage(e.getDamage() /2);
					}
				}
			}
		}else if(  e.getEntity() instanceof Player 
				&& (e.getCause().equals(DamageCause.ENTITY_ATTACK) || e.getCause().equals(DamageCause.PROJECTILE)) 
				&& !PlayerStats.getPlayerStats((Player)e.getEntity()).isInMission){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e){
		if(e.getItem() == null){
			return;
		}if(e.getItem().equals(Refrence.slot7)){
			new MainMenuGUI().open(e.getPlayer());
		}else if(e.getItem().equals(Refrence.slot8S) || e.getItem().equals(Refrence.slot8i)){
			new MissionGUI(e.getPlayer()).open(e.getPlayer());
		}
	}
	
	private void updatePlayerUUID(Player pl){
		Bukkit.getScheduler().runTaskLater(Refrence.main, new Runnable(){
			Player pl;
			public Runnable setPlayer(Player newPL){
				pl = newPL;
				return this;
			}
			@Override
			public void run(){
				PlayerUUIDLookup.addPlayer(new PlayerUUID(pl));
				PlayerUUIDLookup.quickSave();
			}
		}.setPlayer(pl), 10);
	}
	
	public static void cleanupPlayerInventory(Player pl){
		PlayerInventory inv = pl.getInventory();
		inv.clear();
		inv.setItem(0, Refrence.slot0);
		inv.setItem(1, Refrence.slot1);
		inv.setItem(2, Refrence.slot2);
		inv.setItem(3, Refrence.slot3);
		inv.setItem(4, Refrence.slot4);
		inv.setItem(5, Refrence.slot5);
		inv.setItem(6, Refrence.slot6);
		inv.setItem(7, Refrence.slot7);
		inv.setItem(8, Refrence.slot8);
		inv.setItem(9, Refrence.customIS(Material.ARROW, 16, "IBullets", null, null));
	}
}
