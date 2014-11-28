package nl.makertim.MMOmain;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import nl.makertim.MMOmain.lib.IconMenu;
import nl.makertim.MMOmain.lib.MMOOutlaws;

public class MainMenuGUI extends IconMenu{

	public MainMenuGUI(){
		super(Lang.itemMenu, 1, Refrence.main);
		
		this.addItem(new ItemHandler(0, Lang.descOutlaw, null, new ItemStack(Material.SULPHUR), true){
			@Override
			public void onClick(InventoryClickEvent e, IconMenu ic){
				PlayerStats pls = PlayerStats.getPlayerStats((Player)e.getWhoClicked());
				pls.isOutlaw = true;
				MMOOutlaws.sendActionMessage(pls.getPlayer(), ChatColor.GOLD + Lang.joinOutlaw);
				pls.save();
			}
		});
		this.addItem(new ItemHandler(1, Lang.descSheriff, null, new ItemStack(Material.GLOWSTONE_DUST), true){
			@Override
			public void onClick(InventoryClickEvent e, IconMenu ic){
				PlayerStats pls = PlayerStats.getPlayerStats((Player)e.getWhoClicked());
				pls.isOutlaw = false;
				MMOOutlaws.sendActionMessage(pls.getPlayer(), " " + Lang.joinSheriff);
				pls.save();
			}
		});
		this.addItem(new ItemHandler(8, Lang.skillTree, new ArrayList<String>(), new ItemStack(Material.WRITTEN_BOOK), true){
			@Override
			public void onClick(InventoryClickEvent e, IconMenu ic){
				Bukkit.getScheduler().runTaskLater(Refrence.main, new Runnable(){
					Player pl;
					@Override
					public void run(){
						new SkillTreeGUI(PlayerStats.getPlayerStats(pl));
					}
					public Runnable setPlayer(Player pl){
						this.pl = pl;
						return this;
					}
				}.setPlayer((Player)e.getWhoClicked()), 1L);
			}
		});
	}
}
