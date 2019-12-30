package ch.awae.mobaproxy.switches;

import ch.awae.mobaproxy.spi.SpiDataBundle;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SwitchModelTest {

    private SwitchModel model;

    @Before
    public void setup() {
        model = new SwitchModel();
    }

    @Test
    public void testDefaultValues() {
        assertEquals(0, model.getLastUpdate());

        SpiDataBundle bundle = model.getNextBundle();
        assertEquals(0, bundle.display);
        assertEquals(0, bundle.command);
    }

    @Test
    public void testCommandChangeSetsTime() throws InterruptedException {
        SwitchCommand command = new SwitchCommand();
        command.command = 12;
        command.display = 24;

        model.update(command);
        long last = model.getLastUpdate();

        Thread.sleep(10);

        command.command = 14;
        model.update(command);
        assertTrue(last < model.getLastUpdate());
    }

    @Test
    public void testDisplayChangeSetsTime() throws InterruptedException {
        SwitchCommand command = new SwitchCommand();
        command.command = 12;
        command.display = 24;

        model.update(command);
        long last = model.getLastUpdate();

        Thread.sleep(10);

        command.display = 14;
        model.update(command);
        assertTrue(last < model.getLastUpdate());
    }

    @Test
    public void testRepeatedCommandDoesNotUpdateTime() throws InterruptedException {
        SwitchCommand command = new SwitchCommand();
        command.command = 12;
        command.display = 24;

        model.update(command);
        long last = model.getLastUpdate();

        Thread.sleep(10);

        model.update(command);
        assertEquals(last, model.getLastUpdate());
    }

    @Test
    public void testOutputCorrect() {
        SwitchCommand command = new SwitchCommand();
        command.command = 12;
        command.display = 24;

        model.update(command);

        SpiDataBundle bundle = model.getNextBundle();

        assertEquals(command.command.intValue(), bundle.command);
        assertEquals(command.display.intValue(), bundle.display);
    }

}
