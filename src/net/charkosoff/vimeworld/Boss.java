package net.charkosoff.vimeworld;

public class Boss {
    private final long TIME;
    private final String NAME;
    private final String MINE;
    private final String COLOR;

    public Boss(String name, long time, String mine, String color) {
        this.TIME = time;
        this.NAME = name;
        this.MINE = mine;
        this.COLOR = color;
    }

    public String getName() {
        return this.NAME;
    }

    public long getRespawn() {
        return this.TIME;
    }

    public String getMine() {
        return this.MINE;
    }

    public String getColor() {
        return this.COLOR;
    }

    public boolean isKill(String string) {
        return string.contains(this.getName() + " был повержен") || string.contains(this.getName() + " была повержен") || string.contains(this.getName() + " были повержены");
    }

    public long getToRespawn() {
        return System.currentTimeMillis() + this.getRespawn();
    }
}
