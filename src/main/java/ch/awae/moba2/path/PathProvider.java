package ch.awae.moba2.path;

import ch.awae.moba2.Sector;
import ch.awae.moba2.Utils;
import ch.awae.moba2.config.YamlPropertiesLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

@Service
public class PathProvider {

    private final HashMap<String, Path> path_map = new HashMap<>();

    private PathProvider(YamlPropertiesLoader yamlPropertiesLoader) throws IOException {
        loadPredefinedPaths(yamlPropertiesLoader.load("paths.yml"));
    }

    public Path getPath(String key) {
        return path_map.get(key);
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
