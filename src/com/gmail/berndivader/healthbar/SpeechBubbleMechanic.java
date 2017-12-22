package com.gmail.berndivader.healthbar;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class SpeechBubbleMechanic
extends
SkillMechanic 
implements
ITargetedEntitySkill {
	private String text;
	private int ll,time;
	private float offset;
	private double so,fo;
	private boolean b1;

	public SpeechBubbleMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.text=mlc.getString(new String[] {"text","t"},"");
		if (text.startsWith("\"")
			&&text.endsWith("\"")) {
			this.text=text.substring(1,text.length()-1);
		}
		this.ll=mlc.getInteger(new String[] {"linelength","ll"},20);
		this.offset=mlc.getFloat(new String[] {"offset","yo"},2.1f);
		this.time=mlc.getInteger(new String[] {"time","ti"},20);
		this.so=mlc.getDouble("so",0d);
		this.fo=mlc.getDouble("fo",0d);
		this.b1=mlc.getBoolean("anim",true);  
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!data.getCaster().getEntity().isLiving()) return false;
		if (HealthbarHandler.speechbubbles.containsKey(data.getCaster().getEntity().getUniqueId())) {
			SpeechBubble sb=HealthbarHandler.speechbubbles.get(data.getCaster().getEntity().getUniqueId());
			sb.remove();
		}
		LivingEntity entity=(LivingEntity)data.getCaster().getEntity().getBukkitEntity();
		String txt=this.text;
		txt=SkillString.unparseMessageSpecialChars(txt);
		txt=SkillString.parseMobVariables(txt, data.getCaster(), target, data.getTrigger());
		Location l1=entity.getLocation().clone();
		String[]a1=CustomSkillStuff.wrapStr(txt,ll);
		if (!b1) {
			if (this.so!=0d||this.fo!=0d) {
				l1.add(CustomSkillStuff.getSideOffsetVector(l1.getYaw(),this.so,false));
				l1.add(CustomSkillStuff.getFrontBackOffsetVector(l1.getDirection(),this.fo));
			}
			l1.add(0,(a1.length*0.25)+this.offset,0);
		} else {
			l1.add(0,entity.getEyeHeight(),0);
		}
		new SpeechBubble(entity,l1,this.offset,this.time,a1,this.so,this.fo,b1,this.ll);
		return true;
	}

}
