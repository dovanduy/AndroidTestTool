package com.android.test.shell.utest;

/**
 * Created by yuchaofei on 15/11/30.
 */
public class VTCommandResult {
    public final String stdout;
    public final String stderr;
    public final Integer exit_value;

    public VTCommandResult(Integer exit_value_in, String stdout_in, String stderr_in) {
        exit_value = exit_value_in;
        stdout = stdout_in;
        stderr = stderr_in;
    }

    VTCommandResult(Integer exit_value_in) {
        this(exit_value_in, null, null);
    }

    public boolean success() {
        return exit_value != null && exit_value == 0;
    }
}
