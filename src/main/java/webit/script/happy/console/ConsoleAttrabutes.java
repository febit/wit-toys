// Copyright (c) 2013, Webit Team. All Rights Reserved.

package webit.script.happy.console;

/**
 *
 * @author Zqq
 */
public class ConsoleAttrabutes {
    
    private boolean exitFlag;
    private String currentPath;
    private String encoding;

    public boolean isExitFlag() {
        return exitFlag;
    }

    public void setExitFlag(boolean exitFlag) {
        this.exitFlag = exitFlag;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    
}
