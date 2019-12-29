package ch.awae.moba2.command;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LightCommand {
    private final String[] enabled;
    private final boolean test;

    public LightCommand(String[] enabled) {
        this(enabled, false);
    }

}
