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

import fi.iki.elonen.NanoHTTPD;
import static fi.iki.elonen.NanoHTTPD.MIME_HTML;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.febit.wit.Engine;
import org.febit.wit.Template;
import org.febit.wit.exceptions.ParseException;
import org.febit.wit.exceptions.ResourceNotFoundException;
import org.febit.wit.exceptions.ScriptRuntimeException;

/**
 *
 * @author zqq90
 */
public class WitHTTPD extends NanoHTTPD {

    protected static final String WIT_CONFIG = "wit-httpd.wim";
    protected static final String INDEX_PAGE = "/index.wit";
    protected static final String LOCAL_KEY_RESPONSE = "$$__RESPONSE";
    protected static final ThreadLocal<WitResponce> WIT_RESPONCE = new ThreadLocal<>();

    public static WitResponce getWitResponce() {
        return WIT_RESPONCE.get();
    }

    public static class WitResponce {

        protected final Map<String, String> header = new HashMap<>();
        protected final CookieHandler cookieHandler;
        protected String mimeType = MIME_HTML;
        protected String encoding = "utf-8";
        protected int statusCode = 200;

        public WitResponce(IHTTPSession session) {
            this.cookieHandler = session.getCookies();
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public void setHeader(String name, String value) {
            this.header.put(name, value);
        }

        public Map<String, String> getHeader() {
            return Collections.unmodifiableMap(header);
        }

        public Response.Status getStatus() {
            return Response.Status.lookup(statusCode);
        }

        public String getMimeType() {
            String prefix = mimeType != null ? mimeType : MIME_HTML;
            return encoding == null ? prefix : prefix + ";charset=" + encoding;
        }

        public void setCookie(String name, String value) {
            this.cookieHandler.set(new Cookie(name, value));
        }

        public void setCookie(String name, String value, int days) {
            this.cookieHandler.set(new Cookie(name, value, days));
        }

        public void removeCookie(String name) {
            this.cookieHandler.delete(name);
        }
    }

    private Engine witEngine;

    public WitHTTPD(int port) {
        super(port);
    }

    public WitHTTPD(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public void start(int timeout, boolean daemon) throws IOException {
        this.witEngine = Engine.create(WIT_CONFIG);
        super.start(timeout, daemon);
    }

    @Override
    public void stop() {
        super.stop();
        this.witEngine = null;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        if (uri.equals("/")) {
            uri = INDEX_PAGE;
        }
        if (isWitFile(uri)) {
            try {
                return handleWit(uri, session);
            } catch (IOException ioe) {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            } catch (ResponseException re) {
                return newFixedLengthResponse(re.getStatus(), NanoHTTPD.MIME_PLAINTEXT, re.getMessage());
            } catch (ParseException | ScriptRuntimeException e) {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "SERVER SCRIPT ERROR: " + e.getMessage());
            }
        }
        return super.serve(session);
    }

    protected Response handleWit(String uri, IHTTPSession session)
            throws IOException, ResponseException, ParseException, ScriptRuntimeException {
        Method method = session.getMethod();
        Template template;
        try {
            template = witEngine.getTemplate(uri);
        } catch (ResourceNotFoundException e) {
            return response404(uri, method);
        }

        Map<String, Object> pageParams = new HashMap<>();

        pageParams.put("$_METHOD", method.name());
        pageParams.put("$_FILE", Collections.unmodifiableMap(readFiles(session)));
        pageParams.put("$_PARAM", Collections.unmodifiableMap(session.getParms()));
        pageParams.put("$_COOKIE", Collections.unmodifiableMap(readCookies(session)));
        pageParams.put("$_HEADER", Collections.unmodifiableMap(session.getHeaders()));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WitResponce witResponce = new WitResponce(session);
        WIT_RESPONCE.set(witResponce);
        try {
            template.merge(pageParams, out);
        } finally {
            WIT_RESPONCE.remove();
        }
        return createResponse(witResponce, out.toByteArray());
    }

    protected Response createResponse(WitResponce witResponce, byte[] bytes) {
        Response response = newFixedLengthResponse(
                witResponce.getStatus(),
                witResponce.getMimeType(),
                new ByteArrayInputStream(bytes),
                bytes.length
        );
        for (Map.Entry<String, String> entry : witResponce.getHeader().entrySet()) {
            response.addHeader(entry.getKey(), entry.getValue());
        }
        return response;
    }

    protected Map<String, String> readCookies(IHTTPSession session) {
        CookieHandler cookieHandler = session.getCookies();
        Map<String, String> cookies = new HashMap<>();
        for (String key : cookieHandler) {
            cookies.put(key, cookieHandler.read(key));
        }
        return cookies;
    }

    protected Map<String, String> readFiles(IHTTPSession session) throws IOException, ResponseException {
        Method method = session.getMethod();
        if (Method.PUT.equals(method) || Method.POST.equals(method)) {
            Map<String, String> files = new HashMap<>();
            session.parseBody(files);
            return files;
        }
        return Collections.emptyMap();
    }

    protected boolean isWitFile(String uri) {
        return uri.endsWith(".wit");
    }

    protected Response response404(String uri, Method method) {
        return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Not Found");
    }
}
