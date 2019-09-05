package ch.awae.moba2.config;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ConfigurationLoader {

    public <T> T loadResource(Resource res, String prefix, Class<T> clazz) throws IOException {
        List<PropertySource<?>> yml = new YamlPropertySourceLoader().load(null, res);
        Iterable<ConfigurationPropertySource> propertySources = ConfigurationPropertySources.from(yml);
        return new Binder(propertySources).bind(prefix, clazz).get();
    }

}
