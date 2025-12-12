package com.morro.service;

import com.morro.cdsl.interpreter.ProblemContext;
import com.morro.cdsl.model.Card;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class CardsImageGenerator {
    private static final int IMAGE_WIDTH = 500;
    private static final int IMAGE_HEIGHT = 110;
    private static final int CARD_WIDTH = 60;
    private static final int CARD_HEIGHT = 90;

    public BufferedImage generateImage(ProblemContext context) {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Фон (зеленый как у карточного стола)
        GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 100, 0),
                IMAGE_WIDTH, IMAGE_HEIGHT, new Color(0, 70, 0));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        // Генерация карточного изображения (твой текущий код)
        drawCompactCardVisualization(g2d, context);

        g2d.dispose();
        return image;
    }

    private void drawCompactCardVisualization(Graphics2D g2d, ProblemContext context) {
        // ТВОЯ ТЕКУЩАЯ РЕАЛИЗАЦИЯ ДЛЯ КАРТ
        // Оставляем как есть
        int verticalMargin = (IMAGE_HEIGHT - CARD_HEIGHT) / 2;
        int deckStartX = 10;
        int deckStartY = 12;
        int cardSpacing = 5;
        int totalCardsWidth = 6 * CARD_WIDTH + 5 * cardSpacing;
        int resultStartX = IMAGE_WIDTH - 10 - totalCardsWidth;
        int resultStartY = verticalMargin;
        int arrowCenterX = (deckStartX + CARD_WIDTH + 4 + 10 + resultStartX - 10) / 2;
        int arrowY = deckStartY + CARD_HEIGHT / 2;

        drawCompactDeck(g2d, context.getDeckSize(), deckStartX, deckStartY);
        drawArrow(g2d, arrowCenterX, arrowY);
        drawResultCards(g2d, context, resultStartX, resultStartY);
    }

    private void drawCompactDeck(Graphics2D g2d, int deckSize, int x, int y) {
        // ТВОЯ ТЕКУЩАЯ РЕАЛИЗАЦИЯ
        g2d.setColor(new Color(30, 30, 120));
        for (int i = 0; i < 3; i++) {
            int cardOffsetX = i * 2;
            int cardOffsetY = -i * 2;
            g2d.fillRect(x + cardOffsetX, y + cardOffsetY, CARD_WIDTH, CARD_HEIGHT);
            g2d.setColor(Color.YELLOW);
            g2d.fillRect(x + cardOffsetX + 5, y + cardOffsetY + 5, CARD_WIDTH - 10, CARD_HEIGHT - 10);
            g2d.setColor(new Color(30, 30, 120));
        }

        // Размер колоды
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(String.valueOf(deckSize), x + CARD_WIDTH/2 - 5, y + CARD_HEIGHT/2 + 5);
    }

    private void drawArrow(Graphics2D g2d, int centerX, int y) {
        g2d.setColor(Color.YELLOW);
        g2d.setStroke(new BasicStroke(3));
        int arrowWidth = 20;
        g2d.drawLine(centerX - arrowWidth/2, y, centerX + arrowWidth/2, y);

        Polygon arrowHead = new Polygon();
        int headOffset = 4;
        arrowHead.addPoint(centerX + arrowWidth/2 + headOffset, y);
        arrowHead.addPoint(centerX + arrowWidth/2 - 6 + headOffset, y - 4);
        arrowHead.addPoint(centerX + arrowWidth/2 - 6 + headOffset, y + 4);
        g2d.fill(arrowHead);
    }

    private void drawResultCards(Graphics2D g2d, ProblemContext context, int x, int y) {
        int spacing = 5;
        int maxCards = Math.min(context.getDrawCount(), 6);

        List<Card> targetCards = context.getTargetCards();
        for (int i = 0; i < maxCards; i++) {
            int cardX = x + i * (CARD_WIDTH + spacing);

            // Заглушка для карт
            g2d.setColor(Color.WHITE);
            g2d.fillRect(cardX, y, CARD_WIDTH, CARD_HEIGHT);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(cardX, y, CARD_WIDTH, CARD_HEIGHT);

            if (i < targetCards.size()) {
                Card card = targetCards.get(i);
                g2d.drawString(card.getRank(), cardX + 10, y + 30);
                g2d.drawString(card.getSuit(), cardX + 10, y + 50);
            } else {
                g2d.drawString("?", cardX + CARD_WIDTH/2 - 5, y + CARD_HEIGHT/2 + 5);
            }
        }
    }
}