package ch.awae.moba2.control.impl;

import ch.awae.moba2.Sector;
import ch.awae.moba2.buttons.ButtonProvider;
import ch.awae.moba2.buttons.SectorButtonProvider;
import ch.awae.moba2.control.Controller;
import ch.awae.moba2.path.Path;
import ch.awae.moba2.path.PathProvider;
import ch.awae.moba2.path.PathRegistry;
import ch.awae.utils.logic.Logic;
import ch.awae.utils.logic.LogicGroup;
import org.springframework.stereotype.Component;

@Component
public class Right_A_Controller implements Controller {

    private final PathRegistry pathRegistry;

    private final Logic _A_solo, _one_trk_solo;

    private final Logic A_1, A_2, A_3, A_4;

    private final Path[] path_A_R, path_A_I, path_A_O;

    private Logic current;
    private boolean inbound;
    private long activeTime;
    private int trackID;
    private boolean processed;

    public Right_A_Controller(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
        this.pathRegistry = pathRegistry;
        SectorButtonProvider provider = buttonProvider.sector(Sector.RIGHT);
        LogicGroup paths = provider.group("path.all");
        LogicGroup tracks = provider.group("track.all");

        Logic NC = provider.group("clear.all").none();
        Logic one_pth = paths.count(1);
        Logic one_trk = tracks.count(1);

        Logic _A = provider.button("path.A").and(one_pth).and(NC);
        Logic _1 = provider.button("track[1]").and(one_trk).and(NC);
        Logic _2 = provider.button("track[2]").and(one_trk).and(NC);
        Logic _3 = provider.button("track[3]").and(one_trk).and(NC);
        Logic _4 = provider.button("track[4]").and(one_trk).and(NC);

        this._A_solo = _A.and(tracks.none());
        this._one_trk_solo = one_trk.and(NC.and(paths.none()));

        this.A_1 = _A.and(_1);
        this.A_2 = _A.and(_2);
        this.A_3 = _A.and(_3);
        this.A_4 = _A.and(_4);

        path_A_R = pathProvider.getPaths("right.A1_R", "right.A2_R", "right.A3_R", "right.A4_R");
        path_A_I = pathProvider.getPaths("right.A1_I", "right.A2_I", "right.A3_I", "right.A4_I");
        path_A_O = pathProvider.getPaths("right.A1_O", "right.A2_O", "right.A3_O", "right.A4_O");
    }

    @Override
    public void tick() {
        Logic curr = this.current;
        if (curr == null) {
            // base mode
            if (this._A_solo.evaluate())
                // only C is pressed => inbound
                this.inbound = true;
            if (this._one_trk_solo.evaluate())
                // exactly one track is pressed => outbound
                this.inbound = false;
            // check for combo
            if (this.A_1.evaluate()) {
                this.trackID = 1;
                this.current = this.A_1;
                this.activeTime = System.currentTimeMillis();
            } else if (this.A_2.evaluate()) {
                this.trackID = 2;
                this.current = this.A_2;
                this.activeTime = System.currentTimeMillis();
            } else if (this.A_3.evaluate()) {
                this.trackID = 3;
                this.current = this.A_3;
                this.activeTime = System.currentTimeMillis();
            } else if (this.A_4.evaluate()) {
                this.trackID = 4;
                this.current = this.A_4;
                this.activeTime = System.currentTimeMillis();
            }
            if (trackID >= 1 && trackID <= 4)
                pathRegistry.register(path_A_R[trackID - 1]);

        } else {
            // active mode => check if current still holds
            if (curr.evaluate()) {
                // still good => if time reached, decorate
                long deltaT = System.currentTimeMillis() - this.activeTime;
                if ((deltaT > 1000) && !this.processed) {
                    this.processed = true;
                    // decorate
                    if (trackID >= 1 && trackID <= 4)
                        pathRegistry.register((inbound ? path_A_I : path_A_O)[trackID - 1]);
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
