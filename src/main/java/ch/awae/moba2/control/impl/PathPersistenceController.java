package ch.awae.moba2.control.impl;

import ch.awae.moba2.Sector;
import ch.awae.moba2.buttons.ButtonProvider;
import ch.awae.moba2.config.control.PathStoreConfiguration;
import ch.awae.moba2.control.Controller;
import ch.awae.moba2.logic.Timeout;
import ch.awae.moba2.path.PathRegistry;
import ch.awae.moba2.persistence.PathPersistenceService;
import ch.awae.utils.logic.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PathPersistenceController implements Controller {

    private final PathPersistenceService persistenceService;
    private final PathRegistry pathRegistry;

    private final Logic[] slotButtons;
    private final Logic saveButton;

    private final Timeout saveTimeout;

    @Autowired
    public PathPersistenceController(
            PathPersistenceService persistenceService,
            PathRegistry pathRegistry,
            ButtonProvider buttonProvider,
            PathStoreConfiguration config) {
        this.persistenceService = persistenceService;
        this.pathRegistry = pathRegistry;

        this.slotButtons = new Logic[5];
        this.saveButton = buttonProvider.getButton(Sector.LIGHTS, "light[11]").edge();

        for (int i = 0; i < 5; i++) {
            slotButtons[i] = buttonProvider.getButton(Sector.LIGHTS, "light[" + (i + 6) + "]").edge();
        }

        this.saveTimeout = new Timeout(config.getSaveTimeout());
    }

    @Override
    public void tick() {
        if (saveButton.evaluate()) {
            saveTimeout.start();
        }

        for (int i = 0; i < slotButtons.length; i++) {
            if (slotButtons[i].evaluate()) {
                if (saveTimeout.evaluate()) {
                    saveTimeout.stop();
                    handleSave(i);
                } else {
                    handleRead(i);
                }
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
