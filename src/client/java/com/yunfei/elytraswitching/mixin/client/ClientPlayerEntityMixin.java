package com.yunfei.elytraswitching.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    private static final int CHESTPLATE_INDEX = 6;
    private int lastIndex = -1;
    private long fireworkStartTime = -1;

    @Inject(method = "tickMovement", at = @At(value = "TAIL"))
    private void onPlayerTickMovement(CallbackInfo ci) {
        var player = (ClientPlayerEntity) (Object) this;
        var interactionManager = MinecraftClient.getInstance().interactionManager;
        var currentItem = player.getMainHandStack().getItem();

        // 检测烟花或三叉戟的持有时间
        if (currentItem instanceof FireworkRocketItem || currentItem instanceof TridentItem) {
            if (fireworkStartTime == -1) { // 第一次检测到烟花或三叉戟
                fireworkStartTime = System.currentTimeMillis(); //开始计时
            } else if (System.currentTimeMillis() - fireworkStartTime >= 3000) {
                this.equipElytra(player, interactionManager);
            }
        } else {
            fireworkStartTime = -1;
            if (player.isOnGround() && System.currentTimeMillis() - fireworkStartTime >= 3000) {
                // 检查玩家是否已经穿着胸甲
                Item item = player.getInventory().armor.get(2).getItem();
                if (isChestplate(item)) {
                    return;
                }
                int idx = getChestplateIndex(player);
                if (idx != -1) {
                    if (interactionManager != null) {
                        interactionManager.clickSlot(player.playerScreenHandler.syncId, CHESTPLATE_INDEX, idx, SlotActionType.SWAP, player);
                    }
                    lastIndex = -1;
                }
            }
        }
    }


    /**
     * 装备鞘翅
     */
    private void equipElytra(ClientPlayerEntity player, ClientPlayerInteractionManager interactionManager) {
        int firstElytraIndex = this.getElytraIndex(player);
        if (firstElytraIndex != -1) {
            this.lastIndex = firstElytraIndex;
            interactionManager.clickSlot(player.playerScreenHandler.syncId, CHESTPLATE_INDEX, firstElytraIndex, SlotActionType.SWAP, player);
        }
    }


    /**
     * 获取鞘翅的索引
     *
     * @param player
     * @return
     */
    private int getElytraIndex(ClientPlayerEntity player) {
        var inv = player.getInventory().main;
        for (int i = 0; i < inv.size(); i++) {
            if (inv.get(i).getItem() == Items.ELYTRA) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 判断物品是否是胸甲
     */
    private boolean isChestplate(Item item) {
        return item == Items.DIAMOND_CHESTPLATE ||
                item == Items.IRON_CHESTPLATE ||
                item == Items.GOLDEN_CHESTPLATE ||
                item == Items.CHAINMAIL_CHESTPLATE ||
                item == Items.NETHERITE_CHESTPLATE ||
                item == Items.LEATHER_CHESTPLATE;
    }

    /**
     * 获取胸甲的索引
     */
    private int getChestplateIndex(ClientPlayerEntity player) {
        var inv = player.getInventory().main;
        for (int i = 0; i < inv.size(); i++) {
            Item item = inv.get(i).getItem();
            if (isChestplate(item)) {
                return i;
            }
        }
        return -1;
    }
}