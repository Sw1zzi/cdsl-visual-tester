package com.morro.ui;

import javax.swing.*;
import java.awt.*;

public class TextInputPanel extends JPanel {
    private JTextArea textArea;
    private JScrollPane scrollPane;

    public TextInputPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Примеры всех типов задач
        textArea.setText("TASK CARDS \"Тест нескольких карт\"\n" +
                        "DECK STANDARD 52\n" +
                        "DRAW 3\n" +
                        "TARGET [ACE SPADES, KING HEARTS, QUEEN DIAMONDS]\n" +
                        "CALCULATE COMBINATIONS"
        );

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }
}