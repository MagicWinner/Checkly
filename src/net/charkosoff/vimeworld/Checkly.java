package net.charkosoff.vimeworld;

import net.charkosoff.utils.Logger;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Checkly {
    public static String FILE = System.getenv("APPDATA") + "\\.vimeworld\\minigames\\logs\\latest.log";
    public static final Boss[] BOSSES = new Boss[12];

    public static Timer timer = new Timer();

    private String lastString = "";
    private final Logger logger = new Logger();

    private boolean sound = false;

    public Checkly() {
        BOSSES[0] = new Boss("Королевский зомби", getMinute(25), "/base", "&7");
        BOSSES[1] = new Boss("Холуй", getMinute(45), "/mine 5", "&9");
        BOSSES[2] = new Boss("Сточный слизень", getHour(1), "/mine 6", "&a");
        BOSSES[3] = new Boss("Фенрир", getMinute(90), "/mine 8", "&c");
        BOSSES[4] = new Boss("Все Всадники апокалипсиса", getMinute(150), "/mine 9", "&7");
        BOSSES[5] = new Boss("Матка", getMinute(90), "/mine 10", "&2");
        BOSSES[6] = new Boss("Коровка из Коровёнки", getHour(3), "/mine 13", "&d");
        BOSSES[7] = new Boss("Йети", getHour(3), "/mine 15", "&b");
        BOSSES[8] = new Boss("Левиафан", getMinute(150), "/village", "&6");
        BOSSES[9] = new Boss("Хранитель подводного мира", getHour(5), "/mine 18", "&3");
        BOSSES[10] = new Boss("Житель края", getHour(4), "/mine 21", "&5");
        BOSSES[11] = new Boss("Небесный владыка", getHour(5), "/mine 23", "&f");

        this.header();
        this.checkLog();
    }

    private long getHour(double hour) {
        return (long) hour * 60L * 60L * 1000L;
    }

    private long getMinute(double minute) {
        return (long) minute * 60L * 1000L;
    }

    private void header() {
        logger.infoMessage("_________ .__                   __   .__         ");
        logger.infoMessage("\\_   ___ \\|  |__   ____   ____ |  | _|  | ___.__.");
        logger.infoMessage("/    \\  \\/|  |  \\_/ __ \\_/ ___\\|  |/ /  |<   |  |");
        logger.infoMessage("\\     \\___|   Y  \\  ___/\\  \\___|    <|  |_\\___  |");
        logger.infoMessage(" \\______  /___|  /\\___  >\\___  >__|_ \\____/ ____|");
        logger.infoMessage("        \\/     \\/     \\/     \\/     \\/    \\/     ");

        logger.infoMessage("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        logger.infoMessage("~~~~~~~~~~~~~~ Coded by CharkosOff ~~~~~~~~~~~~~~");
        logger.infoMessage("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        logger.successMessage("~ Для поиска боссов необходимо только зайти на один из призонов, весь остальной процесс - автоматический");
        logger.successMessage("~ При спавне босса - будет отправлено сообщение в программе и воспроизведен звуковой сигнал");
        logger.successMessage("~ Изменить его можно, скопировав любой wav файл рядом с программой и назвав его snd.wav\n");

        if(!checkSound()) logger.errorMessage("</> Файл со звуком уведомления не найден. Необходимо поместить его рядом с программой, переименовав как snd.wav");
    }

    private boolean checkSound(){
        try{
            AudioSystem.getAudioInputStream(new File("./snd.wav"));
            sound = true;

            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public void checkLog() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    BufferedReader log = getLog();
                    Object[] l = log.lines().toArray();

                    String last_line = l[l.length-1].toString();

                    if (Objects.equals(last_line, lastString)) return;
                    lastString = last_line;

                    for (Boss boss : BOSSES) {
                        if (!boss.isKill(last_line)) continue;
                        logger.killedMessage(boss);

                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                logger.bossSpawnedMessage(boss);
                                if(!sound) return;

                                try {
                                    File soundFile = new File("./snd.wav");

                                    AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);

                                    Clip clip = AudioSystem.getClip();

                                    clip.open(ais);
                                    clip.setFramePosition(0);
                                    clip.start();

                                    Thread.sleep(clip.getMicrosecondLength() / 1000);
                                    clip.stop();
                                    clip.close();
                                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ignored) {}
                            }
                        }, boss.getRespawn());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0L, 50L);
    }

    private BufferedReader getLog() throws FileNotFoundException, UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(FILE), StandardCharsets.UTF_8));
    }
}
