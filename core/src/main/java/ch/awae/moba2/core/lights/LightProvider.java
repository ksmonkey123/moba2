package ch.awae.moba2.core.lights;

import ch.awae.moba2.core.config.ProviderConfiguration;
import ch.awae.moba2.core.config.YamlLoader;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Validated
@Service
public class LightProvider {

    private final LightModel model;
    private final Properties properties;

    @Autowired
    public LightProvider(LightModel model, YamlLoader loader, ProviderConfiguration config) throws IOException {
        this.model = model;
        this.properties = loader.load(config.getLight());
    }

    public Light getLight(
            @Range(min = 0, max = 15) int chip,
            @Range(min = 0, max = 7) int pin) {
        return new LightProxy("#" + chip + "_" + pin, new LightImpl(chip, pin, model));
    }

    public Light getLight(@NotEmpty String id) {
        List<Light> lights = this.properties.stringPropertyNames()
                .stream()
                .filter(s -> s.startsWith(id))
                .map(this.properties::getProperty)
                .flatMap(this::createLights)
                .collect(Collectors.toList());
        if (lights.size() < 2) {
            return new LightProxy(id, lights.get(0));
        }
        return new LightProxy(id, new LightGroup(lights.toArray(new Light[0])));
    }

    private Stream<Light> createLights(String property) {
        List<Light> lights = new ArrayList<>();
        if ("*:*".equals(property)) {
            for (int chip = 0; chip < 16; chip++) {
                for (int pin = 0; pin < 8; pin++) {
                    lights.add(new LightImpl(chip, pin, model));
                }
            }
        } else {
            String[] sections = property.split(":");
            if (sections.length != 2) {
                throw new IllegalArgumentException("bad part: " + property);
            }
            int chip = Integer.parseInt(sections[0]);

            if ("*".equals(sections[1])) {
                for (int pin = 0; pin < 8; pin++) {
                    lights.add(new LightImpl(chip, pin, model));
                }
            } else {
                int pin = Integer.parseInt(sections[1]);
                lights.add(new LightImpl(chip, pin, model));
            }
        }
        return lights.stream();
    }

}
