package ch.awae.moba2.core.config;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Service
public final class YamlLoader {

    public Properties load(String file) throws IOException {
        List<PropertySource<?>> yml = new YamlPropertySourceLoader().load(null, new ClassPathResource(file));

        Properties properties = new Properties();
        for (PropertySource<?> raw_source : yml) {
            if (raw_source instanceof MapPropertySource) {
                MapPropertySource source = (MapPropertySource) raw_source;
                String[] keys = source.getPropertyNames();
                for (String key : keys) {
                    Object raw_value = source.getProperty(key);
                    properties.setProperty(key, raw_value != null ? raw_value.toString() : null);
                }
            } else {
                throw new UnsupportedOperationException("can only load map property sources");
            }
        }
        return properties;
    }

}
