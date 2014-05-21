// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console;

import webit.script.Engine;
import webit.script.Initable;
import webit.script.global.GlobalManager;
import webit.script.global.GlobalRegister;
import webit.script.util.SimpleBag;

/**
 *
 * @author Zqq
 */
public class ConsoleGlobalRegister implements GlobalRegister, Initable {

    public static final String CONSOLE = "console";
    public static final String CONSOLE_CONFIG_KEY = ConsoleGlobalRegister.class + ".CONSOLE_CONFIG_KEY";

    private ConsoleAttrabutes consoleAttrabutes;

    @Override
    public void regist(GlobalManager manager) {
        SimpleBag constBag = manager.getConstBag();
        constBag.set(CONSOLE, this.consoleAttrabutes);
    }

    @Override
    public void init(Engine engine) {
        this.consoleAttrabutes = (ConsoleAttrabutes) engine.getConfig(CONSOLE_CONFIG_KEY);
    }

}
