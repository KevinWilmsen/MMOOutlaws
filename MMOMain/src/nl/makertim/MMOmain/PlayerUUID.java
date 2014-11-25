package nl.makertim.MMOmain;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;

public class PlayerUUID{
	
	public String playerName;
	public UUID playerUUID;
	
	public PlayerUUID(Player pl){
		playerName = pl.getName();
		playerUUID = pl.getUniqueId();
	}
	
	public boolean isPlayer(Player pl){
		return playerName.equalsIgnoreCase(pl.getName());
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof PlayerUUID){
			PlayerUUID plUd = (PlayerUUID)obj;
			return plUd.toString().equalsIgnoreCase(this.toString());
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString(){
		return String.format("PlayerUUID %s - %s", playerName, playerUUID);
	}
	
	public static class PlayerUUIDLookup{		
		public static void addPlayer(PlayerUUID pl){
			if(!Refrence.playerList.contains(pl)){
				Refrence.playerList.add(pl);
			}
		}
		
		public static void save(){
			File uuidDB = new File(Refrence.mainDir.toString() + File.separatorChar + "NameUUID.json");
			uuidDB.delete();
			try{
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(Refrence.playerList);
				Utils.saveToFile(uuidDB, new String[]{json});
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		public static void quickSave(){
			File uuidDB = new File(Refrence.mainDir.toString() + File.separatorChar + "NameUUID.json");
			uuidDB.delete();
			try{
				Gson gson = new GsonBuilder().create();
				String json = gson.toJson(Refrence.playerList);
				Utils.saveToFile(uuidDB, new String[]{json});
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

		public static void load(){
			File uuidDB = new File(Refrence.mainDir.toString() + File.separatorChar + "NameUUID.json");
			try{
				String json = Utils.readFromFile(uuidDB);
				Gson gson = new GsonBuilder().create();
				Refrence.playerList = gson.fromJson(json, new TypeToken<ArrayList<PlayerUUID>>(){}.getType());
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if(Refrence.playerList == null){
				Refrence.playerList = new ArrayList<PlayerUUID>();
			}
		}
	}
}
