package me.tr.roulette.other;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public enum RColor {

	RED(DyeColor.RED, ChatColor.RED), GREEN(DyeColor.GREEN, ChatColor.GREEN), BLACK(DyeColor.BLACK, ChatColor.DARK_GRAY), NULL(DyeColor.GRAY, ChatColor.GRAY);
	
	DyeColor color;
	ChatColor chatColor;
	
	RColor(DyeColor color, ChatColor chatColor){
		this.color = color;
		this.chatColor = chatColor;
	}
	
	public DyeColor getColor(){
		return color;
	}
	
	public ChatColor getChatColor(){
		return chatColor;
	}
	
}
