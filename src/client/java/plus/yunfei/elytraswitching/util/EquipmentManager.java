package plus.yunfei.elytraswitching.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import plus.yunfei.elytraswitching.client.ElytraswitchingClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.screen.slot.SlotActionType;

/**
 * 装备管理工具类
 * 负责处理鞘翅和胸甲的切换操作
 */
public class EquipmentManager {

    /**
     * 胸甲槽索引
     */
    private static final int CHESTPLATE_SLOT_INDEX = 6;

    /**
     * 装备鞘翅
     */
    public static void equipElytra(ClientPlayerEntity player, ClientPlayerInteractionManager interactionManager) {
        int elytraIndex = ItemHelper.getElytraIndex(player);
        ElytraswitchingClient.logger("尝试寻找鞘翅索引: " + elytraIndex);
        if (elytraIndex != -1) {
            performElytraSwap(player, interactionManager, elytraIndex);
            Text message = Text.literal("鞘翅已装备").formatted(Formatting.AQUA);
            player.sendMessage(message, true);
            player.playSound(SoundEvents.AMBIENT_UNDERWATER_ENTER, 0.5f, 1.0f);
        }
    }

    /**
     * 装备胸甲
     */
    public static void equipChestplate(ClientPlayerEntity player, ClientPlayerInteractionManager interactionManager) {
        int chestplateIndex = ItemHelper.getChestplateIndex(player);
        if (chestplateIndex != -1) {
            ElytraswitchingClient.logger("尝试寻找胸甲索引: " + chestplateIndex);
            performElytraSwap(player, interactionManager, chestplateIndex);
            Text message = Text.literal("胸甲已装备").formatted(Formatting.GOLD);
            player.sendMessage(message, true);
            player.playSound(SoundEvents.ITEM_ARMOR_UNEQUIP_WOLF, 0.5f, 1.0f);
        }
    }

    private static void performElytraSwap(ClientPlayerEntity player, ClientPlayerInteractionManager interactionManager, int equipIndex) {
        ElytraswitchingClient.logger("执行装备交换，索引: " + equipIndex);
        try {
            // 背包中的目标物品
            var inventory = player.getInventory();
            var targetStack = inventory.getStack(equipIndex);
            if (targetStack.isEmpty()) {
                ElytraswitchingClient.logger("索引 " + equipIndex + " 没有物品");
                return;
            }
            // 当前胸甲槽物品
            var chestStack = player.getEquippedStack(net.minecraft.entity.EquipmentSlot.CHEST);
            // 胸甲槽有物品 → 放回背包对应格
            if (!chestStack.isEmpty()) {
                inventory.setStack(equipIndex, chestStack.copy());
            } else {
                // 胸甲槽为空 → 清掉原背包物品
                inventory.removeStack(equipIndex);
            }
            // 穿上背包物品（鞘翅或胸甲）
            player.equipStack(net.minecraft.entity.EquipmentSlot.CHEST, targetStack.copy());
            // 标记背包刷新
            inventory.markDirty();
            ElytraswitchingClient.logger("装备切换成功（纯客户端）");
        } catch (Exception e) {
            ElytraswitchingClient.logger("装备切换失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}