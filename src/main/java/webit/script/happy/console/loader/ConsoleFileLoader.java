// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console.loader;

import webit.script.Engine;
import webit.script.Initable;
import webit.script.exceptions.ResourceNotFoundException;
import webit.script.happy.console.Console;
import webit.script.happy.console.ConsoleAttrabutes;
import webit.script.loaders.Loader;
import webit.script.loaders.Resource;
import webit.script.loaders.impl.resources.FileResource;
import webit.script.util.UnixStyleFileNameUtil;

/**
 *
 * @author Zqq
 */
public class ConsoleFileLoader implements Loader, Initable {

    private ConsoleAttrabutes consoleAttrabutes;
    
    @Override
    public void init(Engine engine) {
        this.consoleAttrabutes = (ConsoleAttrabutes) engine.getConfig(Console.CONSOLE_CONFIG_KEY);
    }

    @Override
    public Resource get(String name) {
        String path = UnixStyleFileNameUtil.concat(consoleAttrabutes.getCurrentPath(), name);
        return new FileResource(path, consoleAttrabutes.getFileEncoding());
    }

    @Override
    public String concat(String parent, String name) {
        return UnixStyleFileNameUtil.concat(parent, name);
    }

    @Override
    public String normalize(String name) {
        return UnixStyleFileNameUtil.concat(consoleAttrabutes.getCurrentPath(), name);
    }

    @Override
    public boolean isEnableCache(String name) {
        return true;
    }

}
