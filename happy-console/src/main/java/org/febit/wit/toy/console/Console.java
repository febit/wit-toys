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
package org.febit.wit.toy.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.febit.wit.Context;
import org.febit.wit.Engine;
import org.febit.wit.Template;
import org.febit.wit.exceptions.ParseException;
import org.febit.wit.exceptions.ResourceNotFoundException;
import org.febit.wit.exceptions.ScriptRuntimeException;
import org.febit.wit.toy.console.util.EnvUtil;
import org.febit.wit.toy.console.util.PrintStreamWriter;
import org.febit.wit.toy.console.util.StringUtil;

/**
 *
 * @author zqq90
 */
public class Console {

    public static final String CONSOLE_CONFIG_KEY = "CONSOLE_CONFIG";
    private final static char MULTI_LINE_START = '>';
    private final static char MULTI_LINE_END = '<';

    private Engine engine;
    private final String engineProps;

    private Scanner scanner;
    private String currentScannerEncoding;
    private final InputStream in;
    private final PrintStream out;
    private final Writer writer;
    private final List<String> commandLines;
    private final ConsoleSession consoleAttrabutes;

    public Console(String props, PrintStream out, InputStream in) {
        this.commandLines = new ArrayList<>();
        this.engineProps = props;
        this.out = out;
        this.writer = new PrintStreamWriter(out);
        this.in = in;
        this.consoleAttrabutes = new ConsoleSession(this);
        init();
    }

    private void init() {
        final ConsoleSession attrabutes = this.consoleAttrabutes;
        attrabutes.setExitFlag(false);
        attrabutes.setCurrentPath(EnvUtil.getUserDir());
        attrabutes.setEncoding(EnvUtil.getFileEncoding());

        Map<String, Object> settings = new HashMap<>();
        settings.put(CONSOLE_CONFIG_KEY, attrabutes);

        currentScannerEncoding = attrabutes.getEncoding();
        //this.scanner = new Scanner(in, attrabutes.getEncoding());
        this.engine = Engine.create(engineProps, settings);
    }

    protected String nextLine() {
        Scanner myScanner = this.scanner;
        String encoding = consoleAttrabutes.getEncoding();
        if (myScanner == null
                || (encoding != null && !encoding.equals(currentScannerEncoding))
                || (encoding == null && currentScannerEncoding != null)) {
            currentScannerEncoding = encoding;
            if (encoding != null) {
                this.scanner = myScanner = new Scanner(in, encoding);
            } else {
                this.scanner = myScanner = new Scanner(in);
            }
        }
        return myScanner.nextLine();
    }

    protected List<String> nextCommand() {
        askforCommand();
        final List<String> lines = this.commandLines;
        lines.clear();
        int multiLineStartCharCount = 0;
        int lineNumber = 0;
        while (true) {
            String line = nextLine();
            if (multiLineStartCharCount == 0) {
                //判定是否是>>..>，否则按单行command
                if (line.isEmpty()) {
                    continue;
                }
                if (StringUtil.isRepeatOfChar(line, MULTI_LINE_START)) {
                    multiLineStartCharCount = line.length();
                    printCommandLineNumber(++lineNumber);
                } else {
                    lines.add(line);
                    break;
                }
            } else {
                //多行模式
                if (line.length() == multiLineStartCharCount
                        && StringUtil.isRepeatOfChar(line, MULTI_LINE_END)) {
                    break;
                } else {
                    printCommandLineNumber(++lineNumber);
                    lines.add(line);
                }
            }
        }
        return lines;
    }

    public void start(String[] args) {
        sayWellcome();
        List<String> command;
        while (true) {
            command = nextCommand();
            println(">>>");
            mergeTemplate(command);
            if (requestExit()) {
                break;
            }
        }
        sayGoodBye();
    }

    protected boolean requestExit() {
        return this.consoleAttrabutes.isExitFlag();
    }

    public void showLastException() {
        if (lastException != null) {
            lastException.printStackTrace(out);
        } else {
            println("没有发生异常");
        }
    }
    private Exception lastException;

    protected void mergeTemplate(String command) throws ResourceNotFoundException, ParseException, ScriptRuntimeException {
        engine.getTemplate(command).merge(writer);
    }

    protected void mergeTemplate(List<String> commands) {
        Template template = null;
        try {
            template = engine.getTemplate("cmd:" + StringUtil.join(commands, this.consoleAttrabutes.getLineSeparator()));
            Context context = template.merge(writer);
            println();
            //export to global
            Map<String, Object> vars = new HashMap<>();
            context.exportTo(vars);
            vars.forEach(this.engine.getGlobalManager()::setGlobal);
            this.lastException = null;
        } catch (ParseException ex) {
            this.lastException = ex;
            println("语法错误: " + ex.getMessage()); //XXX: fix line misstake in message
            if (template == ex.getTemplate() && ex.getLine() >= 1) {
                printCommandLinePosition(commands.get(ex.getLine() - 1), ex.getLine(), ex.getColumn());
            } else {
                println("** 不支持非控制台资源的错误行打印");
            }
        } catch (ScriptRuntimeException ex) {
            this.lastException = ex;
            //XXX: 显示语法错误
            println("运行时异常: " + ex.getMessage());
            ex.printStackTrace(out);
        } catch (ResourceNotFoundException ex) {
            this.lastException = ex;
            println("找不到文件了: " + ex.getMessage());
        } catch (Exception ex) {
            this.lastException = ex;
            println("不知道是什么出错了: " + ex.getMessage());
        }
    }

    protected void printCommandLinePosition(String commandLine, int line, int column) {
        int headLength;
        headLength = printCommandLineNumber(line);
        println(commandLine);
        int ahead;
        if (column > commandLine.length()) {
            ahead = StringUtil.getDbcLength(commandLine, 0, commandLine.length()) + column - commandLine.length();
        } else {
            ahead = StringUtil.getDbcLength(commandLine, 0, column);
        }
        printPosition(headLength + ahead);
    }

    protected void printPosition(int column) {
        if (column <= 0) {
            return;
        }
        char[] msg = new char[column];
        Arrays.fill(msg, 0, column - 1, ' ');
        msg[column - 1] = '^';
        println(msg);
    }

    protected void askforCommand() {
        println(this.consoleAttrabutes.getCurrentPath() + ">");
    }

    protected void sayWellcome() {
        println("============================================");
        println("    Wellcome Febit Wit World  \\(^o^)/");
        println("============================================");
    }

    protected void sayGoodBye() {
        println("Bye (^_^)∠※");
    }

    protected int printCommandLineNumber(int number) {
        StringBuilder sb = new StringBuilder(6);
        if (number >= 10) {
            sb.append(number);
        } else {
            sb.append(' ')
                    .append(number);
        }
        sb.append("| ");
        print(sb.toString());
        return sb.length();
    }

    protected void println(String msg) {
        this.out.println(msg);
    }

    protected void println(char msg) {
        this.out.println(msg);
    }

    protected void println(char[] msg) {
        this.out.println(msg);
    }

    protected void println() {
        this.out.println();
    }

    protected void print(String msg) {
        this.out.print(msg);
    }
}
