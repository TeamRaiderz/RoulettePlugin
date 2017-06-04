package me.tr.roulette;

import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.tr.roulette.other.RoundState;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin implements Listener{

	private static Main instance;
	private static Economy econ;
	
	public void onEnable(){
		
		System.out.println("############ ROULETTE v. " + getDescription().getVersion() + " ############");
		System.out.println("Author: TeamRaiderz");
		System.out.println("Enabling plugin...");
		
		instance = this;
		
		registerCommands();
		registerListeners();
		setupEconomy();
		
		RoundState.setRoundState(RoundState.WAITING);
		
		System.out.println("Plugin enabled!");
		System.out.println("#############################################################################");
		
	}
	
	public void onDisable(){
		
		System.out.println("############ ROULETTE v. " + getDescription().getVersion() + " ############");
		System.out.println("Author: TeamRaiderz");
		System.out.println("Disabling plugin...");
		
		instance = null;
		
		System.out.println("Plugin disabled!");
		System.out.println("#############################################################################");
		
	}
	
	public static Main getInstance(){
		return instance;
	}
	
	private void registerListeners(){
		
	}
	
	private void registerCommands(){
		
	}

	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
        	System.out.println("This plugin needs the plugin Vault to work!");
            return false;
            
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public static Economy getEconomy(){
		return econ;
	}
	
}
