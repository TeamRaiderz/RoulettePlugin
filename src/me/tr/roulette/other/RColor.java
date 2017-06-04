package me.tr.roulette.other;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public enum RColor {

	RED(DyeColor.RED, ChatColor.RED, "RED"), GREEN(DyeColor.GREEN, ChatColor.GREEN, "GREEN"), BLACK(DyeColor.BLACK, ChatColor.DARK_GRAY, "BLACK"), NULL(DyeColor.GRAY, ChatColor.GRAY, "ERROR");
	
	DyeColor color;
	ChatColor chatColor;
	String name;
	
	RColor(DyeColor color, ChatColor chatColor, String name){
		this.color = color;
		this.chatColor = chatColor;
		this.name = name;
	}
	
	public DyeColor getColor(){
		return color;
	}
	
	public ChatColor getChatColor(){
		return chatColor;
	}
	
	public String getName(){
		return name;
	}
	
}
