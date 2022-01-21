package net.charkosoff.utils;

import java.awt.*;
import java.io.File;

public class TrayManager {

    private String bossText;
    private String tooltip;
    private TrayIcon.MessageType notificationType;

    public TrayManager(String bossText, String tooltip) {
        this.bossText = bossText;
        this.tooltip = tooltip;
        this.notificationType = TrayIcon.MessageType.INFO;
    }

    public TrayManager(String bossText, String tooltip, TrayIcon.MessageType notificationType) {
        this.bossText = bossText;
        this.tooltip = tooltip;
        this.notificationType = notificationType;
    }

    public TrayManager display()  {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage(new File("images/icon.ico").getAbsolutePath());

        TrayIcon trayIcon = new TrayIcon(image, "Show when a boss die or spawn");
        trayIcon.setImageAutoSize(true);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        trayIcon.displayMessage(bossText, tooltip, notificationType);

        return this;
    }
}