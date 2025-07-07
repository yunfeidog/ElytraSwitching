package plus.yunfei.elytraswitching.util;

/**
 * 计时器管理工具类
 * 管理切换延迟计时逻辑
 */
public class TimerManager {

    /** 切换延迟时间（毫秒） */
    public static final long SWITCH_DELAY_MS = 3000L;

    /**
     * 检查是否达到切换时间
     */
    public static boolean hasReachedSwitchTime(long startTime) {
        return startTime != -1 && System.currentTimeMillis() - startTime >= SWITCH_DELAY_MS;
    }

    /**
     * 获取当前时间戳
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 重置时间戳
     */
    public static long resetTime() {
        return -1;
    }
} 