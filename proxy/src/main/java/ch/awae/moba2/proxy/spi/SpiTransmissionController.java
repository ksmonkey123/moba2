package ch.awae.moba2.proxy.spi;

import ch.awae.moba2.proxy.DaemonThread;
import ch.awae.moba2.proxy.Sector;
import ch.awae.moba2.proxy.model.ModelProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class SpiTransmissionController {

    private final SpiService spiService;
    private final ModelProxy modelProxy;

    private final Thread thread;
    private final int maxRestore;

    private final Record[] records;

    @Autowired
    public SpiTransmissionController(
            SpiService spiService,
            ModelProxy modelProxy,
            SpiConfiguration spiConfiguration) {
        this.spiService = spiService;
        this.modelProxy = modelProxy;
        this.maxRestore = spiConfiguration.getMaxRestore();

        this.thread = new DaemonThread("spiLoop", this::run);

        records = new Record[Sector.values().length];
        Sector[] values = Sector.values();
        for (int i = 0; i < values.length; i++) {
            Sector sector = values[i];
            records[i] = new Record(sector, maxRestore);
        }
    }

    @PostConstruct
    public void setup() {
        thread.start();
    }

    private void run() {
        while (!Thread.interrupted()) {
            for (Record record : records) {
                Sector sector = record.sector;
                SpiDataBundle bundle = modelProxy.getCommandBundle(sector);
                int input = spiService.update(sector.channel, bundle.command, bundle.display);
                record.sent.incrementAndGet();
                if (input >= 0) {
                    record.received.incrementAndGet();
                    int instability = record.failedSince.getAndUpdate(i -> i > 0 ? i - 1 : i);
                    if (instability == 0) {
                        modelProxy.updateSector(sector, (short) (input & 0x0000ffff));
                    }
                } else {
                    record.failedSince.getAndUpdate(i -> i < maxRestore ? i + 1 : i);
                }
            }
        }
    }

    public HealthInfo getHealthInfo() {
        Map<Sector, Record> map = new HashMap<>();
        for (Record record : records) {
            map.put(record.sector, record);
        }
        return new HealthInfo(map);
    }

    private static class Record {

        private final Sector sector;
        private final AtomicInteger failedSince;

        public final AtomicLong sent = new AtomicLong(0L);
        public final AtomicLong received = new AtomicLong(0L);

        Record(Sector sector, int maxRestore) {
            this.sector = sector;
            this.failedSince = new AtomicInteger(maxRestore);
        }

        public boolean isHealthy() {
            return failedSince.get() == 0;
        }

        @Override
        public String toString() {
            return "Record{" +
                    "sector=" + sector +
                    ", failedSince=" + failedSince +
                    ", sent=" + sent +
                    ", received=" + received +
                    '}';
        }
    }

    public static class HealthInfo {
        private final Map<Sector, Record> records;

        @Override
        public String toString() {
            return "HealthInfo{" +
                    "records=" + records +
                    '}';
        }

        private HealthInfo(Map<Sector, Record> records) {
            this.records = records;
        }

        public Map<Sector, Record> getRecords() {
            return records;
        }
    }
}
