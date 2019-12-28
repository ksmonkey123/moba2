package ch.awae.moba2.persistence;

import ch.awae.moba2.path.Path;
import ch.awae.moba2.path.PathProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PathPersistenceService {

    private final PersistenceSlotRepository repository;
    private final PathProvider pathProvider;

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
}
