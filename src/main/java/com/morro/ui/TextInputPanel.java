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
        textArea.setText(
                "// Пример 1: Карты\n" +
                        "TASK CARDS \"Probability of specific cards\"\n" +
                        "DECK STANDARD 36\n" +
                        "TARGET [ACE SPADES, KING HEARTS]\n" +
                        "DRAW 5 NO_REPLACEMENT\n" +
                        "CALCULATE PROBABILITY\n\n" +

                        "// Пример 2: Слова\n" +
                        "TASK WORDS \"Palindrome Analysis\"\n" +
                        "ALPHABET \"ABCDEFGHIJKLMNOPQRSTUVWXYZ\"\n" +
                        "LENGTH 5\n" +
                        "UNIQUE YES\n" +
                        "TARGET [PALINDROME, EQUAL_VOWELS_CONSONANTS]\n" +
                        "CALCULATE COMBINATIONS\n\n" +

                        "// Пример 3: Шахматы\n" +
                        "TASK CHESS \"Non-attacking rooks\"\n" +
                        "BOARD_HEIGHT 8\n" +
                        "BOARD_WIDTH 8\n" +
                        "PIECES [ROOK 2, KNIGHT 3]\n" +
                        "NON_ATTACKING\n" +
                        "CALCULATE COMBINATIONS\n\n"// +

//                        "// Пример 4: Остатки\n" +
//                        "TASK REMAINDERS \"Find numbers with remainder\"\n" +
//                        "DIVIDEND \"X\"\n" +
//                        "DIVISOR 7\n" +
//                        "REMAINDER 2\n" +
//                        "CALCULATE COUNT\n\n" +
//
//                        "// Пример 5: Делимости\n" +
//                        "TASK DIVISIBILITY \"Transformation divisibility\"\n" +
//                        "NUMBER_LENGTH 8\n" +
//                        "TRANSFORMATION [\"12345678\", \"87654321\"]\n" +
//                        "INCREASES_BY_FACTOR 2\n" +
//                        "CALCULATE COMBINATIONS\n\n" +
//
//                        "// Пример 6: Шары и урны\n" +
//                        "TASK BALLS \"Draw balls from urn\"\n" +
//                        "URN [RED 3, BLUE 5, GREEN 2, WHITE 1]\n" +
//                        "DRAW_SIMULTANEOUS\n" +
//                        "DRAW_COUNT 3\n" +
//                        "TARGET [RED 1, BLUE 1, WHITE 1]\n" +
//                        "CALCULATE PROBABILITY\n\n" +
//
//                        "// Пример 7: Уравнения\n" +
//                        "TASK EQUATIONS \"Solve equation with constraints\"\n" +
//                        "UNKNOWNS 4\n" +
//                        "COEFFICIENTS [1, 1, 1, 1]\n" +
//                        "SUM 25\n" +
//                        "DOMAIN \"NATURAL\"\n" +
//                        "CONSTRAINTS [\"x2 <= 2\", \"x4 > 5\"]\n" +
//                        "CALCULATE COMBINATIONS\n\n" +
//
//                        "// Пример 8: Числа\n" +
//                        "TASK NUMBERS \"Count special numbers\"\n" +
//                        "DIGITS 6\n" +
//                        "DISTINCT YES\n" +
//                        "ADJACENT_DIFFERENT YES\n" +
//                        "INCREASING\n" +
//                        "CALCULATE COMBINATIONS"
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