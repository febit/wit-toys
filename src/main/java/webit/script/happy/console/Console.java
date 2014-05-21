// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import webit.script.Engine;
import webit.script.exceptions.ParseException;
import webit.script.exceptions.ResourceNotFoundException;
import webit.script.exceptions.ScriptRuntimeException;
import webit.script.happy.console.util.EnvUtil;

/**
 *
 * @author Zqq
 */
public class Console {

    private Engine engine;
    private final String engineProps;

    private final PrintStream out;
    private final Scanner scanner;
    private final ConsoleAttrabutes consoleAttrabutes;

    public Console(String props, PrintStream out, InputStream in) {
        this.engineProps = props;
        this.out = out;
        this.scanner = new Scanner(in);
        this.consoleAttrabutes = new ConsoleAttrabutes();
        init();
    }

    private void init() {
        final ConsoleAttrabutes attrabutes = this.consoleAttrabutes;
        attrabutes.setExitFlag(false);
        attrabutes.setCurrentPath(EnvUtil.getUserDir());
        attrabutes.setEncoding("UTF-8");
        
        Map<String, Object> settings = new HashMap<String, Object>();
        settings.put(ConsoleGlobalRegister.CONSOLE_CONFIG_KEY, attrabutes);
        
        this.engine = Engine.createEngine(engineProps, settings);
    }
    
    protected String nextCommand() {
        askforCommand();
        String line = this.scanner.nextLine().trim();
        return line;
    }

    public void command(String[] args) {
        sayWellcome();
        String command;
        while (true) {
            command = nextCommand();
            if (command.isEmpty()) {
                continue;
            }
            //
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

    protected void mergeTemplate(String message) {
        try {
            println(">>>");
            engine.getTemplate(message).merge(out);
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
