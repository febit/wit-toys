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
package org.febit.wit.toy.console.loader;

import org.febit.wit.Engine;
import org.febit.wit.Init;
import org.febit.wit.toy.console.Console;
import org.febit.wit.toy.console.ConsoleSession;
import org.febit.wit.loaders.Loader;
import org.febit.wit.loaders.Resource;
import org.febit.wit.loaders.impl.resources.FileResource;
import org.febit.wit.util.FileNameUtil;

/**
 *
 * @author zqq90
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
