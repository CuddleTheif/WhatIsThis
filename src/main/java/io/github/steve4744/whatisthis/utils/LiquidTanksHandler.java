package io.github.steve4744.whatisthis.utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class LiquidTanksHandler {

    public static String getTankDisplayName(Location loc) {

        String file =Bukkit.getPluginManager().getPlugin("LiquidTanks").getDataFolder().getPath()+"\\tanks.yml";
        YamlConfiguration tanks = YamlConfiguration.loadConfiguration(new File(file));
        String tankLoc = "tanks."+loc.getBlockX()+"_"+loc.getBlockY()+"_"+loc.getBlockZ()+"_"+loc.getWorld().getName();

        if(!tanks.contains(tankLoc))
            return null;
        else{
            String tankType = tanks.getString(tankLoc+".tankType").trim();
            return tankType.substring(0, 1).toUpperCase()+tankType.substring(1)+" Tank";
        }

    }
    
}
