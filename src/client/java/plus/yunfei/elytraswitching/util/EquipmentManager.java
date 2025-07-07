package plus.yunfei.elytraswitching.util;

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


    /**
     * 执行鞘翅与胸甲槽物品交换
     */
    private static void performElytraSwap(ClientPlayerEntity player, ClientPlayerInteractionManager interactionManager, int elytraIndex) {
        ElytraswitchingClient.logger("开始交换鞘翅，索引: " + elytraIndex);
        try {
            // 第一步：拿起鞘翅
            interactionManager.clickSlot(
                    player.playerScreenHandler.syncId,
                    elytraIndex,
                    0,
                    SlotActionType.PICKUP,
                    player
            );

            // 第二步：点击胸甲槽 - 这会将鞘翅放到胸甲槽，同时拿起胸甲槽原有物品
            interactionManager.clickSlot(
                    player.playerScreenHandler.syncId,
                    CHESTPLATE_SLOT_INDEX,
                    0,
                    SlotActionType.PICKUP,
                    player
            );

            // 第三步：将胸甲槽原有物品放到鞘翅原来的位置
            interactionManager.clickSlot(
                    player.playerScreenHandler.syncId,
                    elytraIndex,
                    0,
                    SlotActionType.PICKUP,
                    player
            );

            ElytraswitchingClient.logger("鞘翅交换成功");

        } catch (Exception e) {
            ElytraswitchingClient.logger("鞘翅交换失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 