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
public class Left_AB_Controller implements Controller {

    private final PathRegistry pathRegistry;

    private final Logic A_1, B_1, B_2;
    private final Path  A_1_R, B_1_R, B_2_R;

    public Left_AB_Controller(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
        this.pathRegistry = pathRegistry;
        SectorButtonProvider provider = buttonProvider.sector(Sector.LEFT);

        Logic one_pth = provider.group("paths").count(1);
        Logic one_trk = provider.group("tracks").count(1);

        Logic NC = provider.button("clear").not();

        Logic _A = provider.button("path.A").and(one_pth).and(NC);
        Logic _B = provider.button("path.B").and(one_pth).and(NC);
        Logic _1 = provider.button("track[1]").and(one_trk).and(NC);
        Logic _2 = provider.button("track[2]").and(one_trk).and(NC);

        this.A_1 = _A.and(_1);
        this.B_1 = _B.and(_1);
        this.B_2 = _B.and(_2);

        this.A_1_R = pathProvider.getPath("left.A1_R");
        this.B_1_R = pathProvider.getPath("left.B1_R");
        this.B_2_R = pathProvider.getPath("left.B2_R");
    }

    @Override
    public void tick() {
        A_1.test(() -> pathRegistry.register(A_1_R));
        B_1.test(() -> pathRegistry.register(B_1_R));
        B_2.test(() -> pathRegistry.register(B_2_R));
    }
}
