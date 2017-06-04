package me.tr.roulette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.tr.roulette.other.RColor;
import me.tr.roulette.other.RoundState;

public class Roulette implements Listener, CommandExecutor {
	
	public static HashMap<Player, Integer> roll = new HashMap();

	private int winNumber = 0, betAmount = 20, timeLeftToStart = 20;
	private RColor winColor = RColor.NULL;
	private Inventory rouletteInv;
	private boolean prizeGiven = false;
	
	private void setGlass(Inventory inv) {
		Random r = new Random();
		HashMap Glass = new HashMap();
		for (int i = 0; i < 10; i++) {
			if ((i < 9) && (i != 3)) {
				Glass.put(Integer.valueOf(i), inv.getItem(i));
			}
		}
		for (Iterator localIterator = Glass.keySet().iterator(); localIterator.hasNext();) {
			int i = ((Integer) localIterator.next()).intValue();
			if (inv.getItem(i) == null) {
				int color = r.nextInt(15);
				inv.setItem(i, Util.makeItem(Material.WOOL, 1, color, " "));
				inv.setItem(i + 18, Util.makeItem(Material.WOOL, 1, color, " "));
			}
		}
		for (int i = 1; i < 10; i++) {
			if ((i < 9) && (i != 4)) {
				Glass.put(Integer.valueOf(i), inv.getItem(i));
			}
		}
		int color = r.nextInt(15);
		if (color == 8)
			color = 1;
		
		int[] glasspanes = new int[] { 0, 18, 1, 19, 2, 20, 3, 21, 5, 23, 6, 24, 7, 25, 8, 26, 4 };
		
		for(int i : glasspanes){
			inv.setItem(i, Util.makeItem(Material.STAINED_GLASS_PANE, 1, 0, " "));
		}
		
		inv.setItem(22, Util.makeItem(Material.STAINED_GLASS_PANE, 1, 10, " "));
	}

	public void openCSGO(Player player) {
		
//		if(RoundState.getRoundState() == RoundState.INPROGRESS){
//			player.sendMessage("§cRoulette on jo käynnissä, koita hetken päästä uudelleen, kun tämä kierros on ohi!");
//			return;
//		}
		
		Util.getPlayersWhoBetBlack().add(player.getName());
		
		if(!(Util.getPlayersInRoulette().contains(player))){
			Util.getPlayersInRoulette().add(player);
		}
		
		rouletteInv = Bukkit.createInventory(null, 54, "ROULETTE");
		setGlass(rouletteInv);
		for (int i = 9; (i > 8) && (i < 18); i++) {
			rouletteInv.setItem(i, getRandomItem());
		}
		player.openInventory(rouletteInv);
		if(timeLeftToStart <= 0){
			startCSGO(player, rouletteInv);
		}
		if(timeLeftToStart == 20){
			startRoundCountDown();
		}
	}

	private void startCSGO(final Player player, Inventory inv) {

		winNumber = getWinningNumber();
		winColor = getWinningColor();
		
		RoundState.setRoundState(RoundState.INPROGRESS);
		
		roll.put(player, Integer.valueOf(
				Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
					
					int time = 1;
					int full = 0;
					int open = 0;

					public void run() {
						
						if (this.full <= 50) {
							moveItems(inv, player);
							setGlass(inv);
							player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1, 1);
						}
						this.open++;
						if (this.open >= 5) {
							player.openInventory(inv);
							this.open = 0;
						}
						this.full++;
						if (this.full > 51) {
							if (slowSpin().contains(this.time)) {
								moveItems(inv, player);
								setGlass(inv);
								player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1, 1);
							}
							this.time++;
							if (this.time >= 60) {
								player.playSound(player.getLocation(), Sound.CLICK, 1f, 1f);
								Bukkit.getScheduler().cancelTask(roll.get(player));
								roll.remove(player);
								
								if (inv.getItem(13).getData().getData() == winColor.getColor().getWoolData()) {
									
									System.out.println("Color: " + winColor + " Number: " + winNumber);
									prizeGiven = true;
									rewardWinners();
									RoundState.setRoundState(RoundState.WAITING);
									System.out.println("Starting Roulette again");
									
									if(!(Util.getPlayersInRoulette().contains(player))){
										Util.getPlayersInRoulette().add(player);
									}
									
									timeLeftToStart = 20;
									
									startRoundCountDown();
								}
								return;
							}
						}

					}
				}, 1L, 1L)));
	}

	private ArrayList<Integer> slowSpin() {
		ArrayList slow = new ArrayList();
		int full = 120;
		int cut = 15;
		for (int i = 120; cut > 0; full--) {
			if ((full <= i - cut) || (full >= i - cut)) {
				slow.add(Integer.valueOf(i));
				i -= cut;
				cut--;
			}
		}
		return slow;
	}

	private void moveItems(Inventory inv, Player player) {
		ArrayList items = new ArrayList();
		for (int i = 9; (i > 8) && (i < 17); i++) {
			items.add(inv.getItem(i));
		}
		inv.setItem(9, getRandomItem());
		for (int i = 0; i < 8; i++)
			inv.setItem(i + 10, (ItemStack) items.get(i));
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (roll.containsKey(player)) {
			Bukkit.getScheduler().cancelTask(roll.get(player));
			roll.remove(player);
		}
	}
	
	public RColor getWinningColor() {
		
		if(winNumber > 7 && winNumber != 0){
			winColor = RColor.BLACK;
		}
		else if(winNumber <= 7 && winNumber != 0){
			winColor = RColor.RED;
		}
		else if(winNumber == 0){
			winColor = RColor.GREEN;
		}
		
		return winColor;
	}

	public int getRandomNumber(){
		double encoded = Math.random();
		int winNum = (int) Math.round(encoded * 15);
		return winNum;
	}
	
	public RColor getRandomColor() {
		
		int winNumber = getRandomNumber();
		RColor winColor = RColor.NULL;
		
		if(winNumber > 7 && winNumber != 0){
			winColor = RColor.BLACK;
		}
		else if(winNumber <= 7 && winNumber != 0){
			winColor = RColor.RED;
		}
		else if(winNumber == 0){
			winColor = RColor.GREEN;
		}
		
		return winColor;
	}

	public int getWinningNumber(){
		double encoded = Math.random();
		int winNum = (int) Math.round(encoded * 15);
		return winNum;
	}
	
	
	public ItemStack getRandomItem(){
		RColor winColor = getRandomColor();
		return Util.makeWoolItem(winColor.getColor(), winColor.getChatColor() + winColor.getName());
	}
	
	public ItemStack getWinningItem(){
		return Util.makeWoolItem(winColor.getColor(), winColor.getChatColor() + winColor.getName());
	}
	
	public void rewardWinners(){
		
		if (prizeGiven == true) {

			if (winColor == RColor.BLACK) {
				for (String s : Util.getPlayersWhoBetBlack()) {
					Main.getEconomy().depositPlayer(s, betAmount * 2);

					System.out.println(1);
					
					if (Bukkit.getPlayer(s).isOnline()) {
						Bukkit.getPlayer(s).sendMessage("§7Voitit ruletin, ja sait rahaa §c" + betAmount * 2 + "§7! ("
								+ Main.getEconomy().getBalance(s) + ")");
					}

				}
			}
			if (winColor == RColor.GREEN) {
				for (String s : Util.getPlayersWhoBetGreen()) {
					Main.getEconomy().depositPlayer(s, betAmount * 2);

					System.out.println(2);
					
					if (Bukkit.getPlayer(s).isOnline()) {
						Bukkit.getPlayer(s).sendMessage("§7Voitit ruletin, ja sait rahaa §c" + betAmount * 2 + "§7! ("
								+ Main.getEconomy().getBalance(s) + ")");
					}

				}
			}
			if (winColor == RColor.RED) {
				for (String s : Util.getPlayersWhoBetRed()) {
					Main.getEconomy().depositPlayer(s, betAmount * 2);

					System.out.println(3);
					
					if (Bukkit.getPlayer(s).isOnline()) {
						Bukkit.getPlayer(s).sendMessage("§7Voitit ruletin, ja sait rahaa §c" + betAmount * 2 + "§7! ("
								+ Main.getEconomy().getBalance(s) + ")");
					}

				}
			}
			prizeGiven = false;
		}
	}
	
	private void startRoundCountDown(){
		new BukkitRunnable(){

			@Override
			public void run() {
				
				if(timeLeftToStart >= 1){
					timeLeftToStart -= 1;
					System.out.println("Roulette starts in " + timeLeftToStart);
				}
				else if(timeLeftToStart <= 0){
					for(Player p : Util.getPlayersInRoulette()){
						System.out.println("5");
						startCSGO(p, rouletteInv);
						System.out.println(7);
						
					}
					System.out.println("Time left to start " + timeLeftToStart);
					cancel();
					
				}
				
			}
			
		}.runTaskTimerAsynchronously(Main.getInstance(), 20, 20);
	}
	


	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		Player p = (Player) e.getPlayer();
		if(Util.getPlayersInRoulette().contains(p)){
			Util.getPlayersInRoulette().remove(p);
			System.out.println(6);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) return true;
		
		if(args.length == 0){
			openCSGO((Player) sender);
		}
		else{
			sender.sendMessage("§c/roulette");
		}
		
		return true;
	}
	
}
