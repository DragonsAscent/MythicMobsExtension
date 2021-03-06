package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;

public class TargetMotionTargeter 
extends 
ILocationSelector {
	String selector;
	int length;
	double dyo;
	boolean iy;

	public TargetMotionTargeter(MythicLineConfig mlc) {
		selector=mlc.getLine().toLowerCase().split("motion")[0];
		length=mlc.getInteger("length",10);
		dyo=mlc.getDouble("yoffset",0d);
		iy=mlc.getBoolean("ignorey",true);
		
	}

	@Override
	public HashSet<AbstractLocation> getLocations(SkillMetadata data) {
		Entity ee=null;
		HashSet<AbstractLocation>targets=new HashSet<>();
		switch(selector) {
		case "target":
			ee=data.getEntityTargets().size()>0?data.getEntityTargets().iterator().next().getBukkitEntity()
					:data.getCaster().getEntity().getTarget()!=null?data.getCaster().getEntity().getTarget().getBukkitEntity():null;
			break;
		case "trigger":
			if (data.getTrigger()!=null) ee=data.getTrigger().getBukkitEntity();
			break;
		case "owner":
			ActiveMob am;
			if ((am=(ActiveMob)data.getCaster())!=null&&am.getOwner().isPresent()) {
				ee=NMSUtils.getEntity(data.getCaster().getEntity().getBukkitEntity().getWorld(),am.getOwner().get());
			}
			break;
		default:
			ee=data.getCaster().getEntity().getBukkitEntity();
			break;
		}
		if (ee!=null) {
			Location s=ee.getLocation().clone(),t;
			s.setPitch(0f);
			s.setYaw(0f);
			t=s.clone();
			Vec3D v3=ee.getType()==EntityType.PLAYER
					?Utils.pl.getOrDefault(ee.getUniqueId(),new Vec3D(0d,0d,0d))
					:NMSUtils.getEntityLastMot(ee);
			if (iy) v3.setY(0d);
			t.subtract(v3.getX()*length,v3.getY()*(length/2),v3.getZ()*length).add(0d,dyo,0d);
			targets.add(BukkitAdapter.adapt(t));
		}
		return targets;
	}
}
