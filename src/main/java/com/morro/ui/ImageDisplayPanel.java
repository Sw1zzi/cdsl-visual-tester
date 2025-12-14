package com.morro.ui;

import com.morro.cdsl.interpreter.ProblemContext;
import com.morro.cdsl.model.ProblemType;
import com.morro.service.ImageGeneratorService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ImageDisplayPanel extends JPanel {
    private JPanel imageContainer;
    private JLabel imageLabel;
    private JButton deleteButton;
    private JButton regenerateButton;
    private ProblemContext problemContext;
    private BufferedImage currentImage;

    public ImageDisplayPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Generated Image"));

        // Основной контейнер для изображения
        imageContainer = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                // Соотношение 1:6 по умолчанию
                int width = getParent() != null ? getParent().getWidth() - 80 : 600;
                int height = Math.max(100, width / 6);
                return new Dimension(width, height);
            }
        };

        imageContainer.setBackground(Color.WHITE);
        imageContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Метка для изображения
        imageLabel = new JLabel("No image generated", SwingConstants.CENTER);
        imageLabel.setForeground(Color.GRAY);
        imageLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
        imageContainer.add(imageLabel, BorderLayout.CENTER);

        // Панель управления изображением (справа)
        JPanel imageControlPanel = createImageControlPanel();

        add(imageContainer, BorderLayout.CENTER);
        add(imageControlPanel, BorderLayout.EAST);
    }

    private JPanel createImageControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Кнопка удаления
        deleteButton = createIconButton("🗑", "Delete image", Color.decode("#F44336"));
        deleteButton.setToolTipText("Delete image");

        // Кнопка перегенерации
        regenerateButton = createIconButton("🔄", "Regenerate image", Color.decode("#FF9800"));
        regenerateButton.setToolTipText("Regenerate image");

        // Размер кнопок
        Dimension buttonSize = new Dimension(50, 50);
        deleteButton.setPreferredSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        deleteButton.setMinimumSize(buttonSize);

        regenerateButton.setPreferredSize(buttonSize);
        regenerateButton.setMaximumSize(buttonSize);
        regenerateButton.setMinimumSize(buttonSize);

        controlPanel.add(deleteButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(regenerateButton);

        return controlPanel;
    }

    private JButton createIconButton(String icon, String tooltip, Color color) {
        JButton button = new JButton(icon);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setToolTipText(tooltip);

        return button;
    }

    public void generateImage() {
        if (problemContext != null) {
            try {
                ImageGeneratorService generator = new ImageGeneratorService();
                currentImage = generator.generateImage(problemContext);
                displayImage(currentImage);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error generating image: " + e.getMessage(),
                        "Generation Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void regenerateImage() {
        if (problemContext != null) {
            generateImage();
        }
    }

    public void clearImage() {
        currentImage = null;
        imageLabel.setIcon(null);
        imageLabel.setText("No image generated");
        imageLabel.setForeground(Color.GRAY);
    }

    private void displayImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
        imageLabel.setText("");
    }

    private BufferedImage createPlaceholderImage() {
        int width = imageContainer.getWidth() - 20;
        int height = imageContainer.getHeight() - 20;

        if (width <= 0) width = 580;
        if (height <= 0) height = 80;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Фон
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Рамка
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(5, 5, width - 10, height - 10);

        // Текст
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Исправленная строка - используем getProblemType() вместо getTaskType()
        String taskType = problemContext.getProblemType() != null ?
                problemContext.getProblemType().getDisplayName() : "Unknown";
        String text = "Generated Image for: " + taskType;

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, (width - textWidth) / 2, height / 2);

        // Дополнительная информация
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        String info = String.format("Deck: %s (%d cards)",
                problemContext.getDeckType(), problemContext.getDeckSize());
        int infoWidth = fm.stringWidth(info);
        g2d.drawString(info, (width - infoWidth) / 2, height / 2 + 20);

        g2d.dispose();
        return image;
    }

    // Геттеры и сеттеры
    public void setProblemContext(ProblemContext context) {
        this.problemContext = context;
    }

    public ProblemContext getProblemContext() {
        return problemContext;
    }

    public void setDeleteImageListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void setRegenerateImageListener(ActionListener listener) {
        regenerateButton.addActionListener(listener);
    }
}