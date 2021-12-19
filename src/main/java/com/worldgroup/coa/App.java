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

    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }
    
    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
        if (command.getName().equalsIgnoreCase("createwg"))
        {
            if(args.length != 1)
            {
                sender.sendMessage("Please enter one argument for worldgroup name!");
                return false;
            }
            File folder = new File(getDataFolder(), args[0]);
            File wginfo = new File(getDataFolder() + File.separator + args[0] + File.separator + "info.wg");
            folder.mkdir();
            try {
                wginfo.createNewFile();
            } catch (Exception e) {
                sender.sendMessage("Error when accessing worldgroup files!");
                return false;
            }
            sender.sendMessage("Created new worldgroup " + args[0]);
            return true;
        }
        if(command.getName().equalsIgnoreCase("addworld"))
        {
            if(args.length != 2)
            {
                sender.sendMessage("Please enter worldgroup and world name!");
                return false;
            }
            try {
                FileWriter fw = new FileWriter(getDataFolder() + File.separator + args[0] + File.separator + "info.wg",true);
                fw.write(args[1] + "\n");
                fw.close();
            } catch (Exception e) {
                sender.sendMessage("Error when accessing worldgroup files!");
                return false;
            }    
            sender.sendMessage("Added world to worldgroup " + args[0]);
            return true;        
        }
        if(command.getName().equalsIgnoreCase("listwg"))
        {
            String[] dirs = getDataFolder().list();
            sender.sendMessage(String.join(", ", dirs));

            return true;
        }
        if(command.getName().equalsIgnoreCase("deletewg"))
        {
            if(args.length != 1)
            {
                sender.sendMessage("Please enter worldgroup to delete!");
                return false;
            }
            try {
                File[] contents = new File(getDataFolder() + File.separator + args[0]).listFiles();
                if(contents != null)
                {
                    for(File f: contents){
                        deleteDir(f);
                    }
                }
                new File(getDataFolder() + File.separator + args[0]).delete();
            } catch (Exception e) {
                sender.sendMessage("Error when accessing worldgroup files!");
                return false;
            }
            return true;
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
                    return true;
                }
                else
                {
                    //Bukkit.broadcastMessage("HERE0");
                    Path backupf = Path.of(getDataFolder() + File.separator + args[1] + File.separator + "info.wg");
                    String c = Files.readString(backupf);
                    String arr[] = c.split("\n", 2);
                    //Bukkit.broadcastMessage("/worldwarp " + args[0] + " " + arr[0]);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"worldwarp " + args[0] + " " + arr[0]);
                    return true;
                }
                
            } catch (Exception e) {   
                return false;
            }
        }
       
        return false;
    }
}