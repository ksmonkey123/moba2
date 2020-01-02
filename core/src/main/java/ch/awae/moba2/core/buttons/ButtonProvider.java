package ch.awae.moba2.core.buttons;

import ch.awae.moba2.core.Sector;
import ch.awae.moba2.core.config.ProviderConfiguration;
import ch.awae.moba2.core.config.YamlLoader;
import ch.awae.utils.logic.Logic;
import ch.awae.utils.logic.LogicGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Properties;

@Validated
@Service
public class ButtonProvider {

    private final ButtonRegistry registry;
    private final ApplicationContext context;
    private final Properties properties;

    @Autowired
    public ButtonProvider(YamlLoader loader, ButtonRegistry registry, ProviderConfiguration config, ApplicationContext context) throws IOException {
        this.registry = registry;
        this.context = context;
        this.properties = loader.load(config.getButton());
    }

    public SectorButtonProvider sector(@NotNull Sector sector) {
        return new SectorButtonProvider(context.getBean(ButtonProvider.class), sector);
    }

    private int getMask(Sector sector, String id) {
        String value = properties.getProperty(sector.name().toLowerCase() + "." + id);
        if (value == null)
            throw new NullPointerException("no button for id '" + id + "'");
        return Integer.parseInt(value) & 0x0000ffff;
    }

    public Logic getButton(@NotNull Sector sector, @NotEmpty String id) {
        int mask = getMask(sector, id);
        // check if mask is ok (single '1'-bit)
        if ((mask & (mask - 1)) != 0)
            throw new IllegalArgumentException("Identifier '" + id + "' in sector '" + sector.name()
                    + "' identifies a group, not a button!");
        // mask is ok
        return new Button(registry, sector, mask);
    }

    public LogicGroup getGroup(@NotNull Sector sector, @NotEmpty String id) {
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
