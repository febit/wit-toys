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
package org.febit.wit.toy.exprs;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author zqq90
 */
public class Main {

  public static void main(String[] args) {

    // 引擎是惰性加载的，先触发一下，防止本 demo 输出被内置日志被打断
    Expression.getEngine();
    
    println("已加载的全局常量： ");
    Expression.getEngine().getGlobalManager().forEachConst((key, val) -> {
      println("\t", key);
    });

    println("已加载的全局变量： ");
    Expression.getEngine().getGlobalManager().forEachGlobal((key, val) -> {
      println("\t", key);
    });

    myInfo();
    udfs();
    javaMethods();
  }
  
  private static void udfs(){
    println(Expression.create("concat2(\"udf demo: \", \"[\", substring(toLowerCase(str),5,15), \"]\")")
            .invoke(newMap("str", "0123456789ABCDEFGH")));
  }
  
  private static void javaMethods(){
    println(Expression.create("\"java methods demo: \" + \"[\" + str.~toLowerCase().~substring(5,15) + \"]\"")
            .invoke(newMap("str", "0123456789ABCDEFGH")));
  }

  private static void myInfo() {

    Map<String, Object> params = newMap(
            "name", "zqq90",
            "age", 18
    );

    // 表达式是可以作为单例的，重复利用，避免重复解析的性能开销
    Expression myAge = Expression.create("age");
    Expression myInfo = Expression.create("\"My name is \" + name + \" and i'm \" + age + \" years old.\"");
    Expression myInfo2 = Expression.create("`I said, I'm ${name}!`");
    Expression isAdult = Expression.create(" age >= 18 ? \"我已经足够大了！\" : \"我还是个宝宝！\" ");

    println(myAge.invoke(params));
    println(myInfo.invoke(params));
    println(myInfo2.invoke(params));
    println(isAdult.invoke(params));

    params.put("name", "febit");
    params.put("age", 17);

    println(myAge.invoke(params));
    println(myInfo.invoke(params));
    println(myInfo2.invoke(params));
    println(isAdult.invoke(params));
  }

  private static void println(Object... args) {
    System.out.println(Stream.of(args).map(String::valueOf).collect(Collectors.joining()));
  }

  private static Map<String, Object> newMap() {
    return new HashMap<>();
  }

  private static Map<String, Object> newMap(String k1, Object v1, Object... kvs) {
    if (kvs.length % 2 != 0) {
      throw new IllegalArgumentException("Need key-value pairs");
    }
    Map<String, Object> params = newMap();
    params.put(k1, v1);
    for (int i = 0; i < kvs.length;) {
      params.put(String.valueOf(kvs[i++]), kvs[i++]);
    }
    return params;
  }
}
