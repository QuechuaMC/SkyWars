package fr.kinjer.skywars.task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import fr.kinjer.skywars.SkyWars;
import fr.kinjer.skywars.state.SkyState;

public class StartingTimer extends BukkitRunnable{
	
	private int timer = 10;
	private SkyWars main;

	public StartingTimer(SkyWars main) {
		this.main = main;
	}
	
	@Override
	public void run() {
		if(timer == 10 || timer > 0 && timer <= 5) {
			main.state = SkyState.STARTING;
			Bukkit.broadcastMessage("Le jeu commence dans " + timer + "s !");
		}
		
		if(timer <= 0) {
			Bukkit.broadcastMessage("Le jeu a commencé ! Bon jeu !");
			main.state = SkyState.PLAYING;
			main.start();
			cancel();
		}
		
		
		timer--;
		
	}

}
