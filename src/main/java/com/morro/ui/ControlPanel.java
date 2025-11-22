package com.morro.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    private JButton generateTokensButton;
    private JButton generateImageButton;

    public ControlPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        generateTokensButton = createStyledButton("Generate Tokens", Color.decode("#4CAF50"));
        generateImageButton = createStyledButton("Generate Image", Color.decode("#2196F3"));

        add(generateTokensButton);
        add(Box.createHorizontalStrut(10));
        add(generateImageButton);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFont(new Font("SansSerif", Font.BOLD, 12));

        return button;
    }

    public void setGenerateTokensListener(ActionListener listener) {
        generateTokensButton.addActionListener(listener);
    }

    public void setGenerateImageListener(ActionListener listener) {
        generateImageButton.addActionListener(listener);
    }
}
