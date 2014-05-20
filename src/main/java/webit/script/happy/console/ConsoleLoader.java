// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console;

import webit.script.exceptions.ResourceNotFoundException;
import webit.script.loaders.Loader;
import webit.script.loaders.Resource;
import webit.script.loaders.impl.resources.StringResource;

/**
 * 资源加载器
 *
 * @author Zqq
 */
public class ConsoleLoader implements Loader {

    @Override
    public Resource get(String name) throws ResourceNotFoundException {
        return new StringResource("<% " + name);
    }

    @Override
    public String concat(String parent, String name) {
        return name;
    }

    @Override
    public String normalize(String name) {
        return name;
    }

    @Override
    public boolean isEnableCache(String name) {
        return false;
    }
}
