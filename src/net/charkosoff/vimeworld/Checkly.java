package net.charkosoff.vimeworld;

import net.charkosoff.Main;
import net.charkosoff.utils.Logger;
import org.yaml.snakeyaml.Yaml;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Checkly {
    public static final String FILE = System.getenv("APPDATA") + "\\.vimeworld\\minigames\\logs\\latest.log";
    private static final String COPY_FILE = "</> Скопировал и поставил собственный. Если не нравится - замени, но обязательно с таким же названием.";
    public static final ConcurrentHashMap<String, Boss> BOSSES = new ConcurrentHashMap<>();

    private BossConfig bossConfig;
    private String lastString = "";

    public static Timer timer = new Timer();
    private final Logger logger = new Logger();

    public Checkly() throws IOException {
        this.header();
        this.loadConfig();
        this.checkLog();
        this.checkSound("die", "reminder", "respawn");
    }

    private void loadConfig() throws IOException {
        Yaml yaml = new Yaml();
        Path configPath = Paths.get("./config.yml");
        if (!configPath.toFile().exists()) {
            InputStream inputStream = Main.class.getResourceAsStream("/config.yml");
            Files.copy(Objects.requireNonNull(inputStream), configPath, StandardCopyOption.REPLACE_EXISTING);
        }

        bossConfig = yaml.loadAs(new FileInputStream(configPath.toFile()), BossConfig.class);
        Boss.setReminderTime(getMinute(bossConfig.getReminder_time()));
        assert bossConfig != null;
        for (Map.Entry<String, ArrayList<String>> entry : bossConfig.getBoss().entrySet()) {
            BOSSES.put(entry.getKey(),
                    new Boss(entry.getValue().get(0),
                            entry.getValue().get(1),
                            getMinute(entry.getValue().get(2)),
                            entry.getValue().get(3)));
        }
        Logger.setWithNotification(SystemTray.isSupported() && bossConfig.getNotification());
        logger.successMessage("</> Конфиг загружен, начинаю работу!");
    }

    private void header() {
        logger.infoMessage("_________ .__                   __   .__         ");
        logger.infoMessage("\\_   ___ \\|  |__   ____   ____ |  | _|  | ___.__.");
        logger.infoMessage("/    \\  \\/|  |  \\_/ __ \\_/ ___\\|  |/ /  |<   |  |");
        logger.infoMessage("\\     \\___|   Y  \\  ___/\\  \\___|    <|  |_\\___  |");
        logger.infoMessage(" \\______  /___|  /\\___  >\\___  >__|_ \\____/ ____|");
        logger.infoMessage("        \\/     \\/     \\/     \\/     \\/    \\/     \n");
        logger.successMessage("~ Для поиска боссов необходимо только зайти на один из призонов, весь остальной процесс - автоматический");
        logger.successMessage("~ При спавне, смерти и скором спавне босса - будет отправлено сообщение в программе и воспроизведен звуковой сигнал");
    }

    public void checkLog() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    BufferedReader log = getLog();
                    Object[] l = log.lines().toArray();

                    String last_line = l[l.length - 1].toString();

                    if (Objects.equals(last_line, lastString)) return;
                    lastString = last_line;

                    for (Boss boss : BOSSES.values()) {
                        if (!boss.isKill(last_line)) continue;
                        logger.killedMessage(boss);
                        playNotifySound("die.wav");

                        /*
                            Таймер для уведомления за n минут до спавна.
                            Изменяется в конфиге в параметре: reminder_time
                         */
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                logger.bossSpawnReminder(boss);
                                playNotifySound("reminder.wav");
                            }
                        }, boss.getRespawn() - Boss.getReminderTime());

                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                logger.bossSpawnedMessage(boss);
                                playNotifySound("respawn.wav");
                            }
                        }, boss.getRespawn());
                    }
                } catch (IOException e) {
                    logger.errorMessage("Произошла ошибка при выполнении. Отправьте в тему скриншот с данной ошибкой, пожалуйста");
                    e.printStackTrace();
                }
            }
        }, 0L, 50L);
    }

    private BufferedReader getLog() throws FileNotFoundException, UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(FILE), StandardCharsets.UTF_8));
    }

    private void checkSound(String... fileName) throws IOException {
        for (String s : fileName) {
            Path path = Paths.get("./" + s + ".wav");
            if (!path.toFile().exists()) {
                InputStream inputStream = Main.class.getResourceAsStream("/" + s + ".wav");
                Files.copy(Objects.requireNonNull(inputStream), path, StandardCopyOption.REPLACE_EXISTING);
                logger.infoMessage("</> Я у тебя не нашел файла с названием " + s + ". ");
                logger.infoMessage(COPY_FILE);
            }
        }
    }

    //TODO сделать в конфиге модульное отключение определенных уведомлений
    private void playNotifySound(String path) {
        if (bossConfig.getSoundnotification()) {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
                Clip clip = AudioSystem.getClip();

                clip.open(ais);
                clip.setFramePosition(0);
                clip.start();

                Thread.sleep(clip.getMicrosecondLength() / 1000);
                clip.stop();
                clip.close();
            } catch (IOException | LineUnavailableException | InterruptedException | UnsupportedAudioFileException ignored) {
            }
        }
    }

    //TODO Перенести в отдельный класс с утилитами. Этому тут не место
    private long getMinute(String minute) {
        return Long.parseLong(minute) * 60L * 1000L;
    }

    private long getMinute(Long minute) {
        return minute * 60L * 1000L;
    }
}
