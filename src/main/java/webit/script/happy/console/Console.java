// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import webit.script.Engine;
import webit.script.Template;
import webit.script.exceptions.ParseException;
import webit.script.exceptions.ResourceNotFoundException;
import webit.script.exceptions.ScriptRuntimeException;
import webit.script.happy.console.util.EnvUtil;
import webit.script.happy.console.util.PrintStreamWriter;
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
    private final PrintStream out;
    private final Writer writer;

    private final static char MULTI_LINE_START = '>';
    private final static char MULTI_LINE_END = '<';

    public final static String LINE_SEPARATOR_UNIX = "\n";
    public final static String LINE_SEPARATOR_IOS = "\r";
    public final static String LINE_SEPARATOR_WINDOWS = "\r\n";

    private final List<String> commandLines = new ArrayList<String>();

    public Console(String props, PrintStream out, InputStream in) {
        this.engineProps = props;
        this.out = out;
        this.writer = new PrintStreamWriter(out);
        this.in = in;
        this.consoleAttrabutes = new ConsoleAttrabutes();
        init();
    }

    private void init() {
        final ConsoleAttrabutes attrabutes = this.consoleAttrabutes;
        attrabutes.setExitFlag(false);
        attrabutes.setCurrentPath(EnvUtil.getUserDir());
        attrabutes.setEncoding(EnvUtil.getFileEncoding());

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
        Template template = null;
        try {
            println(">>>");
            template = engine.getTemplate(StringUtil.join(commands, this.consoleAttrabutes.getLineSeparator()));
            template.merge(writer);
            println();
        } catch (ParseException ex) {
            ex.setLine(ex.getLine() - 1); //fix line
            println("语法错误: " + ex.getMessage()); //XXX: fix line misstake in message
            if (template == ex.getTemplate()) {
                printCommandLinePosition(commands.get(ex.getLine() - 1), ex.getLine(), ex.getColumn());
            } else {
                println("** 赞未支持非控制台资源的错误行打印");
            }
        } catch (ScriptRuntimeException ex) {
            //XXX: 显示语法错误
            //XXX: 纠正行显示错误
            println("运行时异常: " + ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            println("找不到文件了: " + ex.getMessage());
        } catch (Exception ex) {
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
        println(this.consoleAttrabutes.getCurrentPath() + " >");
    }

    protected void sayWellcome() {
        println("============================================");
        println("    Wellcome Webit Script World  \\(^o^)/");
        println("                   build：2014.05.31");
        println("                    QQ群：302505483");
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
