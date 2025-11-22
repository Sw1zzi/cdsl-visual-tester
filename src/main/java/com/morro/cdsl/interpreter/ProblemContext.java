package com.morro.cdsl.interpreter;

import com.morro.cdsl.model.Card;
import com.morro.cdsl.model.ProblemType;

import java.util.*;


public class ProblemContext {
    private ProblemType problemType;
    private String taskName;
    private String calculationType;

    // Параметры для карт
    private String deckType;
    private int deckSize;
    private Card targetCard;
    private List<Card> targetCards = new ArrayList<>();
    private int drawCount;
    private boolean withReplacement = false;

    // Параметры для слов
    private String alphabet;
    private int wordLength;
    private boolean uniqueLetters = true;
    private List<String> wordConditions = new ArrayList<>();

    // Параметры для шахмат
    private int boardHeight = 8;
    private int boardWidth = 8;
    private Map<String, Integer> pieces = new HashMap<>();
    private boolean attacking = false;

    // Параметры для остатков
    private String dividend;
    private String divisor;
    private String remainder;

    // Параметры для делимости
    private int numberLength;
    private List<String> transformations = new ArrayList<>();
    private String divisibilityCondition;

    // Параметры для шаров и урн
    private Map<String, Integer> urnContents = new HashMap<>();
    private boolean sequentialDraw = true;
    private int ballDrawCount;

    // Параметры для уравнений
    private int unknowns;
    private List<Integer> coefficients = new ArrayList<>();
    private int sum;
    private String domain;
    private List<String> constraints = new ArrayList<>();

    // Параметры для чисел
    private int digits;
    private boolean distinctDigits = false;
    private boolean adjacentDifferent = false;
    private String numberOrder;

    private Map<String, Object> additionalParams = new HashMap<>();

    // Геттеры и сеттеры для всех полей
    public ProblemType getProblemType() { return problemType; }
    public void setProblemType(ProblemType problemType) { this.problemType = problemType; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getCalculationType() { return calculationType; }
    public void setCalculationType(String calculationType) { this.calculationType = calculationType; }

    public String getDeckType() { return deckType; }
    public void setDeckType(String deckType) { this.deckType = deckType; }

    public int getDeckSize() { return deckSize; }
    public void setDeckSize(int deckSize) { this.deckSize = deckSize; }

    public Card getTargetCard() { return targetCard; }
    public void setTargetCard(Card targetCard) { this.targetCard = targetCard; }

    public List<Card> getTargetCards() { return targetCards; }
    public void setTargetCards(List<Card> targetCards) { this.targetCards = targetCards; }

    public int getDrawCount() { return drawCount; }
    public void setDrawCount(int drawCount) { this.drawCount = drawCount; }

    public boolean isWithReplacement() { return withReplacement; }
    public void setWithReplacement(boolean withReplacement) { this.withReplacement = withReplacement; }

    public String getAlphabet() { return alphabet; }
    public void setAlphabet(String alphabet) { this.alphabet = alphabet; }

    public int getWordLength() { return wordLength; }
    public void setWordLength(int wordLength) { this.wordLength = wordLength; }

    public boolean isUniqueLetters() { return uniqueLetters; }
    public void setUniqueLetters(boolean uniqueLetters) { this.uniqueLetters = uniqueLetters; }

    public List<String> getWordConditions() { return wordConditions; }
    public void setWordConditions(List<String> wordConditions) { this.wordConditions = wordConditions; }
    public void addWordCondition(String condition) { this.wordConditions.add(condition); }

    public int getBoardHeight() { return boardHeight; }
    public void setBoardHeight(int boardHeight) { this.boardHeight = boardHeight; }

    public int getBoardWidth() { return boardWidth; }
    public void setBoardWidth(int boardWidth) { this.boardWidth = boardWidth; }

    public Map<String, Integer> getPieces() { return pieces; }
    public void setPieces(Map<String, Integer> pieces) { this.pieces = pieces; }

    public boolean isAttacking() { return attacking; }
    public void setAttacking(boolean attacking) { this.attacking = attacking; }

    public String getDividend() { return dividend; }
    public void setDividend(String dividend) { this.dividend = dividend; }

    public String getDivisor() { return divisor; }
    public void setDivisor(String divisor) { this.divisor = divisor; }

    public String getRemainder() { return remainder; }
    public void setRemainder(String remainder) { this.remainder = remainder; }

    public int getNumberLength() { return numberLength; }
    public void setNumberLength(int numberLength) { this.numberLength = numberLength; }

    public List<String> getTransformations() { return transformations; }
    public void setTransformations(List<String> transformations) { this.transformations = transformations; }

    public String getDivisibilityCondition() { return divisibilityCondition; }
    public void setDivisibilityCondition(String divisibilityCondition) { this.divisibilityCondition = divisibilityCondition; }

    public Map<String, Integer> getUrnContents() { return urnContents; }
    public void setUrnContents(Map<String, Integer> urnContents) { this.urnContents = urnContents; }

    public boolean isSequentialDraw() { return sequentialDraw; }
    public void setSequentialDraw(boolean sequentialDraw) { this.sequentialDraw = sequentialDraw; }

    public int getBallDrawCount() { return ballDrawCount; }
    public void setBallDrawCount(int ballDrawCount) { this.ballDrawCount = ballDrawCount; }

    public int getUnknowns() { return unknowns; }
    public void setUnknowns(int unknowns) { this.unknowns = unknowns; }

    public List<Integer> getCoefficients() { return coefficients; }
    public void setCoefficients(List<Integer> coefficients) { this.coefficients = coefficients; }

    public int getSum() { return sum; }
    public void setSum(int sum) { this.sum = sum; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public List<String> getConstraints() { return constraints; }
    public void setConstraints(List<String> constraints) { this.constraints = constraints; }

    public int getDigits() { return digits; }
    public void setDigits(int digits) { this.digits = digits; }

    public boolean isDistinctDigits() { return distinctDigits; }
    public void setDistinctDigits(boolean distinctDigits) { this.distinctDigits = distinctDigits; }

    public boolean isAdjacentDifferent() { return adjacentDifferent; }
    public void setAdjacentDifferent(boolean adjacentDifferent) { this.adjacentDifferent = adjacentDifferent; }

    public String getNumberOrder() { return numberOrder; }
    public void setNumberOrder(String numberOrder) { this.numberOrder = numberOrder; }

    public void setAdditionalParam(String key, Object value) {
        additionalParams.put(key, value);
    }

    public Object getAdditionalParam(String key) {
        return additionalParams.get(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Task: %s '%s'",
                problemType != null ? problemType.getDisplayName() : "Unknown",
                taskName != null ? taskName : "Unnamed"));

        if (problemType != null) {
            switch (problemType) {
                case CARDS:
                    sb.append(String.format(", Deck: %s (%d cards), Draws: %d (%s), Target: %s",
                            deckType, deckSize, drawCount,
                            withReplacement ? "with replacement" : "no replacement",
                            targetCards.isEmpty() ? (targetCard != null ? targetCard : "None") : targetCards));
                    break;
                case WORDS:
                    sb.append(String.format(", Alphabet: %s, Length: %d, Unique: %s, Conditions: %s",
                            alphabet, wordLength, uniqueLetters ? "yes" : "no", wordConditions));
                    break;
                case CHESS:
                    sb.append(String.format(", Board: %dx%d, Pieces: %s, Condition: %s",
                            boardHeight, boardWidth, pieces, attacking ? "attacking" : "non-attacking"));
                    break;
                case REMAINDERS:
                    sb.append(String.format(", Dividend: %s, Divisor: %s, Remainder: %s",
                            dividend, divisor, remainder));
                    break;
                case DIVISIBILITY:
                    sb.append(String.format(", Number Length: %d, Transformations: %s, Condition: %s",
                            numberLength, transformations, divisibilityCondition));
                    break;
                case BALLS_AND_URNS:
                    sb.append(String.format(", Urn: %s, Draw: %s, Count: %d",
                            urnContents, sequentialDraw ? "sequential" : "simultaneous", ballDrawCount));
                    break;
                case EQUATIONS:
                    sb.append(String.format(", Unknowns: %d, Coefficients: %s, Sum: %d, Domain: %s, Constraints: %s",
                            unknowns, coefficients, sum, domain, constraints));
                    break;
                case NUMBERS:
                    sb.append(String.format(", Digits: %d, Distinct: %s, Adjacent Different: %s, Order: %s",
                            digits, distinctDigits ? "yes" : "no", adjacentDifferent ? "yes" : "no", numberOrder));
                    break;
            }
        }

        sb.append(String.format(", Calculate: %s",
                calculationType != null ? calculationType : "Unknown"));

        return sb.toString();
    }
}