package net.charkosoff.vimeworld;

import java.util.ArrayList;
import java.util.HashMap;

public class BossConfig {

    private HashMap<String, ArrayList<String>> boss;
    private Boolean notification;
    private Boolean soundnotification;
    private Long reminder_time;

    public HashMap<String, ArrayList<String>> getBoss() {
        return boss;
    }

    public void setBoss(HashMap<String, ArrayList<String>> boss) {
        this.boss = boss;
    }

    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    public Long getReminder_time() {
        return reminder_time;
    }

    public Boolean getSoundnotification() {
        return soundnotification;
    }

    public void setSoundnotification(Boolean soundnotification) {
        this.soundnotification = soundnotification;
    }

    public void setReminder_time(Long reminder_time) {
        this.reminder_time = reminder_time;
    }

    @Override
    public String toString() {
        return boss.toString();
    }
}
