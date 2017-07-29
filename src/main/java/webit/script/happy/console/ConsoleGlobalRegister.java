// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console;

import org.febit.wit.Engine;
import org.febit.wit.Init;
import org.febit.wit.global.GlobalManager;
import org.febit.wit.global.GlobalRegister;

/**
 *
 * @author Zqq
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
