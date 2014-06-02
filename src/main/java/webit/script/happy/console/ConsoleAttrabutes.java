// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console;

import java.io.File;
import webit.script.exceptions.ScriptRuntimeException;
import webit.script.util.UnixStyleFileNameUtil;

/**
 *
 * @author Zqq
 */
public class ConsoleAttrabutes {

    public final static String LINE_SEPARATOR_UNIX = "\n";
    public final static String LINE_SEPARATOR_IOS = "\r";
    public final static String LINE_SEPARATOR_WINDOWS = "\r\n";

    public final static int PATH_SHOW_MODE_FULL_PATH = 1;
    public final static int PATH_SHOW_MODE_SHORT_PATH = 2;

    
    private int pathShowMode = PATH_SHOW_MODE_FULL_PATH;
    private String lineSeparator = LINE_SEPARATOR_UNIX;
    private boolean exitFlag = false;
    private String currentPath;
    private String encoding;
    private String fileEncoding = "UTF-8";

    public String cd(String to){
        String path = UnixStyleFileNameUtil.concat(currentPath, to);
        File file = new File(path);
        if (file.exists() == false) {
            throw new ScriptRuntimeException("path not found: "+ path);
        }
        this.currentPath = file.getAbsolutePath();
        return path;
    } 
    
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

    public String getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public int getPathShowMode() {
        return pathShowMode;
    }

    public void setPathShowMode(int pathShowMode) {
        this.pathShowMode = pathShowMode;
    }

    public void setLineSeparator(String lineSeparator) {
        if (!LINE_SEPARATOR_UNIX.equals(lineSeparator)
                && !LINE_SEPARATOR_IOS.equals(lineSeparator)
                && !LINE_SEPARATOR_WINDOWS.equals(lineSeparator)) {
            throw new ScriptRuntimeException("Unsupport line separator: '" + lineSeparator + "'.");
        }
        this.lineSeparator = lineSeparator;
    }

}
