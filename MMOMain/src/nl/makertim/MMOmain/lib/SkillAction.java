package nl.makertim.MMOmain.lib;

import nl.makertim.MMOmain.PlayerStats;
import nl.makertim.MMOmain.Skill;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public abstract class SkillAction implements Listener{
	
	public abstract Skill theSkill();
	public abstract Integer slotNumber();
	public abstract ItemStack getItemStack();
	
	public boolean hasSkill(Player pl){
		return PlayerStats.getPlayerStats(pl).theTree.gotSkill(theSkill());
	}
	
	public void registerEvents(PluginManager pm, Plugin pl){
		pm.registerEvents(this, pl);
	}
}
