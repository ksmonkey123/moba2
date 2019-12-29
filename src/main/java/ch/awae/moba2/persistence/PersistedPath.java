package ch.awae.moba2.persistence;

import ch.awae.moba2.path.Path;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
class PersistedPath {

    private String title;

    PersistedPath(Path path) {
        this.title = path.getTitle();
    }

}
