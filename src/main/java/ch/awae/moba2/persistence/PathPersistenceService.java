package ch.awae.moba2.persistence;

import ch.awae.moba2.path.Path;
import ch.awae.moba2.path.PathProvider;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log
@Service
public class PathPersistenceService {

    private final PersistenceSlotRepository repository;
    private final PathProvider pathProvider;

    @Autowired
    public PathPersistenceService(PersistenceSlotRepository repository, PathProvider pathProvider) {
        this.repository = repository;
        this.pathProvider = pathProvider;
    }

    public Optional<Set<Path>> getPersistedPaths(int slotId) {
        return repository.findBySlotId(slotId)
                .map(PersistenceSlot::getPaths)
                .map(this::decodePathSet);
    }

    private Set<Path> decodePathSet(Set<PersistedPath> set) {
        return set.stream()
                .map(PersistedPath::getTitle)
                .map(pathProvider::getPath)
                .collect(Collectors.toSet());
    }

    public void persistPaths(int slotId, Collection<Path> paths) {
        PersistenceSlot slot = repository.findBySlotId(slotId).orElseGet(() -> new PersistenceSlot(slotId));

        slot.getPaths().clear();

        paths.stream()
                .filter(path -> !path.isClear())
                .map(PersistedPath::new)
                .collect(Collectors.toCollection(slot::getPaths));

        repository.saveAndFlush(slot);
    }

    @PostConstruct
    public void warmUp() {
        log.info("preheating database");
        repository.findBySlotId(0);
    }
}
