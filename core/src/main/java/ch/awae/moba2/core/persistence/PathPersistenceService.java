package ch.awae.moba2.core.persistence;

import ch.awae.moba2.common.LogHelper;
import ch.awae.moba2.core.path.Path;
import ch.awae.moba2.core.path.PathProvider;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

//@Validated
@Service
public class PathPersistenceService {

    private final static Logger LOG = LogHelper.getLogger();

    private final PersistenceSlotRepository slotRepo;
    private final PathProvider pathProvider;

    @Autowired
    public PathPersistenceService(PersistenceSlotRepository slotRepo, PathProvider pathProvider) {
        this.slotRepo = slotRepo;
        this.pathProvider = pathProvider;
    }

    public Optional<Collection<Path>> getPersistedPaths(
            @Range(min = 0, max = 5) int slotId) {
        return slotRepo.findBySlotId(slotId)
                .map(PersistenceSlot::getPaths)
                .map(this::decodePathSet);
    }

    private Collection<Path> decodePathSet(Set<String> titles) {
        return titles.stream()
                .map(pathProvider::getPath)
                .collect(Collectors.toSet());
    }

    public void persistPaths(
            @Range(min = 0, max = 5) int slotId,
            @NotNull Collection<Path> paths) {
        PersistenceSlot slot = slotRepo.findBySlotId(slotId).orElseGet(() -> new PersistenceSlot(slotId));

        HashSet<String> titles = paths
                .stream()
                .filter(path -> !path.isClear())
                .map(Path::getTitle)
                .collect(Collectors.toCollection(HashSet::new));

        if (titles.isEmpty()) {
            slot.setPaths(null);
        } else {
            slot.setPaths(titles);
        }

        slotRepo.saveAndFlush(slot);
        LOG.info("slot " + slotId + " updated");
    }

    @PostConstruct
    public void warmUp() {
        LOG.info("preheating database");
        slotRepo.findBySlotId(0);
        LOG.info("database preheated");
    }
}
