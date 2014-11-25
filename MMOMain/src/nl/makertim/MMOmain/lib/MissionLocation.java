package nl.makertim.MMOmain.lib;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class MissionLocation {
	public double radius;
	public Location center;
	
	public MissionLocation(Location center, double radius){
		this.center = center;
		this.radius = radius;
	}
	
	public boolean isInsideRange(Location loc){
		return center.distance(loc) < radius;
	}
	
	public Location getRandomLocation(){
		Random r = MMOOutlaws.random;
		double xOff = (r.nextInt((int) radius) - (radius / 2));
		double zOff = (r.nextInt((int) radius) - (radius / 2));
		Location shouldReturn = new Location(center.getWorld(), center.getX() + xOff, center.getY(), center.getZ() + zOff);
		Block b0 = shouldReturn.getBlock();
		Block b1 = shouldReturn.add(0D, 1D, 0D).getBlock();
		if(b0.isEmpty() && b1.isEmpty()){
			return shouldReturn;
		}else{
			return getRandomLocation();
		}
	}
}
