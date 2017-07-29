// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console.loader;

import org.febit.wit.Engine;
import org.febit.wit.Init;
import webit.script.happy.console.Console;
import webit.script.happy.console.ConsoleSession;
import org.febit.wit.loaders.Loader;
import org.febit.wit.loaders.Resource;
import org.febit.wit.loaders.impl.resources.FileResource;
import org.febit.wit.util.FileNameUtil;

/**
 *
 * @author Zqq
 */
public class ConsoleFileLoader implements Loader {

    private ConsoleSession consoleAttrabutes;
    
    @Init
    public void init(Engine engine) {
        this.consoleAttrabutes = (ConsoleSession) engine.getConfig(Console.CONSOLE_CONFIG_KEY);
    }

    @Override
    public Resource get(String name) {
        String path = FileNameUtil.concat(consoleAttrabutes.getCurrentPath(), name);
        return new FileResource(path, consoleAttrabutes.getFileEncoding());
    }

    @Override
    public String concat(String parent, String name) {
        return FileNameUtil.concat(parent, name);
    }

    @Override
    public String normalize(String name) {
        return FileNameUtil.concat(consoleAttrabutes.getCurrentPath(), name);
    }

    @Override
    public boolean isEnableCache(String name) {
        return false;
    }

}
