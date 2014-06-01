// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console;

/**
 *
 * @author Zqq
 */
public class Main {

    private final static String PROPS_PATH = "happy-console.props";

    public static void main(String[] args) {
        new Console(PROPS_PATH, System.out, System.in).start(args);
    }

}
