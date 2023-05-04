package me.spongycat.autoreplantenchant.customenchant;

import me.spongycat.autoreplantenchant.AutoReplantEnchant;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.Crops;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

import static me.spongycat.autoreplantenchant.AutoReplantEnchant.autoReplantEnchantment;


public class AutoReplantEnchantment extends Enchantment implements Listener {
    public AutoReplantEnchantment(String namespace) {
        super(new NamespacedKey(me.spongycat.autoreplantenchant.AutoReplantEnchant.getPlugin(), namespace));
    }

    private final List<Material> cropList = Arrays.asList(Material.WHEAT, Material.POTATOES, Material.CARROTS, Material.BEETROOTS);
    @EventHandler
    public void breakEvent(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Material cropBlockType;
        if(tool.getEnchantments().containsKey(Enchantment.getByKey(autoReplantEnchantment.getKey()))) {

            if (cropList.contains(block.getType())) {
                cropBlockType = block.getType();

            } else {
                return;
            }


            // Main functionality of the plugin
            if (isFullyGrown(block)) {
                replantCrop(block.getLocation(), cropBlockType);
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                int oldDurability = itemInHand.getDurability();
                itemInHand.setDurability((short) (oldDurability + 1));

            }
        }
    }

    public boolean isFullyGrown(Block block) {
        // Check if it is fully grown
        Ageable ageable = (Ageable) block.getBlockData();
        int maximumAge = ageable.getMaximumAge();

        return ageable.getAge() == maximumAge;
    }
    public void replantCrop(Location location, Material cropBlockType) {
        Bukkit.getScheduler().runTaskLater(AutoReplantEnchant.getPlugin(), () -> {
            location.getBlock().setType(cropBlockType);
        },5L);
    }


    @Override
    public String getName() {
        return "Auto Replant";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return true;
    }
}
