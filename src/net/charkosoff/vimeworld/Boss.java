package net.charkosoff.vimeworld;

public class Boss {

    private final String name;
    private final String customName;
    private final long time;
    private final String mine;
    private static Long reminderTime;

    public Boss(String name, String customName, Long time, String mine){
        this.name = name;
        this.customName = customName;
        this.time = time;
        this.mine = mine;
    }

    public String getName(){
        return this.name;
    }

    public long getRespawn(){
        return this.time;
    }

    public String getMine(){
        return this.mine;
    }

    public boolean isKill(String string) {
        return string.contains(this.getName() + " был повержен")
                || string.contains(this.getName() + " была повержен")
                || string.contains(this.getName() + " были повержены");
    }

    public static void setReminderTime(Long time) {
        reminderTime = time;
    }

    public static long getReminderTime() {
        return reminderTime;
    }

    public long getToRespawn(){
        return System.currentTimeMillis() + this.getRespawn();
    }

    public String getCustomName() {
        return this.customName;
    }
}
