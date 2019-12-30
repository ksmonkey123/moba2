package ch.awae.moba2.core.persistence;

import ch.awae.moba2.core.path.Path;

import javax.persistence.Embeddable;

@Embeddable
class PersistedPath {

    private String title;

    PersistedPath(Path path) {
        this.title = path.getTitle();
    }

    PersistedPath() {}

    String getTitle() {
        return title;
    }
}
