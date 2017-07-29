/**
 * Copyright 2013-present febit.org (support@febit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.febit.wit.toy.console.util;

import java.util.List;

/**
 *
 * @author zqq90
 */
public class StringUtil {

    /**
     * 判定该字符串是否只有指定字符组成.
     *
     * @param src
     * @param c
     * @return
     */
    public static boolean isRepeatOfChar(String src, char c) {
        if (src == null
                || src.isEmpty()) {
            return false;
        }
        final int len = src.length();
        for (int i = 0; i < len; i++) {
            if (src.charAt(i) != c) {
                return false;
            }
        }
        return true;
    }

    public static String join(List<String> list, String separator) {
        int size = list.size();
        if (size == 0) {
            return "";
        }
        if (size == 1) {
            return list.get(0);
        }
        int totalLength = size * separator.length();
        for (String string : list) {
            totalLength += string.length();
        }
        StringBuilder sb = new StringBuilder(totalLength);

        boolean first = true;
        for (String string : list) {
            if (first) {
                first = false;
            } else {
                sb.append(separator);
            }
            sb.append(string);
        }
        return sb.toString();
    }
    
    
    /**
     * 是否是 半角字符
     *
     * @param c
     * @return
     */
    public static boolean isDbcCase(char c) {
        if (c <= 127) {
            return true;
        }
        // 日文半角片假名和符号
        if (c >= 65377 && c <= 65439) {
            return true;
        }
        return false;
    }

    /**
     * 按 1全角=2半角 计算长度
     *
     * @param value
     * @return
     */
    public static int getDbcLength(String value) {
        return getDbcLength(value, 0, value.length());
    }
    
    public static int getDbcLength(String value, int from, int to) {
        to = Math.min(to, value.length());
        int result = to - from;
        for (int i = from; i < to; i++) {
            if (!isDbcCase(value.charAt(i))) {
                result++;
            }
        }
        return result;
    }
}
