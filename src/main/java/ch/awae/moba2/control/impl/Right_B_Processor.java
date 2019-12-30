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
public class Right_B_Processor implements Processor {

    private final PathRegistry pathRegistry;

    private final Logic _B_solo, _one_trk_solo;

    private final Logic B_1, B_2, B_3, B_4;

    private final Path[] path_B_R, path_B_I, path_B_O;

    private Logic current;
    private boolean inbound;
    private long activeTime;
    private int trackID;
    private boolean processed;

    @Autowired
    public Right_B_Processor(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
        this.pathRegistry = pathRegistry;
        SectorButtonProvider provider = buttonProvider.sector(Sector.RIGHT);

        LogicGroup paths = provider.group("path.all");
        LogicGroup tracks = provider.group("track.all");

        Logic NC = provider.group("clear.all").none();
        Logic one_pth = paths.count(1);
        Logic one_trk = tracks.count(1);

        Logic _B = provider.button("path.B").and(one_pth).and(NC);
        Logic _1 = provider.button("track[1]").and(one_trk).and(NC);
        Logic _2 = provider.button("track[2]").and(one_trk).and(NC);
        Logic _3 = provider.button("track[3]").and(one_trk).and(NC);
        Logic _4 = provider.button("track[4]").and(one_trk).and(NC);

        this._B_solo = _B.and(tracks.none());
        this._one_trk_solo = one_trk.and(NC.and(paths.none()));

        this.B_1 = _B.and(_1);
        this.B_2 = _B.and(_2);
        this.B_3 = _B.and(_3);
        this.B_4 = _B.and(_4);

        path_B_R = pathProvider.getPaths("right.B1_R", "right.B2_R", "right.B3_R", "right.B4_R");
        path_B_I = pathProvider.getPaths("right.B1_I", "right.B2_I", "right.B3_I", "right.B4_I");
        path_B_O = pathProvider.getPaths("right.B1_O", "right.B2_O", "right.B3_O", "right.B4_O");
    }

    @Override
    public void tick() {
        Logic curr = this.current;
        if (curr == null) {
            // base mode
            if (this._B_solo.evaluate())
                // only C is pressed => inbound
                this.inbound = true;
            if (this._one_trk_solo.evaluate())
                // exactly one track is pressed => outbound
                this.inbound = false;
            // check for combo
            if (this.B_1.evaluate()) {
                this.trackID = 1;
                this.current = this.B_1;
                this.activeTime = System.currentTimeMillis();
            } else if (this.B_2.evaluate()) {
                this.trackID = 2;
                this.current = this.B_2;
                this.activeTime = System.currentTimeMillis();
            } else if (this.B_3.evaluate()) {
                this.trackID = 3;
                this.current = this.B_3;
                this.activeTime = System.currentTimeMillis();
            } else if (this.B_4.evaluate()) {
                this.trackID = 4;
                this.current = this.B_4;
                this.activeTime = System.currentTimeMillis();
            }
            if (trackID >= 1 && trackID <= 4)
                pathRegistry.register(path_B_R[trackID - 1]);
        } else {
            // active mode => check if current still holds
            if (curr.evaluate()) {
                // still good => if time reached, decorate
                long deltaT = System.currentTimeMillis() - this.activeTime;
                if ((deltaT > 1000) && !this.processed) {
                    this.processed = true;
                    // decorate
                    if (trackID >= 1 && trackID <= 4)
                        pathRegistry.register((inbound ? path_B_I : path_B_O)[trackID - 1]);
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
