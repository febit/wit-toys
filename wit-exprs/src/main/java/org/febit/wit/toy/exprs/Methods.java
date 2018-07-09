/*
 * Copyright 2018 febit.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.febit.wit.toy.exprs;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author zqq
 */
public class Methods {

  public static final String METHODS_FLAG = "loaded methods";

  public static String concat(Object[] args) {
    if (args == null) {
      return null;
    }
    return Stream.of(args).map(String::valueOf).collect(Collectors.joining());
  }

  public static String toLowerCase(Object val) {
    if (val == null) {
      return null;
    }
    return val.toString().toLowerCase();
  }

  public static String substring(String val, int from, int to) {
    if (val == null) {
      return null;
    }
    return val.substring(from, to);
  }

  public static Object noop() {
    return null;
  }

  public static boolean isStartWith(String val, Object dict) {
    if (val == null) {
      return false;
    }
    if (dict instanceof String[]) {
      for (String item : (String[]) dict) {
        if (val.startsWith(item)) {
          return true;
        }
      }
      return false;
    }
    if (dict instanceof String) {
      return val.startsWith((String) dict);
    }
    // dict not support or dict == null
    return false;
  }

}
