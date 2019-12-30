package ch.awae.moba2.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
class PersistenceSlot {

    @Id
    @GeneratedValue
    private long id;

    private int slotId;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<PersistedPath> paths = new HashSet<>();

    PersistenceSlot() {}

    PersistenceSlot(int slotId) {
        this.slotId = slotId;
    }

    public Set<PersistedPath> getPaths() {
        return paths;
    }

    public long getId() {
        return id;
    }

    public int getSlotId() {
        return slotId;
    }
}

@Repository
interface PersistenceSlotRepository extends JpaRepository<PersistenceSlot, Long> {
    Optional<PersistenceSlot> findBySlotId(int slotId);
}
