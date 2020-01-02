package ch.awae.moba2.core.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Optional;

@Entity
class PersistenceSlot {

    @Id
    @GeneratedValue
    private long id;

    private int slotId;

    @Basic
    private HashSet<String> paths = new HashSet<>();

    PersistenceSlot() {}

    PersistenceSlot(int slotId) {
        this.slotId = slotId;
    }

    public long getId() {
        return id;
    }

    public int getSlotId() {
        return slotId;
    }

    public HashSet<String> getPaths() {
        return paths;
    }

    public void setPaths(HashSet<String> paths) {
        this.paths = paths;
    }
}

@Repository
interface PersistenceSlotRepository extends JpaRepository<PersistenceSlot, Long> {
    Optional<PersistenceSlot> findBySlotId(int slotId);
}
