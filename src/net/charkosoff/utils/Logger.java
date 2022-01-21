package net.charkosoff.utils;

import net.charkosoff.vimeworld.Boss;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    private static TrayManager trayManager;
    private static boolean withNotification;

    public void successMessage(String message) {
        System.out.println(BLUE + message + RESET);
    }

    public void infoMessage(String message) {
        System.out.println(PURPLE + message + RESET);
    }

    public void errorMessage(String message) {
        System.out.println(RED + message + RESET);
    }

    public void killedMessage(Boss boss) {
        SimpleDateFormat formatted = new SimpleDateFormat("HH:mm");
        String formattedDate = formatted.format(new Date(boss.getToRespawn()));

        System.out.println("\n" + CYAN + "| Был убит босс: '" + PURPLE + boss.getCustomName() + CYAN + "'. Следующий спавн в: " + RED + formattedDate + RESET);
        if (withNotification) {
            trayManager = new TrayManager
                    ("Умер босс '" + boss.getCustomName() + "'", "Следующий его спавн будет в: " + formattedDate).display();
        }
    }

    public void bossSpawnedMessage(Boss boss) {
        System.out.println("\n"
                + CYAN + "| Босс '"
                + PURPLE + boss.getCustomName()
                + CYAN + "' был заспавнен! Вперед на '"
                + YELLOW + boss.getMine()
                + CYAN + "' за своей добычей!");

        if (withNotification) {
            trayManager = new TrayManager
                    ("Заспавнился '" + boss.getCustomName() + "'",
                            "Бегом на " + boss.getMine()).display();
        }
    }

    //TODO Добавить плуралсы для времени
    public void bossSpawnReminder(Boss boss) {
        System.out.println("\n"
                + CYAN + "| Босс '"
                + PURPLE + boss.getCustomName()
                + CYAN + "' заспавнтся через '"
                + RED + Boss.getReminderTime() / 1000 / 60
                + CYAN + "' минут. Можешь начинать подготовку и идти на -> "
                + YELLOW + boss.getMine());

        if (withNotification) {
            trayManager = new TrayManager
                    ("Босс '" + boss.getCustomName() + "'",
                            "Заспавнится через " + Boss.getReminderTime() / 1000 / 60 + "минут.\n Бегом на " + boss.getMine()).display();
        }
    }

    public static void setWithNotification(boolean withNotification) {
        Logger.withNotification = withNotification;
    }
}

