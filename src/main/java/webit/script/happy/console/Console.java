// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import webit.script.Engine;
import webit.script.exceptions.ParseException;
import webit.script.exceptions.ResourceNotFoundException;
import webit.script.exceptions.ScriptRuntimeException;
import webit.script.happy.console.util.EnvUtil;
import webit.script.happy.console.util.StringUtil;

/**
 *
 * @author Zqq
 */
public class Console {

    private Engine engine;
    private final String engineProps;

    private final ConsoleAttrabutes consoleAttrabutes;
    private Scanner scanner;
    private InputStream in;
    private PrintStream out;

    private final static char MULTI_LINE_START = '>';
    private final static char MULTI_LINE_END = '<';

    public final static String LINE_SEPARATOR_UNIX = "\n";
    public final static String LINE_SEPARATOR_IOS = "\r";
    public final static String LINE_SEPARATOR_WINDOWS = "\r\n";

    private final List<String> commandLines = new ArrayList<String>();

    public Console(String props, PrintStream out, InputStream in) {
        this.engineProps = props;
        this.out = out;
        this.in = in;
        this.consoleAttrabutes = new ConsoleAttrabutes();
        init();
    }

    private void init() {
        final ConsoleAttrabutes attrabutes = this.consoleAttrabutes;
        attrabutes.setExitFlag(false);
        attrabutes.setCurrentPath(EnvUtil.getUserDir());
        attrabutes.setEncoding("UTF-8");
        attrabutes.setLineSeparator(LINE_SEPARATOR_UNIX);

        Map<String, Object> settings = new HashMap<String, Object>();
        settings.put(ConsoleGlobalRegister.CONSOLE_CONFIG_KEY, attrabutes);

        this.scanner = new Scanner(in, attrabutes.getEncoding());
        this.engine = Engine.createEngine(engineProps, settings);
    }

    protected List<String> nextCommand() {
        askforCommand();
        final List<String> lines = this.commandLines;
        lines.clear();
        int multiLineStartCharCount = 0;
        int lineNumber = 0;
        while (true) {
            String line = this.scanner.nextLine();
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

    public void command(String[] args) {
        sayWellcome();
        List<String> command;
        while (true) {
            command = nextCommand();
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

    protected void mergeTemplate(List<String> commands) {
        try {
            println(">>>");
            engine.getTemplate(StringUtil.join(commands, this.consoleAttrabutes.getLineSeparator())).merge(out);
            println();
        } catch (ResourceNotFoundException ex) {
            println("找不到文件了: " + ex.getMessage());
        } catch (ParseException ex) {
            //XXX： 显示语法错误
            println("应该是语法错了: " + ex.getMessage());
        } catch (ScriptRuntimeException ex) {
            //XXX： 显示语法错误
            println("运行错了: " + ex.getMessage());
        } catch (Exception ex) {
            println("不知道是什么出错了: " + ex.getMessage());
        }
    }

    protected void askforCommand() {
        println(this.consoleAttrabutes.getCurrentPath() + " >");
    }

    protected void sayWellcome() {
        println("============================================");
        println("    Wellcome Webit Script World  \\(^o^)/");
        println("                   build：2014.05.21");
        println("                    QQ群：302505483");
        println("============================================");
    }

    protected void sayGoodBye() {
        println("Bye (^_^)∠※");
    }

    protected void printCommandLineNumber(int number) {
        StringBuilder sb = new StringBuilder(6);
        if (number >= 10) {
            sb.append(number);
        } else {
            sb.append(' ')
                    .append(number);
        }
        sb.append("| ");
        print(sb.toString());
    }

    protected void println(String msg) {
        this.out.println(msg);
    }

    protected void println(char msg) {
        this.out.println(msg);
    }

    protected void println() {
        this.out.println();
    }

    protected void print(String msg) {
        this.out.print(msg);
    }
}
