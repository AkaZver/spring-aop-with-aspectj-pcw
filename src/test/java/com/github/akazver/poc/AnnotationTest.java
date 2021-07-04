package com.github.akazver.poc;

import com.github.akazver.poc.annotation.Count;
import com.github.akazver.poc.aop.AnnotationAspect;
import lombok.extern.log4j.Log4j2;
import nl.altindag.log.LogCaptor;
import org.assertj.core.api.StringAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnnotationTest {

    private static final String MESSAGE_REGEX = "Executed in \\d+ns";

    @Test
    public void successfulExecution() {
        try (LogCaptor logCaptor = LogCaptor.forClass(AnnotationAspect.class)) {
            assertDoesNotThrow(TestClass::success);

            assertThat(logCaptor.getInfoLogs(), StringAssert.class)
                    .hasSize(1)
                    .first()
                    .matches(MESSAGE_REGEX);
        }
    }

    @Test
    public void failedExecution() {
        try (LogCaptor logCaptor = LogCaptor.forClass(AnnotationAspect.class)) {
            IllegalStateException exception = assertThrows(IllegalStateException.class, TestClass::fail);

            assertThat(exception).hasMessage("Bye-bye");

            assertThat(logCaptor.getErrorLogs())
                    .hasSize(1)
                    .first()
                    .isEqualTo("Exception occurred");

            assertThat(logCaptor.getInfoLogs(), StringAssert.class)
                    .hasSize(1)
                    .first()
                    .matches(MESSAGE_REGEX);
        }
    }

    @Log4j2
    static class TestClass {
        @Count
        static void success() {
            log.info("Hello from TestClass");
        }

        @Count
        static void fail() {
            throw new IllegalStateException("Bye-bye");
        }
    }

}
