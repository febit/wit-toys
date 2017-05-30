// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console;

import java.io.File;
import org.febit.wit.exceptions.ParseException;
import org.febit.wit.exceptions.ResourceNotFoundException;
import org.febit.wit.exceptions.ScriptRuntimeException;
import org.febit.wit.util.FileNameUtil;

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
    private final Console console;

    public ConsoleAttrabutes(Console console) {
        this.console = console;
    }

    public String cd(String to) {
        String path = FileNameUtil.concat(currentPath, to);
        File file = new File(path);
        if (file.exists() == false) {
            throw new ScriptRuntimeException("path not found: " + path);
        }
        this.currentPath = file.getAbsolutePath();
        return path;
    }

    public void exec(String template) throws ResourceNotFoundException, ParseException, ScriptRuntimeException {
        console.mergeTemplate(template);
    }

    public void dir(String to) {
        String path;
        if (to != null) {
            path = FileNameUtil.concat(currentPath, to);
        } else {
            path = currentPath;
        }
        File file = new File(path);
        if (file.exists() == false) {
            throw new ScriptRuntimeException("path not found: " + path);
        }
        if (file.isDirectory() == false) {
            throw new ScriptRuntimeException("path not a directory: " + path);
        }
        File[] files = file.listFiles();
        console.println("Find " + files.length + " files:");
        for (File file1 : files) {
            console.println("    " + file1.getName());
        }
    }

    public void showLastException() {
        console.showLastException();
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
