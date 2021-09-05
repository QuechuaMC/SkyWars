package fr.kinjer.skywars.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kinjer.skywars.SkyWars;
import fr.kinjer.skywars.state.SkyState;
import fr.kinjer.skywars.task.StartingTimer;

public class SkyWarsListener implements Listener {
	
	private SkyWars main;
	private List<Location> locs = new ArrayList<>();
	
	public SkyWarsListener(SkyWars main) {
		this.locs.clear();
		this.main = main;
		this.locs.addAll(main.locs);
		
	}
	
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent e) {
		if(e.getPlayer().getWorld() != main.world)return;
		Player p = e.getPlayer();
		main.clearInventory(p);
		if(main.state == SkyState.PLAYING || main.state == SkyState.STARTING) {
			main.death(p);
			p.sendTitle("§cThe game is started !", "You are spectator");
			return;
		}
		if(!main.pls.contains(p) && main.state == SkyState.WAITING) {
			main.pls.add(p);
			tpPlayer(p);
		}
		if(main.pls.size() == main.playersToStart) {
			StartingTimer game = new StartingTimer(main);
			game.runTaskTimer(main, 0, 20);
		}
	}
	
	@EventHandler
	private void onQuit(PlayerQuitEvent e) {
		if(e.getPlayer().getWorld() != main.world)return;
		Player p = e.getPlayer();
		if(main.pls.contains(p) && (main.state == SkyState.PLAYING || main.state == SkyState.STARTING))
			main.pls.remove(p);
	}

	@EventHandler
	private void onDeath(EntityDamageEvent e) {
		if(e.getEntity().getWorld() != main.world)return;
        Entity player = e.getEntity();
        System.out.println(player.getName() + " a pris des dégats ");
        System.out.println("is playing ?"+(main.state == SkyState.PLAYING));
        if(main.state == SkyState.PLAYING)
        if (player instanceof Player) {
            Player p = (Player) player;
            double damage = e.getDamage();
            double pHealth = p.getHealth();
            if (pHealth - damage <= 0) {
            	e.setCancelled(true);
                main.death(p);
                if(main.pls.contains(p)) {
                	main.pls.remove(p);
                }
                p.sendTitle("§cYou dead !", "You are spectator");
                //-3 -6 1 chest
                System.out.println("Players : " + main.pls.size());
                winPlayer();
            }
            
            
        }
    }
	
	private void finish(Player p) {
		ItemStack replay = new ItemStack(Material.IRON_SWORD);
		ItemMeta replayMeta = replay.getItemMeta();
		
		replayMeta.setDisplayName("Replay");
		replay.setItemMeta(replayMeta);
		
		ItemStack leave = new ItemStack(Material.WOODEN_DOOR);
		ItemMeta leaveMeta = leave.getItemMeta();
		
		leaveMeta.setDisplayName("Leave");
		leave.setItemMeta(leaveMeta);
		
		p.getInventory().setItem(0, replay);
		p.getInventory().setItem(8, leave);
		
		
	}
	
	@EventHandler
	private void onBreak(BlockBreakEvent e){
		if(e.getPlayer().getWorld() != main.world)return;
		if(main.state == SkyState.WAITING || main.state == SkyState.STARTING)
			e.setCancelled(true);
		if(e.getBlock().getType() == Material.CHEST && e.getPlayer().getInventory().getItemInHand().getType() != Material.NETHER_STAR)
			e.setCancelled(true);
	}
	
	@EventHandler
	private void inVoid(PlayerMoveEvent e) {
		if(e.getPlayer().getWorld() != main.world)return;
		Player p = e.getPlayer();
		Location loc = p.getLocation();
		if(loc.getBlockY() < 100) {
			if(main.pls.contains(p)) {
				main.pls.remove(p);
			}
			main.death(p);
			p.sendTitle("§cYou dead !", "You are spectator");
			winPlayer();
		}
		
		
	}
	
	public void winPlayer() {
		if(main.state != SkyState.FINISH)
		if(main.pls.size() <= 1) {
         	main.state = SkyState.FINISH;
         	for(Player pl : main.pls) {
         		if(main.pls.contains(pl)) {
         			pl.sendTitle("You win !", null);
         			spawnFireworks(pl.getLocation(), 3);
         			Bukkit.broadcastMessage(pl.getName() + " a gagné !");
         		}
//         		else
//         			pl.sendTitle("You lose", null);
         	}
         }
	}
	
	 public static void spawnFireworks(Location location, int amount) {
		 Location loc = location;
		 Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		 FireworkMeta fwm = fw.getFireworkMeta();

		 fwm.setPower(1);
		 fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

		 fw.setFireworkMeta(fwm);
		 fw.detonate();

		 for(int i = 0;i<amount; i++){
			 Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
			 fw2.setFireworkMeta(fwm);
		 }
	 }

	public void tpPlayer(Player p) {
		World world = p.getWorld();
		p.setGameMode(GameMode.SURVIVAL);
		p.setHealth(20);
		p.setFoodLevel(20);
		Random rand = new Random();
		int r = rand.nextInt(locs.size());
		int posX = locs.get(r).getBlockX()-1;
		int posY = locs.get(r).getBlockY()-1;
		int posZ = locs.get(r).getBlockZ()-1;
		for(int x = 0;x < 3; ++x) {
			for(int y = 0 ; y < 4 ; ++y)
				for(int z = 0 ; z < 3 ; ++z) {
					if(!(x == 1 && z == 1 && y == 1 || x == 1 && z == 1 && y == 2))
						world.getBlockAt(new Location(world, posX+x, posY+y, posZ+z)).setType(Material.GLASS);
				}
			
		}
		p.teleport(locs.get(r));
		locs.remove(r);
	}
	
	@EventHandler
	public void onMoveItem(InventoryInteractEvent e) {
		if(main.state == SkyState.FINISH || !main.pls.contains(e.getWhoClicked()))
			e.setCancelled(true);
		
	}
	
	
	
	

}
