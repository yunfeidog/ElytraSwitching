package plus.yunfei.elytraswitching.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;

/**
 * 物品相关工具类
 * 提供物品识别、索引查找等功能
 */
public class ItemHelper {

    /**
     * 判断物品是否为飞行物品（烟花或三叉戟）
     */
    public static boolean isFlightItem(Item item) {
        return item instanceof FireworkRocketItem || item instanceof TridentItem;
    }

    /**
     * 判断物品是否为胸甲
     */
    public static boolean isChestplate(Item item) {
        return item == Items.DIAMOND_CHESTPLATE ||
                item == Items.IRON_CHESTPLATE ||
                item == Items.GOLDEN_CHESTPLATE ||
                item == Items.CHAINMAIL_CHESTPLATE ||
                item == Items.NETHERITE_CHESTPLATE ||
                item == Items.LEATHER_CHESTPLATE;
    }

    /**
     * 检查玩家是否已经穿着胸甲
     */
    public static boolean isWearingChestplate(ClientPlayerEntity player) {
        Item armorItem = player.getEquippedStack(EquipmentSlot.CHEST).getItem();
        return isChestplate(armorItem);
    }

    /**
     * 获取背包中鞘翅的索引（包括快捷栏和主背包）
     */
    public static int getElytraIndex(ClientPlayerEntity player) {
        var inventory = player.getInventory();

        // 搜索完整背包（快捷栏 0-8 + 主背包 9-35）
        for (int i = 0; i < inventory.getMainStacks().size(); i++) {
            if (inventory.getMainStacks().get(i).getItem() == Items.ELYTRA) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取背包中胸甲的索引（包括快捷栏和主背包）
     */
    public static int getChestplateIndex(ClientPlayerEntity player) {
        var inventory = player.getInventory();

        // 搜索完整背包（快捷栏 0-8 + 主背包 9-35）
        for (int i = 0; i < inventory.getMainStacks().size(); i++) {
            Item item = inventory.getMainStacks().get(i).getItem();
            if (isChestplate(item)) {
                return i;
            }
        }
        return -1;
    }
} 