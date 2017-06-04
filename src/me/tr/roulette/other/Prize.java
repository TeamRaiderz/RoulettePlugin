package me.tr.roulette.other;

import java.util.Arrays;

import org.bukkit.inventory.ItemStack;

import me.tr.roulette.Util;

public class Prize {

	private String name;
	private int chance;
	private Rarity rarity;
	
	public Prize(String name, int chance, Rarity rarity){
		this.name = name;
		this.chance = chance;
		this.rarity = rarity;
	}

	public String getName() {
		return name;
	}

	public ItemStack getDisplayItem() {
		ItemStack item = Util.makeWoolItem(rarity.color, rarity.chatColor + "§l" + name.toUpperCase(), Arrays.asList(rarity.chatColor + rarity.toString()));
		return item;
	}
	
	public Rarity getRarity(){
		return rarity;
	}

	public int getChance() {
		return chance;
	}
	
	
	
}
