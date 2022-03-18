package org.shakespeareframework;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class OutputTestExtension implements BeforeEachCallback, AfterEachCallback {

    private final PrintStream originalOut;
    private final PrintStream originalErr;
    private final OutputStream out;
    private final OutputStream err;

    public OutputTestExtension() {
        originalOut = System.out;
        originalErr = System.err;
        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.out.println(out);
    }

    public String getOut() {
        return out.toString();
    }

    public String getErr() {
        return err.toString();
    }
}
