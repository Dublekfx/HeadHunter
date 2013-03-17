package com.github.dublekfx.HeadHunter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.meta.SkullMeta;

public final class HeadHunter extends JavaPlugin implements Listener	{
	
	Player pAtt;
	Player pDef; 
	
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
		ItemStack skull = new ItemStack(Material.SKULL_ITEM);
		ItemStack diam = new ItemStack(Material.DIAMOND, 20);
		
		pDef = (Player)event.getEntity();
		pAtt = pDef.getKiller();
		if (pAtt.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_MOBS))	{
			dropHead = ((100 * Math.random() <= getConfig().getDouble(
					"looting." + pAtt.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS))) ? true : false);	
			//Ternary magic, if a random number <= the chance of a head dropping, then true
			}
		if (dropHead)	{
			pAtt.getInventory().addItem(skull);
			SkullMeta skullm = (SkullMeta) skull.getItemMeta();
			skullm.setOwner(pDef.getName());
			skull.setItemMeta(skullm);
		}
		if (pDef.getName().equals("benzrf"))	{
			pAtt.getInventory().addItem(diam);
		}
	}
}
