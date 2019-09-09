package ch.awae.moba2.buttons;

import ch.awae.moba2.Sector;
import ch.awae.moba2.config.YamlPropertiesLoader;
import ch.awae.utils.logic.Logic;
import ch.awae.utils.logic.LogicGroup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Service
public class ButtonProvider {

    private final ButtonRegistry registry;
    private final Properties properties;

    public ButtonProvider(YamlPropertiesLoader loader, ButtonRegistry registry) throws IOException {
        this.registry = registry;
        this.properties = loader.load("buttons.yml");
    }

    public SectorButtonProvider sector(Sector sector) {
        return new SectorButtonProvider(this, sector);
    }

    private int getMask(Sector sector, String id) {
        Objects.requireNonNull(sector, "sector may not be null");
        Objects.requireNonNull(id, "id may not be null");

        String value = properties.getProperty(sector.name().toLowerCase() + "." + id);
        if (value == null)
            throw new NullPointerException("no button for id '" + id + "'");
        return Integer.parseInt(value) & 0x0000ffff;
    }

    public Logic getButton(Sector sector, String id) {
        int mask = getMask(sector, id);
        // check if mask is ok (single '1'-bit)
        if ((mask & (mask - 1)) != 0)
            throw new IllegalArgumentException("Identifier '" + id + "' in sector '" + sector.name()
                    + "' identifies a group, not a button!");
        // mask is ok
        return new Button(registry, sector, mask);
    }

    public LogicGroup getGroup(Sector sector, String id) {
        int mask = getMask(sector, id);

        Logic[] result = new Logic[countOnes(mask)];

        int index = 0;
        for (int i = 0; i < 16; i++) {
            if (((mask >> i) & 0x00000001) == 1) {
                int partialMask = mask & (1 << i);
                result[index] = new Button(registry, sector, partialMask);
                index++;
            }
        }

        return new LogicGroup(result);
    }

    private int countOnes(int number) {
        int num = number;
        int count = 0;
        for (int i = 0; i < 16; i++) {
            if ((num & 0x00000001) == 1)
                count++;
            num >>= 1;
        }
        return count;
    }

}
