package net.charkosoff.utils;

import net.charkosoff.vimeworld.Boss;

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
    
    public void successMessage(String message){
        System.out.println(BLUE + message + RESET);
    }

    public void infoMessage(String message){
        System.out.println(PURPLE + message + RESET);
    }

    public void errorMessage(String message){
        System.out.println(RED + message + RESET);
    }

    public void killedMessage(Boss boss){
        SimpleDateFormat formatted = new java.text.SimpleDateFormat("HH:mm:ss");
        String formattedDate = formatted.format(new Date(boss.getToRespawn()));
        System.out.println("\n" + CYAN + "| Был убит босс: " + boss.getName() + CYAN + ". Следующий спавн: " + CYAN + formattedDate + RESET);
    }

    public void bossSpawnedMessage(Boss boss){
        System.out.println("\n" + PURPLE + "| Босс " + boss.getName() + PURPLE + " был заспавнен! Вперед на " + YELLOW + boss.getMine() + PURPLE + " за своей добычей!");
    }
}
