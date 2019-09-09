package ch.awae.moba2.path;

import ch.awae.moba2.Sector;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PathRegistry {

    private ArrayList<Path> paths = new ArrayList<>();

    public void register(Path... paths) {
        for (Path path : paths) {
            register(path);
        }
    }

    public void register(Path path) {
        if (this.paths.contains(path)) {
            return;
        }
        for (int index = 0; index < this.paths.size(); index++) {
            Path p = this.paths.get(index);
            if (path.collides(p)) {
                this.paths.remove(index);
                index--;
            }
        }
        this.paths.add(path);

    }

    public List<Path> getAllPaths() {
        return new ArrayList<>(paths);
    }

    public void unregister(Path... paths) {
        for (Path path : paths) {
            unregister(path);
        }
    }

    public void unregister(Path path) {
        this.paths.remove(path);
    }

    public List<Path> getPaths(Sector sector) {
        List<Path> result = new ArrayList<>();
        for (Path p : this.paths)
            if (p.getSector() == sector)
                result.add(p);
        return result;
    }

    public boolean isRegistered(Path p) {
        if (p == null)
            return false;
        return this.paths.contains(p);
    }

}
