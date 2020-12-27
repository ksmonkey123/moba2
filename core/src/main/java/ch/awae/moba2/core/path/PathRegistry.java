package ch.awae.moba2.core.path;

import ch.awae.moba2.common.LogHelper;
import ch.awae.moba2.core.Sector;
import ch.awae.moba2.core.command.CommandClient;
import ch.awae.moba2.core.command.SwitchCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

//@Validated
@Component
public class PathRegistry {

    private final static Logger LOG = LogHelper.getLogger();

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

    public void register(@NotEmpty Path... paths) {
        for (Path path : paths) {
            register(path);
        }
    }

    public void register(@NotNull Path path) {
        if (this.paths.contains(path)) {
            LOG.fine("path " + path + " is already registered!");
            return;
        }
        LOG.info("registering path " + path);
        for (int index = 0; index < this.paths.size(); index++) {
            Path p = this.paths.get(index);
            if (path.collides(p)) {
                LOG.fine("removing colliding path " + p);
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

    private List<Path> getPaths(@NotNull Sector sector) {
        List<Path> result = new ArrayList<>();
        for (Path p : this.paths)
            if (p.getSector() == sector)
                result.add(p);
        return result;
    }

    public void registerAll(@NotEmpty Collection<@NotNull Path> paths) {
        for (Path path : paths) {
            this.register(path);
        }
    }
}
