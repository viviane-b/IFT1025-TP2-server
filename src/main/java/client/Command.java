package client;

import java.io.Serializable;

public class Command implements Serializable {
    private String cmd;
    private String arg;

    public Command(String cmd, String arg) {
        this.cmd = cmd;
        this.arg = arg;
    }

    @Override
    public String toString() {
        return (cmd + " " + arg);
    }
}
