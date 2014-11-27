package nl.makertim.MMOmain;

public class Skill{
	
	public double skillIndex = 0;
	public int skillCosts = 0;
	public String name = "";
	public String[] description = new String[0];
	public static final Skill COMMING_SOON = new Skill(-1, 0.0, Lang.soon + Lang.tm, Lang.tm);
		
	public Skill(int costs, double index, String name, String desc){
		this.skillIndex = index;
		this.skillCosts = costs;
		this.name = name;
		this.description = toDescription(desc);
	}
	
	private static String[] toDescription(String str){
		boolean b = false;
		if(str == null){
			return new String[0];
		}
		for(int i=0; i<str.length(); i++){
			if(i%30==0 && i != 0){
				b = true;
			}
			if(b && str.charAt(i) == " ".charAt(0)){
				str = str.substring(0, i) + str.substring(i, str.length()).replaceFirst(" ", "\r");
				b = false;
			}
		}
		return str.split("\r");
	}
	
	public boolean hasSkill(Skill skill){
		return skillIndex >= skill.skillIndex;
	}
	
	public boolean nextSkill(Skill skill){
		return skillIndex+1 >= skill.skillIndex;
	}
	
	
	public static class Movement extends Skill{
 		public static final Movement NONE 				= new Movement(0, 0.0, "none", null);
 		public static final Movement HAY_NO_DAMMAGE 	= new Movement(1, 1.0, "Leap of faith", "Can fall on top of haybales without getting damage");
 		public static final Movement ZIP_LINE 			= new Movement(2, 2.0, "Zipline", "Can use ziplines with right-click");
 		public static final Movement CROUCH_NO_DAMMAGE 	= new Movement(3, 3.0, "Do a barrel roll", "Crouch while falling to roll and reduce damage");
 		public static final Movement NO_PACKAGE_DELAY 	= new Movement(4, 4.0, "Smooth criminal", "Can carry packages without having to lose speed");
 		public static final Movement DEATH_FROM_ABOVE	= new Movement(5, 5.0, "Death from above", "Jump on top of an enemy with a sword to get in some bonus damage!");
 		public static final Movement HANGGLIDER 		= new Movement(6, 6.0, "Hangglider", "Can use the hangglider");
		
		public Movement(int costs, double index, String name, String desc){
			super(costs, index, name, desc);
		}
		
		public static Movement getEnum(int index){
			switch(index){
			case 1:
				return HAY_NO_DAMMAGE;
			case 2:
				return ZIP_LINE;
			case 3:
				return CROUCH_NO_DAMMAGE;
			case 4:
				return NO_PACKAGE_DELAY;
			case 5:
				return DEATH_FROM_ABOVE;
			case 6:
				return HANGGLIDER;
			default:
				return NONE;
			}
		}
	}
	
	public static class Combat extends Skill{
 		public static final Combat NONE 				= new Combat(0, 0.0, "none", null);
 		public static final Combat ZOOM 				= new Combat(1, 1.0, "No scope", "Crouch with a ranged weapon in your hand to toggle zoom");
 		public static final Combat BETTER_BLOCKING 		= new Combat(2, 2.0, "Blocking", "Block with your sword to reduce incomming damage by X ammount");
 		public static final Combat MORE_AMMO 			= new Combat(3, 3.0, "Big pockets", "Can carry 50% more ammunition for every type of weapon");
 		public static final Combat NO_SLOW_WAPON 		= new Combat(4, 4.0, "Heavy lifter", "Can carry weapons in hand without having to lose speed");
 		public static final Combat RANGED_COMBAT 		= new Combat(5, 5.0, "Sharpshooter", "Melee attacks with ranged weapons are now a bit stronger");
 		public static final Combat SECOND_GEAR 			= new Combat(6, 6.0, "2nd Hand", "Can equip a 2nd ranged weapon");
		
		public Combat(int costs, double index, String name, String desc){
			super(costs, index, name, desc);
		}
		
		public static Combat getEnum(int index){
			switch(index){
			case 1:
				return ZOOM;
			case 2: 
				return BETTER_BLOCKING;
			case 3:
				return MORE_AMMO;
			case 4:
				return NO_SLOW_WAPON;
			case 5:
				return RANGED_COMBAT;
			case 6:
				return SECOND_GEAR;
			default:
				return NONE;
			}
		}
	}
	
	public static class Stealth extends Skill{
 		public static final Stealth NONE 				= new Stealth(0, 0.0, "none", null);
 		public static final Stealth BAT_BOM 			= new Stealth(1, 1.0, "Batbomb", "Use this bomb to disapear in a swarm of bats.");
 		public static final Stealth BACK_SLASH 			= new Stealth(2, 2.0, "Backstab", "Backstabing with melee now does more damage.");
 		public static final Stealth IRON_DOOR 			= new Stealth(3, 3.0, "Lockpicking", "You can now lockpick iron doors.");
 		public static final Stealth NO_POTION 			= new Stealth(4, 4.0, "Poison immunity", "You are not immune for poison effects.");
 		public static final Stealth FLAME_ARROW 		= new Stealth(5, 5.0, "Infinity", "75% to recover an arrow when it hits a target.");
 		public static final Stealth NO_SIGHT 			= new Stealth(6, 6.0, "Ghost", "Sneaking in tall bushes or dark places will now make you invisibile.");
		
		public Stealth(int costs, double index, String name, String desc){
			super(costs, index, name, desc);
		}
		
		public static Stealth getEnum(int index){
			switch(index){
			case 1:
				return BAT_BOM;
			case 2: 
				return BACK_SLASH;
			case 3:
				return IRON_DOOR;
			case 4:
				return NO_POTION;
			case 5:
				return FLAME_ARROW;
			case 6:
				return NO_SIGHT;
			default:
				return NONE;
			}
		}
	}
	
	public static class Utility extends Skill{
 		public static final Utility NONE 				= new Utility(0, 0.0, "none", null);
 		public static final Utility NO_MISTAKES		 	= new Utility(1, 1.0, "Tough guy", "You don't receive damage by your own utility's.");
 		public static final Utility BOOM				= new Utility(2, 2.0, "BOOM", "Your TNT will now break open special walls.");
 		public static final Utility HORSE_EVERYWHERE 	= new Utility(3, 3.0, "Horse with no name", "Allows you to summon your horse anywhere.");
 		public static final Utility THROW_AWAY			= new Utility(4, 4.0, "Double trouble", "Can carry twice as much utility's.");
 		public static final Utility FIRE_BUFF 			= new Utility(5, 5.0, "Flame", "Summon a utility which allows you, and your party members, to gain a temporary flame enchantment.");
 		public static final Utility SECOND_SLOT 		= new Utility(6, 6.0, "Double the fun", "Can equip a 2nd utility.");
		 
		public Utility(int costs, double index, String name, String desc){
			super(costs, index, name, desc);
		}
		
		public static Utility getEnum(int index){
			switch(index){
			case 1:
				return NO_MISTAKES;
			case 2: 
				return BOOM;
			case 3:
				return HORSE_EVERYWHERE;
			case 4:
				return THROW_AWAY;
			case 5:
				return FIRE_BUFF;
			case 6:
				return SECOND_SLOT;
			default:
				return NONE;
			}
		}
	}
}
