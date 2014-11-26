package nl.makertim.MMOmain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.makertim.MMOmain.command.Command;
import nl.makertim.MMOmain.lib.Mission;
import nl.makertim.MMOmain.lib.SkillHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Refrence{
	public static JavaPlugin main;
	public static File mainDir;
	
	public static Random random = new Random(2304);
	public static ArrayList<PlayerUUID> playerList = new ArrayList<PlayerUUID>();
	public static ArrayList<PlayerStats> playerStats = new ArrayList<PlayerStats>();
	private static GameWorld world;
	public static World theWorld;
	public static ArrayList<Class<? extends Mission>> missionTypes = new ArrayList<Class<? extends Mission>>();
	public static ArrayList<SkillHandler> handlers = new ArrayList<SkillHandler>();
	public static ArrayList<Command> commands = new ArrayList<Command>();
	public static GameWorld getWorld(){
		if(world==null){
			world = new GameWorld();
		}
		return world;
	}
	
	@SuppressWarnings("deprecation")
	public static List<Player> getAllPlayers(){
		return toList(Bukkit.getOnlinePlayers());
	}
	
	public static <T> List<T> toList(T[] array){
		List<T> ret = new ArrayList<T>();
		for(T t : array){
			ret.add(t);
		}
		return ret;
	}
	
	public static ItemStack slot0 = customIS(Material.STONE_SWORD, 1, null, null, new Enchantment[]{Enchantment.ARROW_INFINITE, Enchantment.DURABILITY});
	public static ItemStack slot1 = customIS(Material.BOW, 1, null, null, new Enchantment[]{Enchantment.ARROW_INFINITE, Enchantment.DURABILITY});
	public static ItemStack slot2 = customIS(Material.AIR, 1, null, null, null);
	public static ItemStack slot3 = customIS(Material.AIR, 1, null, null, null);
	public static ItemStack slot4 = customIS(Material.AIR, 1, null, null, null);
	public static ItemStack slot5 = customIS(Material.AIR, 1, null, null, null);
	public static ItemStack slot6 = customIS(Material.AIR, 1, null, null, null);
	public static ItemStack slot7 = customIS(Material.NETHER_STAR, 1, "Main Menu", new String[]{"Right Click to open"}, null);
	public static ItemStack slot8 = customIS(Material.COMPASS, 1, ChatColor.DARK_RED + "No missions near", null, null);
	public static ItemStack slot8s = customIS(Material.COMPASS, 1, ChatColor.AQUA + "Mission Menu", null, new Enchantment[]{Enchantment.ARROW_INFINITE});
	public static ItemStack slot8S = customIS(Material.COMPASS, 1, ChatColor.DARK_GREEN + "Mission Menu", null, new Enchantment[]{Enchantment.ARROW_INFINITE});
	public static ItemStack slot8i = customIS(Material.COMPASS, 1, ChatColor.GREEN + "Lobby", null, new Enchantment[]{Enchantment.ARROW_INFINITE});
	
	public static String preFixWARNING = String.format("%s[Warning] %s", ChatColor.RED.toString(), ChatColor.RESET.toString());
	public static String skillTreeMainMenuTitle = String.format("%s%sMain Skill Tree",  ChatColor.DARK_GRAY.toString(), ChatColor.BOLD.toString());
	public static String skillTreeMovementMenuTitle = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Movement Skill Tree";
	public static String skillTreeCombatMenuTitle = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Combat Skill Tree";
	public static String skillTreeStealthMenuTitle = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Stealth Skill Tree";
	public static String skillTreeUtilityMenuTitle = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Utitlty Skill Tree";
	
	public static ItemStack customIS(Material mat, int amount, String name, String[] lore, Enchantment[] enchants){
		ItemStack is = new ItemStack(mat, amount);
		if(enchants != null){
			for(Enchantment enc : enchants){
				is.addUnsafeEnchantment(enc, 1);
			}
		}
		ItemMeta im = is.getItemMeta();
		if(name != null){
			im.setDisplayName(ChatColor.RESET.toString() + ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + name);
		}if(lore != null){
			ArrayList<String> lst = new ArrayList<String>();
			for(String str : lore){
				lst.add(ChatColor.RESET.toString() + ChatColor.YELLOW + str);
			}
			im.setLore(lst);
		}
		is.setItemMeta(im);
		return is;
	}
}
