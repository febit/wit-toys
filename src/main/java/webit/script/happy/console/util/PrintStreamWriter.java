// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.happy.console.util;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.CharBuffer;

/**
 *
 * @author Zqq
 */
public class PrintStreamWriter extends Writer {

    private final PrintStream out;

    public PrintStreamWriter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        out.append(CharBuffer.wrap(cbuf), off, off + len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

}
