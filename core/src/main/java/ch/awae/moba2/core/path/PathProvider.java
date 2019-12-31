package ch.awae.moba2.core.path;

import ch.awae.moba2.common.Utils;
import ch.awae.moba2.core.Sector;
import ch.awae.moba2.core.config.YamlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

@Service
public class PathProvider {

    private final HashMap<String, Path> path_map = new HashMap<>();

    @Autowired
    private PathProvider(YamlLoader yamlLoader) throws IOException {
        loadPredefinedPaths(yamlLoader.load("paths.yml"));
    }

    public Path getPath(String key) {
        Path path = path_map.get(key);
        if (path == null) {
            throw new IllegalArgumentException("unknown path key: " + key);
        }
        return path;
    }


    private void loadPredefinedPaths(Properties properties) {
        for (Object _key : properties.keySet()) {
            if (!(_key instanceof String)) {
                continue;
            }

            String key = (String) _key;
            String value = properties.getProperty(key);

            path_map.put(key, createFromProperty(key, value));
        }
    }

    private Path createFromProperty(String key, String value) {
        String[] sections = value.split("_");
        if (sections.length != 3)
            throw new IllegalArgumentException("path definition requires 4 segments");
        // parse segments
        Sector sector = Sector.getById(Utils.parseInt(sections[0]));
        int data = Utils.parseInt(sections[1]);
        int mask = Utils.parseInt(sections[2]);
        // create path
        return new Path(key, sector, mask, data);
    }

    public Path[] getPaths(String... keys) {
        Path[] paths = new Path[keys.length];
        for (int i = 0; i < keys.length; i++) {
            paths[i] = getPath(keys[i]);
        }
        return paths;
    }
}
