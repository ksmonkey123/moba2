package ch.awae.mobaproxy;

public class DaemonThread extends Thread {

    public DaemonThread(String name, Runnable code) {
        super(code);
        setName(name);
        setDaemon(true);
    }

}
