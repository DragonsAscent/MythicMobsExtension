package com.gmail.berndivader.mmcustomskills26;

import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.garbagemule.MobArena.MobArenaHandler;
import com.gmail.berndivader.MythicPlayers.MythicPlayers;
import com.gmail.berndivader.mmcustomskills26.NMS.NMSUtils;
import com.gmail.berndivader.mmcustomskills26.conditions.Factions.FactionsFlags;
import com.gmail.berndivader.mmcustomskills26.conditions.Factions.mmFactionsFlag;
import com.gmail.berndivader.mmcustomskills26.conditions.MobArena.mmMobArenaConditions;
import com.gmail.berndivader.mmcustomskills26.conditions.Own.mmOwnConditions;
import com.gmail.berndivader.mmcustomskills26.conditions.WorldGuard.WorldGuardFlags;
import com.gmail.berndivader.mmcustomskills26.conditions.WorldGuard.mmWorldGuardFlag;
import com.gmail.berndivader.nanpatch.NaNpatch;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import io.lumine.xikage.mythicmobs.MythicMobs;

public class Main extends JavaPlugin {
	
	private BukkitTask taskid;
	private static ThiefHandler thiefhandler = new ThiefHandler();
	private Iterator<Thief> ti;

	private static Main plugin;
	public static MythicMobs mm;
	public static MythicPlayers mp;
	public static MobArenaHandler maHandler;
	public static WorldGuardPlugin wg;
	public static Integer wgVer;
	public static WorldGuardFlags wgf;
	public static FactionsFlags fflags;
	public static String mpNameVar = "mythicprojectile";
	public static String noTargetVar = "nottargetable";
	public static boolean hasRpgItems=false;
	private static NMSUtils nmsutils;
	public static NMSUtils NMSUtils() {return nmsutils;}
	
	@Override
	public void onEnable() {
		plugin = this;
		if (Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null) {
			mm = MythicMobs.inst();
			getServer().getPluginManager().registerEvents(new UndoBlockListener(), this);
			getServer().getPluginManager().registerEvents(new mmCustomSkills26(), this);
			getServer().getPluginManager().registerEvents(new ThiefDamageEvent(), this);
			getServer().getPluginManager().registerEvents(new CustomSkillStuff(), this);
			Bukkit.getLogger().info("Found MythicMobs, registered CustomSkills.");
			new mmOwnConditions();
			if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
				wg = getWorldGuard();
				wgf = new WorldGuardFlags();
				new mmWorldGuardFlag();
			}
			if (Bukkit.getPluginManager().isPluginEnabled("Factions") && Bukkit.getPluginManager().isPluginEnabled("MassiveCore")) {
				fflags = new FactionsFlags();
				new mmFactionsFlag();
			}
			if (Bukkit.getServer().getPluginManager().getPlugin("RPGItems")!=null) {
				Bukkit.getLogger().info("RPGItems support enabled!");
				hasRpgItems=true;
			}
			if (Bukkit.getPluginManager().isPluginEnabled("MobArena")) {
				maHandler = getMobArena();
				new mmMobArenaConditions();
			}
			getNMSUtil();
			mp = new MythicPlayers(this);
			Bukkit.getLogger().info("registered MythicPlayers!");
			new NaNpatch();
			Bukkit.getLogger().info("NaN patch applied!");
			taskid = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
	    		@Override
				public void run() {
	    			ti = thiefhandler.getThiefs().iterator();
	    			while (ti.hasNext()) {
	    				Thief thief = ti.next();if (!mm.getMobManager().isActiveMob(thief.getUuid())) {ti.remove();}}
	    		}}, 1200L, 1200L);
		}
	}
	
	@Override
	public void onDisable() {
    	Bukkit.getServer().getScheduler().cancelTask(taskid.getTaskId());
    	thiefhandler = null;
    	mp = null;
	}
	public static Main getPlugin() {return plugin;}
	public static Set<Thief> getThiefs() {return thiefhandler.getThiefs();}
	public static ThiefHandler thiefhandler() {return thiefhandler;}
	private static WorldGuardPlugin getWorldGuard() {
	    return (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	}
	private static MobArenaHandler getMobArena() {
		return new MobArenaHandler();
	}
	private boolean getNMSUtil() {
		nmsutils=new NMSUtils();
		return nmsutils!=null;
	}
}
