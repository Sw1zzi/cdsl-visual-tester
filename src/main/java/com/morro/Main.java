package com.morro;

import com.morro.ui.MainFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Устанавливаем нативный look and feel для лучшего внешнего вида
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Failed to set system look and feel: " + e.getMessage());
            }

            new MainFrame().setVisible(true);
        });
    }
}