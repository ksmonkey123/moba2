package ch.awae.moba2.core.command;

public class SwitchCommand {
    private final int command;
    private final int display;
    private final boolean test;

    public SwitchCommand(int command, int display) {
        this(command, display, false);
    }

    SwitchCommand(int command, int display, boolean test) {
        this.command = command;
        this.display = display;
        this.test = test;
    }

    public int getCommand() {
        return command;
    }

    public int getDisplay() {
        return display;
    }

    public boolean isTest() {
        return test;
    }

    @Override
    public String toString() {
        return "SwitchCommand{" +
                "command=" + command +
                ", display=" + display +
                ", test=" + test +
                '}';
    }
}
