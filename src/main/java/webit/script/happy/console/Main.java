// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console;

import java.util.Scanner;
import webit.script.Engine;
import webit.script.exceptions.ParseException;
import webit.script.exceptions.ResourceNotFoundException;
import webit.script.exceptions.ScriptRuntimeException;

/**
 *
 * @author Zqq
 */
public class Main {

    private static Engine engine = Engine.createEngine("happy-console.props");

    public static void main(String[] args) {
        final Scanner sc = new Scanner(System.in);
        sayWellcome();
        String command;
        while (true) {
            askforCommand();
            command = sc.nextLine().trim();
            if (command.isEmpty()) {
                continue;
            }
            if (command.equals("exit")) {
                break;
            }
            mergeTemplate(command);
        }
        sayGoodBye();
    }

    private static void mergeTemplate(String message) {
        try {
            println(">>>");
            engine.getTemplate(message).merge(System.out);
            println();
            println();
        } catch (ResourceNotFoundException ex) {
            println("找不到什么东西了: " + ex.getMessage());
        } catch (ParseException ex) {
            println("应该是语法错了: " + ex.getMessage());
        } catch (ScriptRuntimeException ex) {
            println("运行错了: " + ex.getMessage());
        } catch (Exception ex) {
            println("不知道是什么出错了: " + ex.getMessage());
        }
    }

    private static void askforCommand() {
        println("<<<");
    }
    
    private static void sayWellcome() {
        println("============================================");
        println("    Wellcome Webit Script World \\(^o^)/");
        println("                    QQ群：302505483");
        println("============================================");
    }

    private static void sayGoodBye() {
        println("Bye (^_^)∠※");
    }

    private static void println(String msg) {
        System.out.println(msg);
    }

    private static void println() {
        System.out.println();
    }

    private static void print(String msg) {
        System.out.print(msg);
    }
}
