package com.morro.service;

import com.morro.cdsl.interpreter.ProblemContext;
import com.morro.cdsl.model.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Генератор изображений для карточных задач.
 * Создает визуализацию колоды карт и процесса вытягивания карт.
 */
public class CardsImageGenerator {
    // Список доступных изображений в папке imgs
    private ArrayList<String> availableImages = new ArrayList<>();
    private Random random = new Random();

    // Константы размеров изображения
    private static final int IMAGE_WIDTH = 500;
    private static final int IMAGE_HEIGHT = 110;
    private static final int CARD_WIDTH = 60;
    private static final int CARD_HEIGHT = 90;

    // Варианты рубашек карт
    private static final String[] CARD_BACK_VARIANTS = {
            "rubashka.png", "rubashka1.png", "rubashka2.png", "rubashka3.png", "rubashka4.png"
    };

    // Текущая выбранная рубашка
    private String currentCardBack;

    /**
     * Конструктор - сканирует доступные изображения.
     */
    public CardsImageGenerator() {
        scanAvailableImages();
    }

    /**
     * Сканирует папку с изображениями для определения доступных файлов.
     */
    private void scanAvailableImages() {
        try {
            URL resourcesUrl = getClass().getClassLoader().getResource("imgs");
            if (resourcesUrl != null) {
                java.io.File imgsDir = new java.io.File(resourcesUrl.getFile());
                if (imgsDir.exists() && imgsDir.isDirectory()) {
                    String[] files = imgsDir.list((dir, name) -> name.toLowerCase().endsWith(".png"));
                    if (files != null) {
                        availableImages = new ArrayList<>(Arrays.asList(files));
                    }
                }
            }
        } catch (Exception e) {
            // Игнорируем ошибку сканирования
        }
    }

    /**
     * Выбирает случайную рубашку карты из доступных вариантов.
     */
    private void selectRandomCardBack() {
        currentCardBack = getRandomAvailable(CARD_BACK_VARIANTS);
    }

    /**
     * Возвращает случайный доступный файл из списка вариантов.
     * @param variants массив возможных имен файлов
     * @return доступное имя файла или первый вариант по умолчанию
     */
    private String getRandomAvailable(String[] variants) {
        ArrayList<String> available = new ArrayList<>();
        for (String variant : variants) {
            if (availableImages.contains(variant)) {
                available.add(variant);
            }
        }

        if (!available.isEmpty()) {
            return available.get(random.nextInt(available.size()));
        }

        return variants[0];
    }

    /**
     * Загружает изображение из файла.
     * @param filename имя файла изображения
     * @return загруженное изображение или null при ошибке
     */
    private Image loadImage(String filename) {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/imgs/" + filename));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Возвращает изображение текущей рубашки карты.
     */
    private Image getCurrentCardBack() {
        return loadImage(currentCardBack);
    }

    /**
     * Возвращает изображение конкретной карты по ее рангу и масти.
     * @param card объект карты
     * @return изображение карты или рубашка по умолчанию
     */
    private Image getCardImage(Card card) {
        String rank = card.getRank().toUpperCase();
        String suit = card.getSuit().toUpperCase();

        String fileRank = convertRank(rank);
        String fileSuit = convertSuit(suit);

        String fileName = fileRank + "_" + fileSuit + ".png";

        Image image = loadImage(fileName);
        if (image != null) {
            return image;
        }

        // Если не нашли изображение конкретной карты, используем рубашку
        return getCurrentCardBack();
    }

    /**
     * Конвертирует английское название ранга в русское имя файла.
     * @param englishRank английское название ранга (ACE, KING, etc.)
     * @return русское имя файла для ранга
     */
    private String convertRank(String englishRank) {
        return switch (englishRank) {
            case "ACE" -> "as";
            case "KING" -> "king";
            case "QUEEN" -> "dam";
            case "JACK" -> "valet";
            default -> englishRank.toLowerCase();
        };
    }

    /**
     * Конвертирует английское название масти в русское имя файла.
     * @param englishSuit английское название масти
     * @return русское имя файла для масти
     */
    private String convertSuit(String englishSuit) {
        return switch (englishSuit) {
            case "DIAMONDS" -> "bub";
            case "HEARTS" -> "heart";
            case "SPADES" -> "pik";
            case "CLUBS" -> "tref";
            default -> englishSuit.toLowerCase();
        };
    }

    /**
     * Основной метод генерации изображения для карточной задачи.
     * @param context контекст задачи
     * @return сгенерированное изображение
     */
    public BufferedImage generateImage(ProblemContext context) {
        // Выбираем случайную рубашку для разнообразия
        selectRandomCardBack();

        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Включаем сглаживание для лучшего качества
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Рисуем фон и визуализацию
        drawBackground(g2d);
        drawVisualization(g2d, context);

        g2d.dispose();
        return image;
    }

    /**
     * Рисует унифицированный фон с карточными мастями.
     * @param g2d графический контекст
     */
    private void drawBackground(Graphics2D g2d) {
        // Используем унифицированный фон с карточными мастями
        BackgroundGenerator.drawBackground(g2d, IMAGE_WIDTH, IMAGE_HEIGHT,
                BackgroundGenerator.Style.CARDS);
    }

    /**
     * Рисует основную визуализацию: колоду, стрелку и вытянутые карты.
     * @param g2d графический контекст
     * @param context контекст задачи
     */
    private void drawVisualization(Graphics2D g2d, ProblemContext context) {
        int verticalMargin = (IMAGE_HEIGHT - CARD_HEIGHT) / 2;
        int deckStartX = 10;
        int deckStartY = 12;
        int cardSpacing = 5;

        // Определяем сколько карт показывать (не более 6)
        int drawCount = context.getDrawCount();
        int targetCardsCount = context.getTargetCards().size();
        int cardsToShow = Math.max(drawCount, targetCardsCount);
        cardsToShow = Math.min(cardsToShow, 6);

        // Рассчитываем позиции для карт
        int totalCardsWidth = cardsToShow * CARD_WIDTH + (cardsToShow - 1) * cardSpacing;
        int resultStartX = IMAGE_WIDTH - 10 - totalCardsWidth;
        int resultStartY = verticalMargin;

        // Позиции для стрелки
        int arrowStartX = deckStartX + CARD_WIDTH + 8;  // От края колоды
        int arrowEndX = resultStartX - 5;  // До начала карт (с небольшим отступом)
        int arrowY = deckStartY + CARD_HEIGHT / 2;

        // Рисуем все компоненты
        drawDeck(g2d, context.getDeckSize(), deckStartX, deckStartY);
        drawArrow(g2d, arrowStartX, arrowEndX, arrowY, cardsToShow);
        drawResultCards(g2d, context, resultStartX, resultStartY, cardsToShow, cardSpacing);
    }

    /**
     * Рисует стопку карт (колоду) с указанием размера.
     * @param g2d графический контекст
     * @param deckSize количество карт в колоде
     * @param x координата X левого верхнего угла
     * @param y координата Y левого верхнего угла
     */
    private void drawDeck(Graphics2D g2d, int deckSize, int x, int y) {
        Image cardBack = getCurrentCardBack();

        // Рисуем 3 карты со смещением для эффекта стопки
        for (int i = 0; i < 3; i++) {
            int cardOffsetX = i * 2;
            int cardOffsetY = -i * 2;

            if (cardBack != null) {
                g2d.drawImage(cardBack, x + cardOffsetX, y + cardOffsetY, CARD_WIDTH, CARD_HEIGHT, null);
            } else {
                // Запасной вариант если нет изображения рубашки
                g2d.setColor(new Color(30, 30, 120));
                g2d.fillRect(x + cardOffsetX, y + cardOffsetY, CARD_WIDTH, CARD_HEIGHT);
                g2d.setColor(Color.YELLOW);
                g2d.fillRect(x + cardOffsetX + 5, y + cardOffsetY + 5, CARD_WIDTH - 10, CARD_HEIGHT - 10);
            }
        }

        // Рисуем цифру с количеством карт в колоде
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String sizeText = String.valueOf(deckSize);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(sizeText);
        int centerX = x + CARD_WIDTH / 2 + 4;
        int centerY = y + CARD_HEIGHT / 2;
        int textX = centerX - textWidth / 2;
        int textY = centerY + fm.getAscent() / 2 - 5;

        // Фон для цифры
        g2d.setColor(new Color(0, 0, 0, 180));
        int padding = 4;
        g2d.fillRoundRect(textX - padding, textY - fm.getAscent() - padding / 2,
                textWidth + padding * 2, fm.getHeight() + padding, 5, 5);

        // Сама цифра
        g2d.setColor(Color.WHITE);
        g2d.drawString(sizeText, textX, textY);
    }

    /**
     * Рисует стрелку от колоды к вытянутым картам.
     * Длина стрелки зависит от количества карт:
     * - 6 карт: стрелка не рисуется (мало места)
     * - 1 карта: максимальная длина (400px)
     * - 2-5 карт: длина уменьшается с увеличением количества карт
     *
     * @param g2d графический контекст
     * @param startX начальная координата X (от колоды)
     * @param endX конечная координата X (до карт)
     * @param y координата Y по вертикали
     * @param cardsToShow количество показываемых карт
     */
    private void drawArrow(Graphics2D g2d, int startX, int endX, int y, int cardsToShow) {
        // Если 6 карт - не рисуем стрелку (мало места)
        if (cardsToShow == 6) {
            return;
        }

        // Черная жирная стрелка
        g2d.setColor(Color.BLACK);

        // Минимальная и максимальная длина стрелки
        int minArrowLength = 80;
        int maxArrowLength = 400;

        // Рассчитываем длину стрелки в зависимости от количества карт
        int baseLength;
        if (cardsToShow == 1) {
            // Для 1 карты - максимальная длина
            baseLength = maxArrowLength;
        } else {
            // Для 2-5 карт: длина уменьшается с увеличением количества карт
            baseLength = Math.max(minArrowLength, Math.min(maxArrowLength,
                    (IMAGE_WIDTH - startX - 20) - (cardsToShow * 15)));
        }

        // Фактическая конечная точка стрелки (не дальше чем до карт)
        int actualEndX = Math.min(endX, startX + baseLength);

        // Отступы от краев
        int lineStartX = startX + 12;
        int lineEndX = actualEndX - 28; // Оставляем место для наконечника

        // Проверяем, что стрелка имеет достаточную длину
        if (lineEndX <= lineStartX) {
            lineEndX = lineStartX + 50; // Минимальная длина
        }

        // Основная линия стрелки
        g2d.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(lineStartX, y, lineEndX, y);

        // Наконечник стрелки (отодвинут от линии)
        int tipX = lineEndX + 10; // Отодвигаем наконечник
        int tipWidth = 14;        // Ширина основания
        int tipHeight = 10;       // Высота

        // Создаем треугольник для наконечника
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(tipX, y);                     // Острие
        arrowHead.addPoint(tipX - tipWidth, y - tipHeight / 2);  // Верхняя точка основания
        arrowHead.addPoint(tipX - tipWidth, y + tipHeight / 2);  // Нижняя точка основания

        // Заполняем наконечник
        g2d.fill(arrowHead);

        // Небольшая декоративная точка в начале стрелки
        g2d.fillOval(lineStartX - 4, y - 4, 8, 8);

        // Подпись с количеством карт (если больше 0 и не 1)
        if (cardsToShow > 0 && cardsToShow != 1) {
            g2d.setFont(new Font("Arial", Font.BOLD, 11));

            // Правильное склонение для 2-5 карт
            String cardWord;
            switch (cardsToShow) {
                case 2:
                case 3:
                case 4: cardWord = "карты"; break;
                case 5: cardWord = "карт"; break;
                default: cardWord = "карт"; break;
            }

            String countText = cardsToShow + " " + cardWord;
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(countText);
            int textX = (lineStartX + lineEndX) / 2 - textWidth / 2;
            int textY = y - 10;

            // Фон для текста (полупрозрачный белый)
            g2d.setColor(new Color(255, 255, 255, 220));
            g2d.fillRoundRect(textX - 5, textY - fm.getAscent() + 3,
                    textWidth + 10, fm.getHeight(), 6, 6);

            // Текст с количеством карт
            g2d.setColor(Color.BLACK);
            g2d.drawString(countText, textX, textY);
        }
    }

    /**
     * Рисует вытянутые карты справа от стрелки.
     * Карты рисуются в случайном порядке при каждой генерации.
     *
     * @param g2d графический контекст
     * @param context контекст задачи
     * @param startX начальная координата X для карт
     * @param startY начальная координата Y для карт
     * @param cardsToShow количество карт для отображения
     * @param spacing расстояние между картами
     */
    private void drawResultCards(Graphics2D g2d, ProblemContext context, int startX, int startY,
                                 int cardsToShow, int spacing) {
        ArrayList<Card> targetCards = new ArrayList<>(context.getTargetCards());
        int targetCardsCount = targetCards.size();

        // Создаем список для изображений карт
        ArrayList<Image> cardImages = new ArrayList<>();

        // Загружаем изображения целевых карт
        for (Card card : targetCards) {
            if (cardImages.size() < cardsToShow) {
                cardImages.add(getCardImage(card));
            }
        }

        // Добиваем до нужного количества рубашками
        Image cardBack = getCurrentCardBack();
        while (cardImages.size() < cardsToShow) {
            cardImages.add(cardBack);
        }

        // ПЕРЕМЕШИВАЕМ карты для случайного порядка
        // Каждая генерация будет показывать карты в разном порядке
        Collections.shuffle(cardImages, random);

        // Рисуем карты в ряд (теперь в случайном порядке)
        for (int i = 0; i < cardImages.size(); i++) {
            int cardX = startX + i * (CARD_WIDTH + spacing);
            Image cardImage = cardImages.get(i);

            if (cardImage != null) {
                g2d.drawImage(cardImage, cardX, startY, CARD_WIDTH, CARD_HEIGHT, null);
            } else {
                // Запасной вариант если нет изображения карты
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillRect(cardX, startY, CARD_WIDTH, CARD_HEIGHT);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(cardX, startY, CARD_WIDTH, CARD_HEIGHT);
            }
        }
    }
}