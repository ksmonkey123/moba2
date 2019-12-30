package ch.awae.moba2.persistence;

import ch.awae.moba2.path.Path;

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
