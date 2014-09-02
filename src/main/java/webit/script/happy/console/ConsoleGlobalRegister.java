// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console;

import webit.script.Engine;
import webit.script.Initable;
import webit.script.global.GlobalManager;
import webit.script.global.GlobalRegister;

/**
 *
 * @author Zqq
 */
public class ConsoleGlobalRegister implements GlobalRegister, Initable {

    public static final String CONSOLE = "console";

    private ConsoleAttrabutes consoleAttrabutes;

    @Override
    public void regist(GlobalManager manager) {
        manager.setConst(CONSOLE, this.consoleAttrabutes);
    }

    @Override
    public void init(Engine engine) {
        this.consoleAttrabutes = (ConsoleAttrabutes) engine.getConfig(Console.CONSOLE_CONFIG_KEY);
    }

}
