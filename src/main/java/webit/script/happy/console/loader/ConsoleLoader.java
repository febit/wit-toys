// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console.loader;

import org.febit.wit.loaders.Loader;
import org.febit.wit.loaders.Resource;
import org.febit.wit.loaders.impl.resources.StringResource;

/**
 * 资源加载器
 *
 * @author Zqq
 */
public class ConsoleLoader implements Loader {

    @Override
    public Resource get(String name) {
        return new StringResource(name, true);
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
