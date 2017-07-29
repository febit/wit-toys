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
package org.febit.wit.toy.httpd;

import java.util.concurrent.atomic.AtomicLong;
import org.febit.wit.InternalContext;
import org.febit.wit.core.NativeFactory;
import org.febit.wit.global.GlobalManager;
import org.febit.wit.global.GlobalRegister;
import org.febit.wit.lang.MethodDeclare;
import org.febit.wit.toy.httpd.WitHTTPD.WitResponce;
import org.febit.wit.util.JavaNativeUtil;

/**
 * All public static fields and methods will be imported to templates.
 *
 * @author zqq90
 */
public class ContextMethods implements GlobalRegister {

    private static final AtomicLong NEXT_ID = new AtomicLong(1);

    public static final MethodDeclare noop = (InternalContext context, Object[] args) -> null;
    public static final MethodDeclare id = (InternalContext context, Object[] args) -> NEXT_ID.getAndIncrement();
    public static final MethodDeclare now = (InternalContext context, Object[] args) -> System.currentTimeMillis();

    public static void setStatusCode(int code) {
        WitHTTPD.getWitResponce().setStatusCode(code);
    }

    public static void setEncoding(String encoding) {
        WitHTTPD.getWitResponce().setEncoding(encoding);
    }

    public static void setContentType(String mimeType) {
        WitHTTPD.getWitResponce().setMimeType(mimeType);
    }

    public static void setHeader(String name, Object value) {
        WitHTTPD.getWitResponce().setHeader(name, toString(value));
    }

    public static void setCookie(String name, Object value, Integer days) {
        WitResponce responce = WitHTTPD.getWitResponce();
        if (days == null) {
            responce.setCookie(name, toString(value));
        } else {
            responce.setCookie(name, toString(value), days);
        }
    }

    public static void removeCookie(String name) {
        WitHTTPD.getWitResponce().removeCookie(name);
    }

    public static String toString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public static double toDouble(Object value) {
        if (value == null) {
            return 0D;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (Exception e) {
                return 0D;
            }
        }
        return 0D;
    }

    public static int toInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            if (((String) value).isEmpty()) {
                return 0;
            }
            try {
                return (int) Double.parseDouble((String) value);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static boolean toBoolean(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        String string = value.toString();
        return string.equals("1")
                || string.equalsIgnoreCase("true")
                || string.equalsIgnoreCase("on");
    }

    public static long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            if (((String) value).isEmpty()) {
                return 0L;
            }
            try {
                return (long) Long.parseLong((String) value);
            } catch (Exception e) {
                return 0L;
            }
        }
        return 0L;
    }

    protected NativeFactory nativeFactory;

    @Override
    public void regist(GlobalManager manager) {
        JavaNativeUtil.addConstFields(manager, nativeFactory, ContextMethods.class);
        JavaNativeUtil.addStaticMethods(manager, nativeFactory, ContextMethods.class);
    }
}
