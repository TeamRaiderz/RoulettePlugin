package me.tr.roulette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.tr.roulette.other.Crate;
import me.tr.roulette.other.Prize;
import me.tr.roulette.other.RColor;

public class Roulette implements Listener {
	
	public static HashMap<Player, Integer> roll = new HashMap();

	private int winNumber = 0;
	
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
				inv.setItem(i, Util.makeItem(Material.STAINED_GLASS_PANE, 1, color, " "));
				inv.setItem(i + 18, Util.makeItem(Material.STAINED_GLASS_PANE, 1, color, " "));
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
		
		int[] glasspanes = new int[] { 0, 18, 1, 19, 2, 20, 3, 21, 5, 23, 6, 24, 7, 25, 8, 26 };
		
		for(int i : glasspanes){
			inv.setItem(i, Util.makeItem(Material.STAINED_GLASS_PANE, 1, 0, " "));
		}
		
		inv.setItem(4, Util.makeItem(Material.STAINED_GLASS, 1, 15, " "));
		inv.setItem(22, Util.makeItem(Material.STAINED_GLASS, 1, 15, " "));
	}

	public void openCSGO(Player player) {
		Crate c = new Crate();
		Inventory inv = Bukkit.createInventory(null, 27, "ROULETTE");
		setGlass(inv);
		for (int i = 9; (i > 8) && (i < 18); i++) {
			inv.setItem(i, c.pickPrize(player).getDisplayItem());
		}
		player.openInventory(inv);
		startCSGO(player, inv);
	}

	private void startCSGO(final Player player, Inventory inv) {

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
								RColor winColor = getWinningColor();
								if (inv.getItem(13).getData().getData() == winColor.getColor().getWoolData()) {
									rewardWinners();
									endRound();
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
		Crate c = new Crate();
		ArrayList items = new ArrayList();
		for (int i = 9; (i > 8) && (i < 17); i++) {
			items.add(inv.getItem(i));
		}
		inv.setItem(9, c.pickPrize(player).getDisplayItem());
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

		String g = "Green";
		String b = "Black";
		String r = "Red";
		
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
		System.out.println("Encoded: " + encoded);
		double winNumFloat = encoded * 15;
		int winNum = (int) Math.round(encoded * 15);
		System.out.println("Win Number: " + winNum + " (" + winNumFloat + ")");
		return winNum;
	}
	
	public void rewardWinners(){
		
	}
	
	public void endRound(){
		
	}
	
}
