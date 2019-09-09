package ch.awae.moba2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@Validated
@Component
@ConfigurationProperties("proxy")
public class ProxyConfiguration {
    private @NotNull @URL @Pattern(regexp = ".*/") String host;
}
