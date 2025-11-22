package com.morro.ui;

import com.morro.cdsl.tokenizer.Token;
import com.morro.cdsl.tokenizer.TokenType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TokensPanel extends JPanel {
    private JTable tokensTable;
    private DefaultTableModel tableModel;

    public TokensPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Создаем таблицу для отображения токенов
        String[] columnNames = {"Type", "Value", "Line", "Column"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tokensTable = new JTable(tableModel);

        tokensTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tokensTable.setRowHeight(20);
        tokensTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        tokensTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        tokensTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        tokensTable.getColumnModel().getColumn(3).setPreferredWidth(50);

        JScrollPane scrollPane = new JScrollPane(tokensTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void displayTokens(List<Token> tokens) {
        // Очищаем таблицу
        tableModel.setRowCount(0);

        // Добавляем токены
        for (Token token : tokens) {
            if (token.getType() != TokenType.EOF) {
                tableModel.addRow(new Object[]{
                        token.getType().toString(),
                        token.getValue(),
                        token.getLine(),
                        token.getColumn()
                });
            }
        }
    }

    public void clearTokens() {
        tableModel.setRowCount(0);
    }
}