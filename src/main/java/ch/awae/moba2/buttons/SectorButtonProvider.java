package ch.awae.moba2.buttons;

import ch.awae.moba2.Sector;
import ch.awae.utils.logic.Logic;
import ch.awae.utils.logic.LogicGroup;

public class SectorButtonProvider {

    private final ButtonProvider provider;
    private final Sector sector;

    public SectorButtonProvider(ButtonProvider provider, Sector sector) {
        this.provider = provider;this.sector = sector;
    }

    public Logic button(String id) {
        return provider.getButton(sector, id);
    }

    public LogicGroup group(String id) {
        return provider.getGroup(sector, id);
    }

}
