package ch.awae.moba2.command;

import ch.awae.moba2.Sector;
import ch.awae.moba2.config.ProxyConfiguration;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log
@Service
public class CommandClient {

    private final RestTemplate http;
    private final ProxyConfiguration configuration;

    public CommandClient(RestTemplate http, ProxyConfiguration configuration) {
        this.http = http;
        this.configuration = configuration;
    }


    public void sendSwitchCommand(Sector sector, SwitchCommand switchCommand) {
        log.info(String.format("sending switch command to sector %s: %s", sector, switchCommand));
        http.postForObject(configuration.getHost() + "switch/" + sector.name(), switchCommand, Object.class);
    }
}
