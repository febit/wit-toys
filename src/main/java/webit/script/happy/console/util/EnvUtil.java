// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console.util;

/**
 * 获取常用环境变量.
 *
 * @author Zqq
 */
public class EnvUtil {

    public static String get(String key) {
        return System.getProperty(key);
    }

    public static String getUserDir() {
        return get("user.dir");
    }

    public static String getFileEncoding() {
        return get("file.encoding");
    }
}
