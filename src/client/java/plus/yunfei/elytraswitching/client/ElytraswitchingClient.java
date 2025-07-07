package plus.yunfei.elytraswitching.client;

import net.fabricmc.api.ClientModInitializer;

/**
 * ElytraSwitching 模组客户端初始化类
 */
public class ElytraswitchingClient implements ClientModInitializer {
    final static boolean enableLog = true; // 是否启用日志输出

    @Override
    public void onInitializeClient() {
        // 客户端初始化完成日志
        logger("鞘翅切换Mod客户端初始化完成");
    }

    public static void logger(String message) {
        if (!enableLog) return;
        // 输出日志到控制台
        System.out.println("[鞘翅切换Mod]:" + message);
    }
}
