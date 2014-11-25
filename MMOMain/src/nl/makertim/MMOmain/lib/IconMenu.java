package nl.makertim.MMOmain.lib;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class IconMenu implements Listener{
	private Plugin plugin;
	protected String GUIname;
	protected int size;
	protected Inventory inv;
	public ArrayList<ItemHandler> items;
	public ArrayList<ItemOption> fakeItems;
	
	public IconMenu(String name, int rows, Plugin plugin){
		this.GUIname = name;
		this.size = rows*9;
		items = new ArrayList<ItemHandler>();
		fakeItems = new ArrayList<ItemOption>();
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void refresh(Player pl){
		pl.closeInventory();
		open(pl);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);	
	}
	
	public IconMenu addItem(ItemHandler item){
		items.add(item);
		return this;
	}
	
	public IconMenu addOption(ItemOption item){
		fakeItems.add(item);
		return this;
	}
	
	public void open(Player pl){
		inv = Bukkit.createInventory(pl, size, GUIname);
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
	
	public void destroy(){
		HandlerList.unregisterAll(this);
		items = null;
		fakeItems = null;
		inv = null;
	}
	
	@EventHandler()
	public void onIneventoryClick(InventoryClickEvent e){
		if(e.getInventory().equals(inv)){
			for(ItemHandler selectedItem : items){
				if(selectedItem.getPosition() == e.getSlot()){
					selectedItem.onClick(e, this);
					if(selectedItem.willClose()){
						destroy();
						e.getWhoClicked().closeInventory();
					}
				}
			}
			e.setResult(Result.DENY);
		}
	}
	
	@EventHandler()
	public void onIneventoryClose(InventoryCloseEvent e){
		if(e.getInventory().equals(inv)){
			destroy();
		}
	}
	
	public static class ItemOption{
		public String optionName;
		public ItemStack theItem;
		public int slotNumber;
		
		public ItemOption(int slot, String itemName, List<String> lore, ItemStack newItemStack){
			newItemStack.setAmount(1);
			ItemMeta im = newItemStack.getItemMeta();
			im.setDisplayName(ChatColor.RESET.toString() + ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + itemName);
			if(lore != null){
				im.setLore(lore);
			}
			newItemStack.setItemMeta(im);
			
			this.optionName = itemName;
			this.theItem = newItemStack;
			this.slotNumber = slot;
		}
		
		public int getPosition(){
			return slotNumber;
		}
	}
	
	public static abstract class ItemHandler extends ItemOption{
		protected boolean shouldClose;
		
		public ItemHandler(int slot, String itemName, List<String> lore, ItemStack newItemStack, boolean shouldClose){
			super(slot, itemName, lore, newItemStack);
			this.shouldClose = shouldClose;
		}
		
		public abstract void onClick(InventoryClickEvent e, IconMenu iconMenu);
		
		public boolean willClose(){
			return shouldClose;
		}
	}
}
