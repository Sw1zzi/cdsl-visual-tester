package com.morro.ui;

import com.morro.cdsl.interpreter.ProblemContext;
import com.morro.cdsl.interpreter.ProblemInterpreter;
import com.morro.cdsl.parser.ASTNode;
import com.morro.cdsl.parser.CDSLParser;
import com.morro.cdsl.tokenizer.CDSLTokenizer;
import com.morro.cdsl.tokenizer.Token;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private TextInputPanel textInputPanel;
    private TokensPanel tokensPanel;
    private ControlPanel controlPanel;
    private ImageDisplayPanel imageDisplayPanel;

    public MainFrame() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("CDSL Visual Test Environment - com.morro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Создаем панели
        textInputPanel = new TextInputPanel();
        tokensPanel = new TokensPanel();
        controlPanel = new ControlPanel();
        imageDisplayPanel = new ImageDisplayPanel();

        // Верхняя панель (ввод текста и токены)
        JPanel topPanel = createTopPanel();

        // Центральная панель (изображение)
        JPanel centerPanel = createCenterPanel();

        // Добавляем панели в окно
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Настраиваем размеры
        setPreferredSize(new Dimension(1200, 800));
        pack();
        setLocationRelativeTo(null);

        // Подписываемся на события
        setupEventListeners();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Левая панель - ввод текста
        JPanel leftTopPanel = new JPanel(new BorderLayout());
        leftTopPanel.setBorder(BorderFactory.createTitledBorder("CDSL Input"));
        leftTopPanel.add(textInputPanel, BorderLayout.CENTER);

        // Правая панель - токены
        JPanel rightTopPanel = new JPanel(new BorderLayout());
        rightTopPanel.setBorder(BorderFactory.createTitledBorder("Tokens"));
        rightTopPanel.add(tokensPanel, BorderLayout.CENTER);

        topPanel.add(leftTopPanel);
        topPanel.add(rightTopPanel);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        centerPanel.add(imageDisplayPanel, BorderLayout.CENTER);
        return centerPanel;
    }

    private void setupEventListeners() {
        controlPanel.setGenerateTokensListener(e -> generateTokens());
        controlPanel.setGenerateImageListener(e -> generateImage());
        imageDisplayPanel.setDeleteImageListener(e -> deleteImage());
        imageDisplayPanel.setRegenerateImageListener(e -> regenerateImage());
    }

    private void generateTokens() {
        try {
            String input = textInputPanel.getText();
            List<Token> tokens = CDSLTokenizer.tokenize(input);
            tokensPanel.displayTokens(tokens);

            // Парсим и интерпретируем
            CDSLParser parser = new CDSLParser(tokens);
            ASTNode ast = parser.parse();
            ProblemContext context = ProblemInterpreter.interpret(ast);

            // Сохраняем контекст для генерации изображения
            imageDisplayPanel.setProblemContext(context);

            // Показываем AST в консоли для отладки
            System.out.println("Generated AST:");
            System.out.println(ast);
            System.out.println("Problem Context: " + context);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error parsing CDSL: " + ex.getMessage(),
                    "Parse Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void generateImage() {
        ProblemContext context = imageDisplayPanel.getProblemContext();
        if (context != null) {
            imageDisplayPanel.generateImage();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please generate tokens first",
                    "No Context",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteImage() {
        imageDisplayPanel.clearImage();
    }

    private void regenerateImage() {
        imageDisplayPanel.regenerateImage();
    }
}