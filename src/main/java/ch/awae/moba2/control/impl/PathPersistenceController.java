package ch.awae.moba2.control.impl;

import ch.awae.moba2.Sector;
import ch.awae.moba2.buttons.ButtonProvider;
import ch.awae.moba2.control.Controller;
import ch.awae.moba2.path.PathRegistry;
import ch.awae.moba2.persistence.PathPersistenceService;
import ch.awae.utils.logic.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PathPersistenceController implements Controller {

    private final PathPersistenceService persistenceService;
    private final PathRegistry pathRegistry;

    private final Logic[] readButtons;
    private final Logic[] saveButtons;

    @Autowired
    public PathPersistenceController(
            PathPersistenceService persistenceService,
            PathRegistry pathRegistry,
            ButtonProvider buttonProvider) {
        this.persistenceService = persistenceService;
        this.pathRegistry = pathRegistry;

        this.readButtons = new Logic[5];
        this.saveButtons = new Logic[5];

        for (int i = 0; i < 5; i++) {
            readButtons[i] = buttonProvider.getButton(Sector.LIGHTS, "light[" + (i + 6) + "]").edge();
            saveButtons[i] = buttonProvider.getButton(Sector.LIGHTS, "light[" + (i + 11) + "]").edge();
        }
    }

    @Override
    public void tick() {
        for (int i = 0; i < readButtons.length; i++) {
            if (readButtons[i].evaluate()) {
                handleRead(i);
            }
        }

        for (int i = 0; i < saveButtons.length; i++) {
            if (saveButtons[i].evaluate()) {
                handleSave(i);
            }
        }
    }

    private void handleRead(int slotId) {
        persistenceService.getPersistedPaths(slotId).ifPresent(pathRegistry::registerAll);
    }

    private void handleSave(int slotId) {
        persistenceService.persistPaths(slotId, pathRegistry.getAllPaths());
    }

}
