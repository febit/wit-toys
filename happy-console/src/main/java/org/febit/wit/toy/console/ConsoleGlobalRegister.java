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
package org.febit.wit.toy.console;

import org.febit.wit.Engine;
import org.febit.wit.Init;
import org.febit.wit.global.GlobalManager;
import org.febit.wit.global.GlobalRegister;

/**
 *
 * @author zqq90
 */
public class ConsoleGlobalRegister implements GlobalRegister {

    public static final String CONSOLE = "console";

    private ConsoleSession consoleAttrabutes;

    @Override
    public void regist(GlobalManager manager) {
        manager.setConst(CONSOLE, this.consoleAttrabutes);
    }

    @Init
    public void init(Engine engine) {
        this.consoleAttrabutes = (ConsoleSession) engine.getConfig(Console.CONSOLE_CONFIG_KEY);
    }
}
