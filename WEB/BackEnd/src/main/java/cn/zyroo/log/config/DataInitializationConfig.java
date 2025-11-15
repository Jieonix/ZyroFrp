package cn.zyroo.all.log.config;

import cn.zyroo.all.log.model.LogCleanupConfig;
import cn.zyroo.all.log.repository.LogCleanupConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 数据初始化配置类
 * 在应用启动时初始化必要的配置数据
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Component
public class DataInitializationConfig implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializationConfig.class);

    @Autowired
    private LogCleanupConfigRepository logCleanupConfigRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始初始化日志系统配置数据...");

        // 初始化日志清理配置
        initLogCleanupConfig();

        log.info("日志系统配置数据初始化完成");
    }

    /**
     * 初始化日志清理配置
     */
    private void initLogCleanupConfig() {
        try {
            // 检查是否已存在配置
            List<LogCleanupConfig> existingConfigs = logCleanupConfigRepository.findAll();
            if (!existingConfigs.isEmpty()) {
                log.info("日志清理配置已存在，跳过初始化");
                return;
            }

            // 创建默认的日志清理配置
            List<LogCleanupConfig> defaultConfigs = Arrays.asList(
                    new LogCleanupConfig("LOGIN", 18, "登录日志保留18个月"),
                    new LogCleanupConfig("OPERATION", 36, "操作日志保留3年"),
                    new LogCleanupConfig("ERROR", 9, "错误日志保留9个月"),
                    new LogCleanupConfig("SECURITY", 60, "安全日志保留5年")
            );

            // 设置清理策略
            defaultConfigs.forEach(config -> {
                config.setCleanupStrategy(LogCleanupConfig.CleanupStrategy.DELETE.name());
                config.setEnabled(true);
            });

            // 保存配置
            logCleanupConfigRepository.saveAll(defaultConfigs);

            log.info("成功初始化{}条日志清理配置", defaultConfigs.size());

        } catch (Exception e) {
            log.error("初始化日志清理配置失败", e);
        }
    }
}