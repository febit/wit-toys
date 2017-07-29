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
package org.febit.wit.toy.httpd;

import fi.iki.elonen.util.ServerRunner;

/**
 *
 * @author zqq90
 */
public class Main {

    public static void main(String[] args) {
        // Get port from env.
        // Thinks lean-engine: https://leancloud.cn/
        int appPort;
        try {
            appPort = Integer.parseInt(System.getenv("LEANCLOUD_APP_PORT"));
        } catch (NumberFormatException e) {
            appPort = 8080;
        }
        WitHTTPD server = new WitHTTPD(appPort);
        ServerRunner.executeInstance(server);
    }
}
