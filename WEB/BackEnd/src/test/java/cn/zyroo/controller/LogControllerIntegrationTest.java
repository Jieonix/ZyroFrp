package cn.zyroo.controller;

import cn.zyroo.log.repository.OperationLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 日志系统集成测试
 * 测试Controller层的自动日志记录功能
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class LogControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    /**
     * 设置MockMvc
     */
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                // .apply(springSecurity())
                .build();
    }

    /**
     * 测试日志查询接口
     */
    @Test
    public void testLogQueryEndpoint() throws Exception {
        setUp();

        // 记录测试前的日志数量
        long initialCount = operationLogRepository.count();

        // 调用日志查询接口（这会触发AOP日志记录）
        mockMvc.perform(get("/api/logs")
                .header("Authorization", "Bearer test-token")
                .param("page", "0")
                .param("size", "10")
                // 
                )
                .andExpect(status().isOk());

        // 验证是否产生了新的日志记录
        long finalCount = operationLogRepository.count();
        assertTrue(finalCount > initialCount, "应该产生新的日志记录");

        System.out.println("✓ 日志查询接口测试通过");
    }

    /**
     * 测试日志统计接口
     */
    @Test
    public void testLogStatisticsEndpoint() throws Exception {
        setUp();

        mockMvc.perform(get("/api/logs/statistics")
                .header("Authorization", "Bearer test-token")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        System.out.println("✓ 日志统计接口测试通过");
    }

    /**
     * 测试权限验证
     */
    @Test
    public void testPermissionValidation() throws Exception {
        setUp();

        // 测试无token访问
        mockMvc.perform(get("/api/logs")
                )
                .andExpect(status().isForbidden());

        // 测试无效token访问
        mockMvc.perform(get("/api/logs")
                .header("Authorization", "Bearer invalid-token")
                )
                .andExpect(status().isForbidden());

        System.out.println("✓ 权限验证测试通过");
    }
}