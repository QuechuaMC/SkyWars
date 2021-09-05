package fr.kinjer.skywars;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import fr.kinjer.skywars.command.SkyReloadCommand;
import fr.kinjer.skywars.listener.ScoreboardListener;
import fr.kinjer.skywars.listener.SkyWarsListener;
import fr.kinjer.skywars.schem.Schematic;
import fr.kinjer.skywars.state.SkyState;
import fr.kinjer.skywars.utils.LoadSchematic;

public class SkyWars extends JavaPlugin {
	
	public List<Location> locs = new ArrayList<>();
	public List<Location> chests = new ArrayList<>();
	public List<Location> chestsMiddle = new ArrayList<>();
	public List<Player> pls = new ArrayList<>();
	public List<ItemStack> items = new ArrayList<>();
	public List<ItemStack> itemsMiddle = new ArrayList<>();
	public SkyState state = SkyState.WAITING;
	public World world;
	public int playersToStart;
	
	@Override
	public void onEnable() {
		System.out.println("The plugin SkyWars is enable");
		saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		world = getServer().getWorld(getConfig().getString("name_world"));
		playersToStart = getConfig().getInt("max_players");
		state = SkyState.WAITING;

		initSkyWars();
		
		getServer().getPluginManager().registerEvents(new SkyWarsListener(this), this);
		getServer().getPluginManager().registerEvents(new ScoreboardListener(this), this);
//		getCommand("skyload").setExecutor(new SkyReloadCommand());
	}
	
	@Override
	public void onDisable() {
		System.out.println("The plugin SkyWars is disable");
	}
	
	private void initSkyWars() {
		world.getEntities().stream().filter(Item.class::isInstance).forEach(Entity::remove);
		try {
			Schematic schem = LoadSchematic.loadSchematic(new File(getDataFolder().getAbsolutePath()+"/schems/skywars01.schematic"));
			LoadSchematic.pasteSchematic(world, new Location(world, -41, 127, -43), schem);
			System.out.println(schem);
		} catch (IOException e) {
			e.printStackTrace();
		}
		world.getBlockAt(1, 142, -1).setType(Material.ANVIL);
		locs.clear();
		pls.clear();
		chests.clear();
		chestsMiddle.clear();
		itemsMiddle.clear();
		items.clear();
		locs.add(new Location(world, 1.5, 149, 25.5, 180f, 0f));
		locs.add(new Location(world, 27.5, 149, 0.5, 90f , 0f));
		locs.add(new Location(world, 1.5, 149, -26.5, 0f, 0f));
		locs.add(new Location(world, -24.5, 149, -1.5, -90f, 0f));
		
		chests.add(new Location(world, 1, 144, -30, 0f, 0f));
		chests.add(new Location(world, 2, 139, -29, 0f, 0f));
		
		chests.add(new Location(world, 30, 144, 0, 0f, 0f));
		chests.add(new Location(world, 29, 139, 1, 0f, 0f));
		
		chests.add(new Location(world, 1, 144, 28, 0f, 0f));
		chests.add(new Location(world, 0, 139, 27, 0f, 0f));
		
		chests.add(new Location(world, -28, 144, -2, 0f, 0f));
		chests.add(new Location(world, -27, 139, -3, 0f, 0f));
		
		//Middle
		chestsMiddle.add(new Location(world, -1, 141, -1, 0f, 0f));
		chestsMiddle.add(new Location(world, 3, 141, -1, 0f, 0f));
		chestsMiddle.add(new Location(world, 1, 141, -3, 0f, 0f));
		chestsMiddle.add(new Location(world, 1, 141, 1, 0f, 0f));
		
		items.add(new ItemStack(Material.WOOL, 32));
		items.add(new ItemStack(Material.IRON_SWORD, 1));
		items.add(new ItemStack(Material.DIAMOND_HOE, 1));
		items.add(new ItemStack(Material.EGG, 8));
		items.add(new ItemStack(Material.SNOW_BALL, 16));
		items.add(new ItemStack(Material.ARROW, 8));
		items.add(new ItemStack(Material.BOW, 1));
		items.add(new ItemStack(Material.WOOD_SWORD, 1));
		items.add(new ItemStack(Material.STONE, 32));
		items.add(new ItemStack(Material.IRON_PICKAXE, 1));
		items.add(new ItemStack(Material.IRON_AXE, 1));
		items.add(new ItemStack(Material.WOOD_AXE, 1));
		items.add(new ItemStack(Material.WOOD_PICKAXE, 1));
		items.add(new ItemStack(Material.WOOD_SPADE, 1));
		items.add(new ItemStack(Material.FISHING_ROD, 1));
		items.add(new ItemStack(Material.WOOD, 32));
		items.add(new ItemStack(Material.FLINT_AND_STEEL, 1));
		items.add(new ItemStack(Material.LAVA, 1));
		items.add(new ItemStack(Material.WATER, 1));
		items.add(new ItemStack(Material.EXP_BOTTLE, 8));
		items.add(new ItemStack(Material.COOKED_BEEF, 8));
		items.add(new ItemStack(Material.COMPASS, 1));
		
		items.add(new ItemStack(Material.IRON_HELMET, 1));
		items.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
		items.add(new ItemStack(Material.IRON_LEGGINGS, 1));
		items.add(new ItemStack(Material.IRON_BOOTS, 1));
		
		items.add(new ItemStack(Material.GOLD_HELMET, 1));
		items.add(new ItemStack(Material.GOLD_CHESTPLATE, 1));
		items.add(new ItemStack(Material.GOLD_LEGGINGS, 1));
		items.add(new ItemStack(Material.GOLD_BOOTS, 1));
		
		items.add(new ItemStack(Material.LEATHER_HELMET, 1));
		items.add(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
		items.add(new ItemStack(Material.LEATHER_LEGGINGS, 1));
		items.add(new ItemStack(Material.LEATHER_BOOTS, 1));
		
		//Middle
		itemsMiddle.add(new ItemStack(Material.GOLDEN_APPLE, 2));
		itemsMiddle.add(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
		itemsMiddle.add(new ItemStack(Material.DIAMOND_BOOTS, 1));
		itemsMiddle.add(new ItemStack(Material.ENDER_PEARL, 1));
		itemsMiddle.add(new ItemStack(Material.DIAMOND_SWORD, 1));
		
		Potion potionSpeed = new Potion(PotionType.SPEED, 2);
		ItemStack protItem = new ItemStack(Material.ENCHANTED_BOOK, 1);
		ItemMeta protItemMeta = protItem.getItemMeta();
		protItemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
		protItem.setItemMeta(protItemMeta);
		
		ItemStack powerItem = new ItemStack(Material.ENCHANTED_BOOK, 1);
		ItemMeta powerItemMeta = powerItem.getItemMeta();
		powerItemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
		powerItem.setItemMeta(powerItemMeta);
		
		ItemStack knockbackStick = new ItemStack(Material.STICK, 1);
		ItemMeta knockbackStickMeta = knockbackStick.getItemMeta();
		knockbackStickMeta.addEnchant(Enchantment.KNOCKBACK, 1, false);
		knockbackStick.setItemMeta(knockbackStickMeta);
		
		itemsMiddle.add(new ItemStack(potionSpeed.splash().toItemStack(1)));
		itemsMiddle.add(protItem);
		itemsMiddle.add(powerItem);
		itemsMiddle.add(knockbackStick);
	}
	
	public void start() {
		System.out.println("Nombre de locs : "+locs.size());
		for(Location loc : locs) {
			int posX = loc.getBlockX()-1;
			int posY = loc.getBlockY()-1;
			int posZ = loc.getBlockZ()-1;
			for(int x = 0;x < 3; ++x) {
				for(int y = 0 ; y < 4 ; ++y)
					for(int z = 0 ; z < 3 ; ++z) {
						if(!(x == 1 && z == 1 && y == 1 || x == 1 && z == 1 && y == 2))
							world.getBlockAt(new Location(world, posX+x, posY+y, posZ+z)).setType(Material.AIR);
					}
			}
		}
		for(Location loc : chests) {
			Random r = new Random();
			int maxItems = 5;
			if(loc.getBlock().getType() != Material.CHEST)
				loc.getBlock().setType(Material.CHEST);
			
			Chest chest = (Chest) loc.getBlock().getState();
			Inventory c = chest.getInventory();
			c.clear();
			
			for(int i = 0 ; i < maxItems ; ++i )
				c.setItem(r.nextInt(26), items.get(r.nextInt(items.size())));
		}
		for(Location loc : chestsMiddle) {
			List<ItemStack> allItems = new ArrayList<>();
			allItems.addAll(items);
			allItems.addAll(itemsMiddle);
			Random r = new Random();
			int maxItems = 5;
			if(loc.getBlock().getType() != Material.CHEST)
				loc.getBlock().setType(Material.CHEST);
			
			Chest chest = (Chest) loc.getBlock().getState();
			Inventory c = chest.getInventory();
			c.clear();
			
			for(int i = 0 ; i < maxItems ; ++i) {
				c.setItem(r.nextInt(26), allItems.get(r.nextInt(allItems.size())));
			}
		}
	}
	
	public void death(Player p) {
		Location loc = new Location(world, 1.5D, 150D, -0.5D, 90.0F, 90.0F);
		clearInventory(p);
        p.setGameMode(GameMode.SPECTATOR);
//        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 99));
        p.teleport(loc);
        p.setHealth(20.0D);
        p.setFoodLevel(20);
//        p.setFlying(true);
//        p.setAllowFlight(true);
	}
	
	public void clearInventory(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[] {new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.AIR)});
		
	}
	

}
