package ch.awae.moba2.command;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SwitchCommand {
    private final int command;
    private final int display;
    private final boolean test;

    public SwitchCommand(int command, int display) {
        this(command, display, false);
    }

}
