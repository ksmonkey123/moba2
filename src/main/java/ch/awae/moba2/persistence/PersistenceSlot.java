package ch.awae.moba2.persistence;

import lombok.*;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
class PersistenceSlot {

    @Id
    @GeneratedValue
    private long id;

    private int slotId;

    @Setter(AccessLevel.NONE)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<PersistedPath> paths = new HashSet<>();

    PersistenceSlot(int slotId) {
        this.slotId = slotId;
    }

}

interface PersistenceSlotRepository extends JpaRepository<PersistenceSlot, Long> {
    Optional<PersistenceSlot> findBySlotId(int slotId);
}
