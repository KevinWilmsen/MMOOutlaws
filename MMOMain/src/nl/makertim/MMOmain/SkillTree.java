package nl.makertim.MMOmain;

import nl.makertim.MMOmain.Skill.*;

public class SkillTree{
	private int totalLevel = 1;
	private int availableLevels = 1;
	
	public Movement myMovementLevel 	= Movement.none;
	public Combat myComatLevel 			= Combat.none;
	public Stealth myStealthLevel 		= Stealth.none;
	public Utility myUtilityLevel 		= Utility.none;
	
	public void addLevel(){
		if(totalLevel < 50){
			totalLevel++;
			availableLevels++;
		}
	}
	
	public void reset(){
		this.myMovementLevel = Movement.none;
		this.myComatLevel = Combat.none;
		this.myStealthLevel = Stealth.none;
		this.myUtilityLevel = Utility.none;
		this.availableLevels = this.totalLevel;
	}
	
	public void Reset(){
		this.myMovementLevel = Movement.none;
		this.myComatLevel = Combat.none;
		this.myStealthLevel = Stealth.none;
		this.myUtilityLevel = Utility.none;
		this.availableLevels = this.totalLevel;
		totalLevel = 1;
		availableLevels = 1;
	}
	
	public void addLevels(int max){
		for(int i=0; i<max; i++){
			addLevel();
		}
	}
	
	public int getAvalibleLVL(){
		return availableLevels;
	}
	
	public int getTotalLVL(){
		return totalLevel;
	}
	
	/** @return * 0 = ERROR. * 1 = OK. * 2 = Level Incorrect. * 3 = Index Incorrect. */
	public int buyNewSkill(Skill newSkill){
		Skill oldSkill = null;
		if(newSkill instanceof Movement){
			oldSkill = myMovementLevel;
		}else if(newSkill instanceof Combat){
			oldSkill = myComatLevel;
		}else if(newSkill instanceof Stealth){
			oldSkill = myStealthLevel;
		}else if(newSkill instanceof Utility){
			oldSkill = myUtilityLevel;
		}
		if(oldSkill == null){
			return 0;
		}else if(oldSkill.skillIndex + 1 == newSkill.skillIndex){
			if(availableLevels >= newSkill.skillCosts){
				availableLevels -= newSkill.skillCosts;
				if(newSkill instanceof Movement){
					myMovementLevel = (Movement)newSkill;
				}else if(newSkill instanceof Combat){
					myComatLevel = (Combat)newSkill;
				}else if(newSkill instanceof Stealth){
					myStealthLevel = (Stealth)newSkill;
				}else if(newSkill instanceof Utility){
					myUtilityLevel = (Utility)newSkill;
				}else{
					availableLevels += newSkill.skillCosts;
					return 0;
				}
				return 1;
			}else{
				return 2;
			}
		}else{
			return 3;
		}		
	}
}
