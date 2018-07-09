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

import java.util.UUID;
import org.febit.wit.Engine;
import org.febit.wit.Init;
import org.febit.wit.core.NativeFactory;
import org.febit.wit.global.GlobalManager;
import org.febit.wit.global.GlobalRegister;
import org.febit.wit.lang.MethodDeclare;
import org.febit.wit.util.JavaNativeUtil;

/**
 *
 * @author zqq90
 */
public class ExprsGlobalRegister implements GlobalRegister {

  static final MethodDeclare FUNC_UUID = (context, args) -> UUID.randomUUID();

  // Note: 引擎管理的组件会被自动注入
  protected NativeFactory nativeFactory;

  @Override
  public void regist(GlobalManager manager) {

    // 手动注册全局函数
    manager.setConstMethod("concat2", (context, args) -> Methods.concat(args));
    manager.setConst("uuid", FUNC_UUID);

    // 手动注册全局常量
    manager.setConst("WIT", "Wit is template engine");

    // 注册全局变量，仅限当前进程的引擎！
    manager.setGlobal("global", "全局变量");

    // 批量注册指定类中所有 public static 的方法
    JavaNativeUtil.addConstFields(manager, nativeFactory, Methods.class);
    JavaNativeUtil.addStaticMethods(manager, nativeFactory, Methods.class);
  }

  @Init
  public void init(Engine engine) {
  }
}
