package io.github.linyimin0812.profiler.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author linyimin
 **/

public class OSUtilTest {

    @Test
    public void testIsWindows() {
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            assertTrue(OSUtil.isWindows());
        } else {
            assertFalse(OSUtil.isWindows());
        }
    }

    @Test
    public void testIsLinux() {
        if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
            assertTrue(OSUtil.isLinux());
        } else {
            assertFalse(OSUtil.isLinux());
        }
    }

    @Test
    public void testIsMac() {
        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            assertTrue(OSUtil.isMac());
        } else {
            assertFalse(OSUtil.isMac());
        }
    }
}
