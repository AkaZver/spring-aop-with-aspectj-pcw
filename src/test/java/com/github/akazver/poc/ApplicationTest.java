package com.github.akazver.poc;

import com.github.akazver.poc.aop.ControllerAspect;
import com.github.akazver.poc.aop.ServiceAspect;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void successfulExecution() throws Exception {
        try (LogCaptor controllerLogCaptor = LogCaptor.forClass(ControllerAspect.class);
             LogCaptor serviceLogCaptor = LogCaptor.forClass(ServiceAspect.class)) {

            mockMvc.perform(get("/hello/world"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Hello, world"));

            List<String> controllerLogs = controllerLogCaptor.getInfoLogs();
            assertThat(controllerLogs).hasSize(3);

            assertSoftly(softly -> {
                softly.assertThat(controllerLogs.get(0))
                        .isEqualTo("ControllerAspect executed with argument: world");

                softly.assertThat(controllerLogs.get(1))
                        .isEqualTo("MyController is Spring AOP proxy: true");

                softly.assertThat(controllerLogs.get(2))
                        .startsWith("MyController class name: com.github.akazver.poc.controller.MyController$$EnhancerBySpringCGLIB$$");
            });

            List<String> serviceLogs = serviceLogCaptor.getInfoLogs();

            assertThat(serviceLogs)
                    .hasSize(3)
                    .containsExactly(
                            "ServiceAspect executed with argument: world",
                            "MyService is Spring AOP proxy: false",
                            "MyService class name: com.github.akazver.poc.service.MyService"
                    );
        }
    }

}
