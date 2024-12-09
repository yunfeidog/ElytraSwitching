package com.yunfei.elytraswitching.client;

import net.fabricmc.api.ClientModInitializer;

public class ElytraswitchingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // 客户端特有的初始化代码
        System.out.println("System.out.printlnElytraswitching client initialized         ");
    }
}
