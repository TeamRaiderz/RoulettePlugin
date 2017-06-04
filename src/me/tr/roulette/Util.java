package me.tr.roulette;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Util {

	private static ArrayList<String> playersWhoBetRed = new ArrayList<>();
	private static ArrayList<String> playersWhoBetBlack = new ArrayList<>();
	private static ArrayList<String> playersWhoBetGreen = new ArrayList<>();
	private static ArrayList<Player> playersInRoulette = new ArrayList<>();
	
	public static ItemStack makeItem(Material material, int amount, int type, String name) {
		ItemStack item = new ItemStack(material, amount, (short) type);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		item.setItemMeta(m);
		return item;
	}

	public static ItemStack makeItem(Material material, int amount, int type, String name, List<String> lore) {
		ItemStack item = new ItemStack(material, amount, (short) type);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		m.setLore(lore);
		item.setItemMeta(m);
		return item;
	}

	public static void createWoolItem(Inventory inv, int pos, DyeColor color, String displayName, List<String> lore) {
		ItemStack wool = new ItemStack(Material.WOOL, 1, color.getData());
		ItemMeta meta = wool.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		meta.setLore(lore);
		wool.setItemMeta(meta);
		inv.setItem(pos, wool);
	}

	public static ItemStack makeWoolItem(DyeColor color, String displayName) {
		ItemStack wool = new ItemStack(Material.WOOL, 1, color.getData());
		ItemMeta meta = wool.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		wool.setItemMeta(meta);
		return wool;
	}

	public static ItemStack makeWoolItem(DyeColor color, String displayName, List<String> lore) {
		ItemStack wool = new ItemStack(Material.WOOL, 1, color.getData());
		ItemMeta meta = wool.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		meta.setLore(lore);
		wool.setItemMeta(meta);
		return wool;
	}

	
	
	public static ArrayList<String> getPlayersWhoBetRed() {
		return playersWhoBetRed;
	}

	public static ArrayList<String> getPlayersWhoBetBlack() {
		return playersWhoBetBlack;
	}

	public static ArrayList<String> getPlayersWhoBetGreen() {
		return playersWhoBetGreen;
	}

	public static ArrayList<Player> getPlayersInRoulette() {
		return playersInRoulette;
	}
	
}
