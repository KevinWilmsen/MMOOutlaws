package nl.makertim.MMOmain;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nl.makertim.MMOmain.Skill.*;
import nl.makertim.MMOmain.lib.IconMenu;

public class SkillTreeGUI extends IconMenu{
	private PlayerStats pls;
	
	public SkillTreeGUI(PlayerStats pls){
		super("Skill Tree", 5, Refrence.main);		
		this.pls = pls;
		this.open(pls.getPlayer());
	}
	
	@Override
	public void open(Player pl) {
		inv = Bukkit.createInventory(pl, size, GUIname);
		items = new ArrayList<ItemHandler>();
		fakeItems = new ArrayList<ItemOption>();
		instance();
		for(ItemHandler item : items){
			if(item != null){
				inv.setItem(item.slotNumber, item.theItem);
			}
		}
		for(ItemOption item : fakeItems){
			if(item != null){
				inv.setItem(item.slotNumber, item.theItem);
			}
		}
		pl.openInventory(inv);
	}
	
	private void instance(){
		this.addItem(new ItemHandler(0, "Main Menu", null, new ItemStack(Material.NAME_TAG), true){ 
			@Override 
			public void onClick(InventoryClickEvent e, IconMenu ic){ 
				Bukkit.getScheduler().runTaskLater(Refrence.main, new Runnable(){
					private Player pl;
					public Runnable geefMee(Player pl){
						this.pl = pl;
						return this;
					}
					@Override
					public void run(){
						new MainMenuGUI().open(pl);
					}
				}.geefMee((Player)e.getWhoClicked()), 1L);
			} 
		});
		addCurrent();
		addResset();
		this.addOption(new ItemOption(9, "Movement Skills", null, new ItemStack(Material.FEATHER)));
		Movement();
		this.addOption(new ItemOption(18, "Combat Skills", null, new ItemStack(Material.IRON_SWORD)));
		Combat();
		this.addOption(new ItemOption(27, "Stealth Skills", null, new ItemStack(Material.LEATHER_BOOTS)));
		Stealth();
		this.addOption(new ItemOption(36, "Utility Skills", null, new ItemStack(Material.TNT)));
		Utility();
	}
	
	private void Movement(){
		for(int i=0; i<7; i++){
			Movement skill = Movement.getEnum(i);
			if(skill != Movement.none){
				this.addItem(new SkillItemHandler(9+i, skill, pls));
			}else{
				this.addItem(new SkillItemHandler(9+i, Skill.commingSoon, pls));
			}
		}
	}
	
	private void Combat(){
		for(int i=0; i<7; i++){
			Combat skill = Combat.getEnum(i);
			if(skill != Combat.none){
				this.addItem(new SkillItemHandler(18+i, skill, pls));
			}else{
				this.addItem(new SkillItemHandler(18+i, Skill.commingSoon, pls));
			}
		}
	}
	
	private void Stealth(){
		for(int i=0; i<7; i++){
			Stealth skill = Stealth.getEnum(i);
			if(skill != Stealth.none){
				this.addItem(new SkillItemHandler(27+i, skill, pls));
			}else{
				this.addItem(new SkillItemHandler(27+i, Skill.commingSoon, pls));
			}
		}
	}
	
	private void Utility(){
		for(int i=0; i<7; i++){
			Utility skill = Utility.getEnum(i);
			if(skill != Utility.none){
				this.addItem(new SkillItemHandler(36+i, skill, pls));
			}else{
				this.addItem(new SkillItemHandler(36+i, Skill.commingSoon, pls));
			}
		}
	}
	
	private void addCurrent(){
		this.addOption(new ItemOption(
				8,
				"Skill Points", 
				arrayToList(new String[]{
						"", 
						ChatColor.GREEN + "Available Levels : " + pls.theTree.getAvalibleLVL(),
						ChatColor.GOLD + "Total Level : " + pls.theTree.getTotalLVL(),
						ChatColor.YELLOW + "Max Level : 50"}), 
				new ItemStack(Material.EMERALD, pls.theTree.getAvalibleLVL()<0?1:pls.theTree.getAvalibleLVL())
				)
		);
	}
	
	private void addResset(){
		this.addItem(new ItemHandler(
				4,
				"Reset Skills", 
				arrayToList(new String[]{
						"", 
						ChatColor.RED + "You will get your levels back"}), 
				new ItemStack(Material.SULPHUR), 
				false)
		{	
			@Override 
			public void onClick(InventoryClickEvent e, IconMenu ic){
				PlayerStats pls = PlayerStats.getPlayerStats((Player)e.getWhoClicked());
				pls.theTree.reset();
				pls.save();
				Bukkit.getScheduler().scheduleSyncDelayedTask(Refrence.main, new Runnable(){
					private Player pl;
					private PlayerStats pls;
					public Runnable geefMee(Player pl, PlayerStats pls){
						this.pl = pl;
						this.pls = pls;
						return this;
					}
					@Override
					public void run() {
						pl.playSound(pl.getLocation(), Sound.NOTE_PLING, 1F, 2F);
						for(int i=0; i<15; i++){
							pl.getWorld().playEffect(pl.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
						}
						new SkillTreeGUI(pls).open(pl);
					}}.geefMee((Player)e.getWhoClicked(), pls),1L);
				
			} 
		});
	}
	
	public static ArrayList<String> arrayToList(String[] array){
		ArrayList<String> ret = new ArrayList<String>();
		for (String part : array) {
			ret.add(part);
		}
		return ret;
	}
	
	public static class SkillItemHandler extends ItemHandler{
		private PlayerStats thePlayer;
		private Skill theSkill;
		
		public SkillItemHandler(int pos, Skill skill, PlayerStats pls){
			super(
					pos, 
					ChatColor.BOLD.toString() + ChatColor.GOLD + skill.name, 
					getLore(skill, pls), 
					getItemStack(skill, pls), 
					false);
			this.thePlayer = pls;
			this.theSkill = skill;
		}
		
		public static ArrayList<String> getLore(Skill skill, PlayerStats pls){
			ArrayList<String> lst = new ArrayList<String>();
			for(String desc : skill.description){
				lst.add(ChatColor.YELLOW + desc);
			}
			Skill mySkill = null;
			if(skill instanceof Combat){
				mySkill = pls.theTree.myComatLevel;
			}else if(skill instanceof Movement){
				mySkill = pls.theTree.myMovementLevel;
			}else if(skill instanceof Stealth){
				mySkill = pls.theTree.myStealthLevel;
			}else if(skill instanceof Utility){
				mySkill = pls.theTree.myUtilityLevel;
			}else{
				return lst;
			}
			boolean hasSkill = mySkill.hasSkill(skill);
			boolean nextSkill = mySkill.nextSkill(skill);
			if(hasSkill){
				lst.add(0, "");
				lst.add(1, ChatColor.BOLD + "" + ChatColor.GREEN + "Got Skill");
			}else if(nextSkill){
				lst.add(0, "");
				lst.add(1, ChatColor.YELLOW + "Click to unlock");
				lst.add(2, ChatColor.GRAY + "Cost : " + skill.skillCosts + " Skill Point" + (skill.skillCosts>1?"s":""));
			}else{
				lst.add(0, "");
				lst.add(1, ChatColor.DARK_GRAY + "You need to unlock other skills first");
			}
			
			return lst;
		}
		
		public static ItemStack getItemStack(Skill skill, PlayerStats pls){
			ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE);
			
			Skill mySkill = null;
			if(skill instanceof Combat){
				mySkill = pls.theTree.myComatLevel;
			}else if(skill instanceof Movement){
				mySkill = pls.theTree.myMovementLevel;
			}else if(skill instanceof Stealth){
				mySkill = pls.theTree.myStealthLevel;
			}else if(skill instanceof Utility){
				mySkill = pls.theTree.myUtilityLevel;
			}else{
				return is;
			}
			boolean hasSkill = mySkill.hasSkill(skill);
			boolean nextSkill = mySkill.nextSkill(skill);
			String title = hasSkill?"Got it":nextSkill?"Next skill":"Locked";
			short extra = (short)(hasSkill?13:nextSkill?1:7);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(title);
			is.setItemMeta(im);
			is.setDurability(extra);
			return is;
		}
		
		@Override
		public void onClick(InventoryClickEvent e, IconMenu ic){
			int message = thePlayer.theTree.buyNewSkill(theSkill);
			switch(message){
			case 1:
				Player pl = thePlayer.getPlayer();
				pl.sendMessage(String.format("%s Unlocked.", theSkill.name));
				PlayerStats.getPlayerStats(pl).save();
				pl.playSound(pl.getLocation(), Sound.NOTE_PLING, 1F, 2F);
				Bukkit.getScheduler().runTaskLater(Refrence.main, new Runnable(){
					private IconMenu stGUI;
					public Runnable geefMee(IconMenu stGUI){
						this.stGUI = stGUI;
						return this;
					}
					@Override
					public void run(){
						stGUI.refresh(thePlayer.getPlayer());
					}
				}.geefMee(ic), 1L);
				break;
			case 2:
				thePlayer.getPlayer().sendMessage("Your skill level is not high enough for this skill");
				break;
			case 3:
				thePlayer.getPlayer().sendMessage("Already unlocked this skill");
				break;
			default:
				thePlayer.getPlayer().sendMessage("You cant buy this item");
				break;
			}
			
		}
		
	}
}
