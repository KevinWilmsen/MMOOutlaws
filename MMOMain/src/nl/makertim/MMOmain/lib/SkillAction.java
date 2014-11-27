package nl.makertim.MMOmain.lib;

import nl.makertim.MMOmain.Skill;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public abstract class SkillAction implements Listener{
	
	public abstract Skill theSkill();
	public abstract Integer slotNumber();
	public abstract ItemStack getItemStack();
	
	public void registerEvents(PluginManager pm, Plugin pl){
		pm.registerEvents(this, pl);
	}
}
