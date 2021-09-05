package fr.kinjer.skywars.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.kinjer.skywars.SkyWars;

public class ScoreboardListener implements Listener {
	
	public ScoreboardListener(SkyWars main) {
		
	}
	
	@EventHandler
	private void onJoin(PlayerJoinEvent e) {
		scoreboard("En attente de joueurs");
		
	}
	
	public static void scoreboard(String timer) {
		final ScoreboardManager sm = Bukkit.getScoreboardManager();
		final Scoreboard s = sm.getNewScoreboard();
		final Objective o = s.registerNewObjective("skywars", "dummy");
		
		o.setDisplayName("§l§6SkyWars");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		int po = Bukkit.getOnlinePlayers().size();
		int pm = Bukkit.getMaxPlayers();
		final Score empty1 =    o.getScore("§r            ");
		final Score players =   o.getScore("Joueurs : " + po + "/" + pm);
		final Score empty3 =    o.getScore(" ");
		final Score start =    o.getScore(timer);
		final Score empty2 =    o.getScore("");
		final Score site = o.getScore(ChatColor.GOLD+"andelia-mc.net");
		empty1.setScore(5);
		players.setScore(4);
		empty3.setScore(3);
		start.setScore(2);
		empty2.setScore(1);
		site.setScore(0);
		
		for(Player pl : Bukkit.getOnlinePlayers()) {
			pl.setScoreboard(s);
		}
	}

}
