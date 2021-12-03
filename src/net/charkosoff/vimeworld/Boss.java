package net.charkosoff.vimeworld;

public class Boss {
    private final long TIME;
    private final String NAME;
    private final String MINE;

    public Boss(String name, long time, String mine){
        this.TIME = time;
        this.NAME = name;
        this.MINE = mine;
    }

    public String getName(){
        return this.NAME;
    }

    public long getRespawn(){
        return this.TIME;
    }

    public String getMine(){
        return this.MINE;
    }

    public boolean isKill(String string){
        return string.contains(this.getName() + " был повержен") || string.contains(this.getName() + " была повержен");
    }

    public long getToRespawn(){
        return System.currentTimeMillis() + this.getRespawn();
    }
}
