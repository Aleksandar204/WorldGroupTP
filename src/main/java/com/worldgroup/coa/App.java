package com.worldgroup.coa;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Path;

public class App extends JavaPlugin {

    static public File dat;

    @Override
    public void onEnable() {
        getLogger().info("WorldGroupTP Plugin loaded!");

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        getServer().getPluginManager().registerEvents(new WorldEvent(), this);
        dat = getDataFolder();
    }
    @Override
    public void onDisable() {
        getLogger().info("Copyright Coa 2021");
    }
    
    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
        if (command.getName().equalsIgnoreCase("createwg"))
        {
            //sender.sendMessage(sender.getName() + " ran /create!");
            
            File folder = new File(getDataFolder(), args[0]);
            File wginfo = new File(getDataFolder() + File.separator + args[0] + File.separator + "info.wg");
            folder.mkdir();
            try {
                wginfo.createNewFile();
            } catch (Exception e) {
                //TODO: handle exception
            }
            return true;
        }
        if(command.getName().equalsIgnoreCase("addwg"))
        {
            try {
                FileWriter fw = new FileWriter(getDataFolder() + File.separator + args[0] + File.separator + "info.wg",true);
                fw.write(args[1] + "\n");
                fw.close();
            } catch (Exception e) {
                //TODO: handle exception
            }            
        }
        if(command.getName().equalsIgnoreCase("worldgrouptp"))
        {
            Player p = Bukkit.getPlayer(args[0]);
            String pUUID = p.getUniqueId() + "";
            File playerfile = new File(getDataFolder() + File.separator + args[1] + File.separator + pUUID + ".wgi");
            try {
                if(playerfile.exists())
                {
                    Path filename = Path.of(getDataFolder() + File.separator + args[1] + File.separator + pUUID + ".wgi");
                    String content = Files.readString(filename);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"worldwarp " + args[0] + " " + content);
                    //Bukkit.broadcastMessage("/worldwarp " + args[0] + " " + content);
                }
                else
                {
                    //Bukkit.broadcastMessage("HERE0");
                    Path backupf = Path.of(getDataFolder() + File.separator + args[1] + File.separator + "info.wg");
                    String c = Files.readString(backupf);
                    String arr[] = c.split("\n", 2);
                    //Bukkit.broadcastMessage("/worldwarp " + args[0] + " " + arr[0]);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"worldwarp " + args[0] + " " + arr[0]);
                }
                
            } catch (Exception e) {     
            }
        }
       
        return false;
    }
}