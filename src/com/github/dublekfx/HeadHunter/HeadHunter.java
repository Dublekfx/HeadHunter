package com.github.dublekfx.HeadHunter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.material.Skull;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public final class HeadHunter extends JavaPlugin implements Listener	{
	
	
	Player pAtt;
	Player pDef;
	
	static int looting0Chance;	//2.5% chance of rare drops in Vanilla
	static int looting1Chance;	//3.0%
	static int looting2Chance;	//3.5%
	static int looting3Chance;	//4.0%
	
	
	
	@Override
	public void onEnable()	{
		this.saveDefaultConfig();
		getLogger().info("HeadHunter enabled!");		
	}
	
	@Override
	public void onDisable()	{
		getLogger().info("HeadHunter disabled!");
	}
	
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("hh"))	{
			if ( (sender instanceof Player && sender.isOp()) || !(sender instanceof Player) )	{
				if (args.length == 1)	{
					if (args[0].equalsIgnoreCase("reload"))	{
						reloadConfig();
						saveDefaultConfig();
						sender.sendMessage(ChatColor.DARK_GREEN +"Config reloaded!");
						return true;
					}
				}
				if (args.length == 3)	{
					if (args[0].equalsIgnoreCase("looting"))	{
						double newChance = Double.parseDouble(args[2]);
						this.getConfig().set(args[0] + "." + args[1], newChance);
						sender.sendMessage(ChatColor.DARK_GREEN + "Looting " + args[1] + "probability set to" + newChance);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void onEntityDeathEvent (EntityDeathEvent event)	{
		boolean dropHead = false;
		
		
		pDef = (Player)event.getEntity();
		pAtt = pDef.getKiller();
		if (pAtt.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_MOBS))	{
			dropHead = ((100 * Math.random() <= getConfig().getDouble("looting." + pAtt.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS))) ? true : false);	//Ternary magic, if a random number <= the chance of a head dropping, then true
			}
		if (dropHead)	{
			pAtt.getInventory().addItem();
		}
		}
	}
