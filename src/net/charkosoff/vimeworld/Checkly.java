package net.charkosoff.vimeworld;

import net.charkosoff.utils.Logger;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class Checkly {
    public static String FILE = System.getenv("APPDATA") + "\\.vimeworld\\minigames\\logs\\latest.log";
    public static final ConcurrentHashMap<String, Boss> BOSSES = new ConcurrentHashMap<>();

    public static Timer timer = new Timer();

    private String lastString = "Первые две буквы в слове \"гребля\" означают групповая";
    private final Logger logger = new Logger();

    private boolean sound = false;

    public Checkly() {
        BOSSES.put("Королевский зомби", new Boss("Королевский зомби", getMinute(25), "/base"));
        BOSSES.put("Сточный слизень", new Boss("Сточный слизень", getHour(1), "/mine 6"));
        BOSSES.put("Матка", new Boss("Матка", getMinute(90), "/mine 10"));
        BOSSES.put("Йети", new Boss("Йети", getHour(3), "/mine 15"));
        BOSSES.put("Коровка из Коровёнки", new Boss("Коровка из Коровёнки" , getHour(3), "/mine 13"));
        BOSSES.put("Левиафан", new Boss("Левиафан", getMinute(150), "/village"));
        BOSSES.put("Небесный владыка", new Boss("Небесный владыка", getHour(5), "/mine 23"));
        BOSSES.put("Хранитель подводного мира", new Boss("Хранитель подводного мира", getHour(5), "/mine 18"));
        BOSSES.put("Холуй", new Boss("Холуй", getMinute(45), "/mine 5"));

        BOSSES.put("Фенрир", new Boss("Фенрир", getMinute(90), "/mine 8"));
        BOSSES.put("Все Всадники апокалипсиса", new Boss("Все Всадники апокалипсиса", getMinute(150), "/mine 9"));
        BOSSES.put("Житель края", new Boss("Житель края", getHour(4), "/mine 21"));

        //BOSSES.put("Название босса", new Boss("Название босса", *время между убийством и появлением*, "*место, где он спавнится*"));

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

        try{
            AudioSystem.getAudioInputStream(new File("./snd.wav"));
            sound = true;
        }
        catch (Exception e){
            logger.errorMessage("</> Файл со звуком уведомления не найден. Поместите его обратно рядом с программой, назвав snd.wav");
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

                    for (Boss boss : BOSSES.values()) {
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
                                    /*
                                    FloatControl vc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                                    vc.setValue(5);
                                     */
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
