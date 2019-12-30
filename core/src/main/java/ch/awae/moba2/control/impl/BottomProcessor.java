package ch.awae.moba2.control.impl;

import ch.awae.moba2.Sector;
import ch.awae.moba2.buttons.ButtonProvider;
import ch.awae.moba2.buttons.SectorButtonProvider;
import ch.awae.moba2.control.Processor;
import ch.awae.moba2.path.Path;
import ch.awae.moba2.path.PathProvider;
import ch.awae.moba2.path.PathRegistry;
import ch.awae.utils.logic.Logic;
import ch.awae.utils.logic.LogicCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BottomProcessor implements Processor {

    private final Logic clear;
    private final LogicCluster cluster;
    private final Path clearPath;
    private final Path[] leftPaths, rightPaths;
    private final PathRegistry pathRegistry;

    @Autowired
    public BottomProcessor(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
        this.pathRegistry = pathRegistry;

        SectorButtonProvider provider = buttonProvider.sector(Sector.BOTTOM);
        clear = provider.button("clear");
        cluster = Logic.cluster(provider.button("track[1]"), provider.button("track[2]"),
                provider.button("track[3]"), provider.button("track[4]"), provider.button("track[5]"),
                provider.button("track[6]"), provider.button("track[7]"), provider.button("track[8]"),
                provider.button("track[9]"), provider.button("track[10]"), null, null,
                provider.button("exit.left"), provider.button("exit.right"));

        leftPaths = pathProvider.getPaths("bottom.l1", "bottom.l2", "bottom.l3", "bottom.l4", "bottom.l5",
                "bottom.l6", "bottom.l7", "bottom.l8", "bottom.l9", "bottom.l10");
        rightPaths = pathProvider.getPaths("bottom.r2", "bottom.r3", "bottom.r4", "bottom.r5");
        clearPath = pathProvider.getPath("bottom.clear");
    }

    @Override
    public void tick() {
        if (clear.evaluate()) {
            pathRegistry.register(clearPath);
        }
        Path p = null;
        switch (cluster.evaluate()) {
            case 0x1001: p = leftPaths[0]; break;
            case 0x1002: p = leftPaths[1]; break;
            case 0x1004: p = leftPaths[2]; break;
            case 0x1008: p = leftPaths[3]; break;
            case 0x1010: p = leftPaths[4]; break;
            case 0x1020: p = leftPaths[5]; break;
            case 0x1040: p = leftPaths[6]; break;
            case 0x1080: p = leftPaths[7]; break;
            case 0x1100: p = leftPaths[8]; break;
            case 0x1200: p = leftPaths[9]; break;
            case 0x2002: p = rightPaths[0]; break;
            case 0x2004: p = rightPaths[1]; break;
            case 0x2008: p = rightPaths[2]; break;
            case 0x2010: p = rightPaths[3]; break;
            default: break;
        }
        if (p != null) {
            pathRegistry.register(p);
        }
    }
}
