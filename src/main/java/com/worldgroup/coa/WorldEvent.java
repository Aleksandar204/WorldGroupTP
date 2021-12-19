// ZA WARUDO
package com.worldgroup.coa;

import org.bukkit.plugin.java.JavaPlugin;

import jdk.jfr.events.FileWriteEvent;

import org.bukkit.Bukkit;
import org.bukkit.World;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileReader;
import java.io.FileWriter;

import java.nio.file.Files;
import java.nio.file.Path;


public class WorldEvent implements Listener 
{
    @EventHandler
    public void ChangedWorld(PlayerChangedWorldEvent event)
    {
        //Bukkit.broadcastMessage("Player " + event.getPlayer() + " is going from world " + event.getFrom() + " to " + event.getPlayer().getWorld());
        File f = App.dat;
        File[] subdirectories = f.listFiles(new FilenameFilter()
            {
                public boolean accept(File d, String name)
                {
                    return d.isDirectory();
                }
            }
        );
        // Bukkit.broadcastMessage("Directories:\n");
        // for (File file : subdirectories) {
        //     Bukkit.broadcastMessage(file.getName());
        // }

        for (File dir : subdirectories) {
            try {
                Path filename = Path.of(dir + File.separator + "info.wg");
                String content = Files.readString(filename);
                if(content.contains(event.getPlayer().getWorld().getName()))
                {
                    //Bukkit.broadcastMessage("World " + event.getPlayer().getWorld().getName() + " is in Group " + dir.getName());
                    File playerfile = new File(App.dat + File.separator + dir.getName() + File.separator + event.getPlayer().getUniqueId() + ".wgi");
                    playerfile.createNewFile();
                    //Bukkit.broadcastMessage("Created file");
                    FileWriter fw = new FileWriter(App.dat + File.separator + dir.getName() + File.separator + event.getPlayer().getUniqueId() + ".wgi");
                    fw.write(event.getPlayer().getWorld().getName());
                    fw.close();
                }
            } catch (Exception e) {
                Bukkit.broadcastMessage("ERROR");
            }
        }
    }
}