package me.spongycat.autoreplantenchant;

import me.spongycat.autoreplantenchant.customenchant.AutoReplantEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public final class AutoReplantEnchant extends JavaPlugin {
    private static me.spongycat.autoreplantenchant.AutoReplantEnchant plugin;
    public static AutoReplantEnchantment autoReplantEnchantment;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        autoReplantEnchantment = new AutoReplantEnchantment("auto_replant");

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        registerEnchantment(autoReplantEnchantment);
        this.getServer().getPluginManager().registerEvents(autoReplantEnchantment, this);

        if (getConfig().getBoolean("Allow_Crafting")) {
            ItemStack hoe = new ItemStack(Material.NETHERITE_HOE, 1);
            hoe.addUnsafeEnchantment(AutoReplantEnchant.autoReplantEnchantment, 1);
            hoe.addEnchantment(Enchantment.DIG_SPEED, 5);
            hoe.addEnchantment(Enchantment.DURABILITY, 3);
            hoe.addEnchantment(Enchantment.MENDING, 1);
            hoe.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
            ItemMeta meta = hoe.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Auto Replant I");
            meta.setLore(lore);
            hoe.setItemMeta(meta);

            ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT, 1);
            carrot.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 10);
            ItemMeta meta2 = carrot.getItemMeta();
            ArrayList<String> lore2 = new ArrayList<>();
            lore2.add(ChatColor.LIGHT_PURPLE + "Special golden carrot with mysterious power...");
            meta2.setLore(lore2);
            meta2.setDisplayName(ChatColor.GOLD + "Super Golden Carrot");
            carrot.setItemMeta(meta2);




            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "Replant_Hoe"), hoe);
            recipe.shape("PXP", "XYX", "AAA");
            recipe.setIngredient('X', carrot.getData());
            recipe.setIngredient('Y', Material.NETHERITE_HOE);
            recipe.setIngredient('A', Material.NETHERITE_INGOT);
            recipe.setIngredient('P', Material.DISPENSER);

            ShapedRecipe recipe2 = new ShapedRecipe(new NamespacedKey(this, "Super_Carrot"), carrot);
            recipe2.shape("SSS", "SSS", "SSS");
            recipe2.setIngredient('S', Material.GOLDEN_CARROT);

            Bukkit.addRecipe(recipe);
            Bukkit.addRecipe(recipe2);
        }
    }

    public static AutoReplantEnchant getPlugin() {
        return plugin;
    }


    @Override
    public void onDisable() {
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");

            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);

            if(byKey.containsKey(autoReplantEnchantment.getKey())) {
                byKey.remove(autoReplantEnchantment.getKey());
            }
            Field nameField = Enchantment.class.getDeclaredField("byName");

            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            if(byName.containsKey(autoReplantEnchantment.getName())) {
                byName.remove(autoReplantEnchantment.getName());
            }
        } catch (Exception ignored) { }
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if(registered){
            // It's been registered!
        }
    }



}
