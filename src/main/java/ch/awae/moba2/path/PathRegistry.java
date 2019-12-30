package ch.awae.moba2.path;

import ch.awae.moba2.Sector;
import ch.awae.moba2.command.CommandClient;
import ch.awae.moba2.command.SwitchCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class PathRegistry {

    private final CommandClient commandClient;
    private ArrayList<Path> paths = new ArrayList<>();

    @Autowired
    public PathRegistry(CommandClient commandClient) {
        this.commandClient = commandClient;
    }

    private void pushUpdate(Sector sector) {
        List<Path> paths = getPaths(sector);

        int data = 0;
        for (Path path : paths) {
            data += path.getData();
        }
        int display = (data >> 8) & 0x0000ffff;
        int command = data & 0x000000ff;

        commandClient.sendSwitchCommand(sector, new SwitchCommand(command, display));
    }

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
        pushUpdate(path.getSector());
    }

    public List<Path> getAllPaths() {
        return new ArrayList<>(paths);
    }

    private List<Path> getPaths(Sector sector) {
        List<Path> result = new ArrayList<>();
        for (Path p : this.paths)
            if (p.getSector() == sector)
                result.add(p);
        return result;
    }

    public void registerAll(Collection<Path> paths) {
        for (Path path : paths) {
            this.register(path);
        }
    }
}
