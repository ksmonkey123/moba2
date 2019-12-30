package ch.awae.moba2.control.impl;

import ch.awae.moba2.Sector;
import ch.awae.moba2.buttons.ButtonProvider;
import ch.awae.moba2.buttons.SectorButtonProvider;
import ch.awae.moba2.control.Processor;
import ch.awae.moba2.path.Path;
import ch.awae.moba2.path.PathProvider;
import ch.awae.moba2.path.PathRegistry;
import ch.awae.utils.logic.Logic;
import ch.awae.utils.logic.LogicGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Left_C_Processor implements Processor {

    private final PathRegistry pathRegistry;

    private final Logic _C_solo, _one_trk_solo;

    private final Logic C_1, C_2, C_3, C_4;

    private final Path[] path_R, path_I, path_O;

    private Logic   current;
    private boolean inbound;
    private long    activeTime;
    private int     trackID;
    private boolean processed;

    @Autowired
    public Left_C_Processor(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
        this.pathRegistry = pathRegistry;
        SectorButtonProvider provider = buttonProvider.sector(Sector.LEFT);

        Logic NC = provider.button("clear").not();

        LogicGroup paths = provider.group("paths");
        LogicGroup tracks = provider.group("tracks");
        Logic one_pth = paths.count(1);
        Logic one_trk = tracks.count(1);

        Logic _C = provider.button("path.C").and(one_pth).and(NC);
        Logic _1 = provider.button("track[1]").and(one_trk).and(NC);
        Logic _2 = provider.button("track[2]").and(one_trk).and(NC);
        Logic _3 = provider.button("track[3]").and(one_trk).and(NC);
        Logic _4 = provider.button("track[4]").and(one_trk).and(NC);

        this._C_solo = _C.and(tracks.none());
        this._one_trk_solo = one_trk.and(paths.none());

        this.C_1 = _C.and(_1);
        this.C_2 = _C.and(_2);
        this.C_3 = _C.and(_3);
        this.C_4 = _C.and(_4);

        this.path_R = pathProvider.getPaths("left.C1_R", "left.C2_R", "left.C3_R", "left.C4_R");
        this.path_I = pathProvider.getPaths("left.C1_I", "left.C2_I", "left.C3_I", "left.C4_I");
        this.path_O = pathProvider.getPaths("left.C1_O", "left.C2_O", "left.C3_O", "left.C4_O");
    }

    @Override
    public void tick() {
        Logic curr = this.current;
        if (curr == null) {
            // base mode
            if (this._C_solo.evaluate())
                // only C is pressed => inbound
                this.inbound = true;
            if (this._one_trk_solo.evaluate())
                // exactly one track is pressed => outbound
                this.inbound = false;
            // check for combo
            if (this.C_1.evaluate()) {
                pathRegistry.register(path_R[0]);
                this.trackID = 1;
                this.current = this.C_1;
                this.activeTime = System.currentTimeMillis();
            } else if (this.C_2.evaluate()) {
                pathRegistry.register(path_R[1]);
                this.trackID = 2;
                this.current = this.C_2;
                this.activeTime = System.currentTimeMillis();
            } else if (this.C_3.evaluate()) {
                pathRegistry.register(path_R[2]);
                this.trackID = 3;
                this.current = this.C_3;
                this.activeTime = System.currentTimeMillis();
            } else if (this.C_4.evaluate()) {
                pathRegistry.register(path_R[3]);
                this.trackID = 4;
                this.current = this.C_4;
                this.activeTime = System.currentTimeMillis();
            }
        } else {
            // active mode => check if current still holds
            if (curr.evaluate()) {
                // still good => if time reached, decorate
                long deltaT = System.currentTimeMillis() - this.activeTime;
                if ((deltaT > 1000) && !this.processed) {
                    this.processed = true;
                    // decorate
                    if (trackID >= 1 && trackID <= 4)
                    pathRegistry.register((inbound ? path_I : path_O)[trackID - 1]);
                }
            } else {
                // degraded => return to base mode
                this.trackID = 0;
                this.current = null;
                this.inbound = false;
                this.processed = false;
            }
        }
    }
}
