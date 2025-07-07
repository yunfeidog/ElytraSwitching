package plus.yunfei.elytraswitching.mixin.client;

import plus.yunfei.elytraswitching.util.EquipmentManager;
import plus.yunfei.elytraswitching.util.ItemHelper;
import plus.yunfei.elytraswitching.util.TimerManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 客户端玩家实体混入类
 * 实现鞘翅和胸甲的智能自动切换功能
 */
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    
    /** 烟花/三叉戟持有开始时间 */
    private long fireworkStartTime = -1;
    /** 非烟花/三叉戟持有开始时间 */
    private long chestplateStartTime = -1;

    @Inject(method = "tickMovement", at = @At(value = "TAIL"))
    private void onPlayerTickMovement(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
        
        if (interactionManager == null) return;
        
        Item currentItem = player.getMainHandStack().getItem();

        // 根据当前持有物品类型选择处理逻辑
        if (ItemHelper.isFlightItem(currentItem)) {
            handleFlightItemHeld(player, interactionManager);
        } else {
            handleNonFlightItemHeld(player, interactionManager);
        }
    }

    /**
     * 处理持有飞行物品的情况
     */
    private void handleFlightItemHeld(ClientPlayerEntity player, ClientPlayerInteractionManager interactionManager) {
        if (fireworkStartTime == -1) {
            fireworkStartTime = TimerManager.getCurrentTime();
            chestplateStartTime = TimerManager.resetTime();
        } else if (TimerManager.hasReachedSwitchTime(fireworkStartTime)) {
            EquipmentManager.equipElytra(player, interactionManager);
            resetAllTimers();
        }
    }

    /**
     * 处理未持有飞行物品的情况
     */
    private void handleNonFlightItemHeld(ClientPlayerEntity player, ClientPlayerInteractionManager interactionManager) {
        // 必须在地面上才能切换回胸甲
        if (!player.isOnGround()) return;

        if (chestplateStartTime == -1) {
            chestplateStartTime = TimerManager.getCurrentTime();
            fireworkStartTime = TimerManager.resetTime();
        } else if (TimerManager.hasReachedSwitchTime(chestplateStartTime)) {
            // 达到切换时间且仍在地面上，检查是否需要装备胸甲
            if (player.isOnGround() && !ItemHelper.isWearingChestplate(player)) {
                EquipmentManager.equipChestplate(player, interactionManager);
            }
            resetAllTimers();
        }
    }

    /**
     * 重置所有计时器
     */
    private void resetAllTimers() {
        fireworkStartTime = TimerManager.resetTime();
        chestplateStartTime = TimerManager.resetTime();
    }
}
