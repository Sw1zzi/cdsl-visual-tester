package com.morro.cdsl.parser;

import com.morro.cdsl.tokenizer.Token;
import com.morro.cdsl.tokenizer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class CDSLParser {
    private final List<Token> tokens;
    private int current = 0;

    public CDSLParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ASTNode parse() {
        ASTNode program = new ASTNode("PROGRAM");

        while (!isAtEnd()) {
            try {
                if (match(TokenType.TASK)) {
                    program.addChild(parseTaskDeclaration());
                } else if (match(TokenType.DECK)) {
                    program.addChild(parseDeckDeclaration());
                } else if (match(TokenType.ALPHABET) || match(TokenType.SET)) {
                    program.addChild(parseAlphabetDeclaration());
                } else if (match(TokenType.LENGTH)) {
                    program.addChild(parseLengthDeclaration());
                } else if (match(TokenType.UNIQUE) || match(TokenType.ALLOW_DUPLICATES)) {
                    program.addChild(parseUniqueDeclaration());
                } else if (match(TokenType.TARGET)) {
                    program.addChild(parseTargetDeclaration());
                } else if (match(TokenType.DRAW)) {
                    program.addChild(parseDrawDeclaration());
                } else if (match(TokenType.CONDITION)) {
                    program.addChild(parseCondition());
                } else if (match(TokenType.CALCULATE)) {
                    program.addChild(parseCalculate());
                } else if (match(TokenType.BOARD_HEIGHT) || match(TokenType.BOARD_WIDTH) ||
                        match(TokenType.PIECES) || match(TokenType.ATTACKING) || match(TokenType.NON_ATTACKING)) {
                    program.addChild(parseChessDeclaration());
                } else if (match(TokenType.DIVIDEND) || match(TokenType.DIVISOR) || match(TokenType.REMAINDER)) {
                    program.addChild(parseRemaindersDeclaration());
                } else if (match(TokenType.NUMBER_LENGTH) || match(TokenType.TRANSFORMATION) ||
                        match(TokenType.INCREASES_BY_FACTOR) || match(TokenType.DECREASES_BY_FACTOR) ||
                        match(TokenType.UNCHANGED) || match(TokenType.INCREASES_BY) || match(TokenType.DECREASES_BY)) {
                    program.addChild(parseDivisibilityDeclaration());
                } else if (match(TokenType.URN) || match(TokenType.CONTENTS) ||
                        match(TokenType.DRAW_SEQUENTIAL) || match(TokenType.DRAW_SIMULTANEOUS)) {
                    program.addChild(parseBallsDeclaration());
                } else if (match(TokenType.UNKNOWNS) || match(TokenType.COEFFICIENTS) || match(TokenType.SUM) ||
                        match(TokenType.DOMAIN) || match(TokenType.CONSTRAINTS)) {
                    program.addChild(parseEquationsDeclaration());
                } else if (match(TokenType.DIGITS) || match(TokenType.DISTINCT) || match(TokenType.ADJACENT_DIFFERENT) ||
                        match(TokenType.INCREASING) || match(TokenType.NON_DECREASING) ||
                        match(TokenType.DECREASING) || match(TokenType.NON_INCREASING)) {
                    program.addChild(parseNumbersDeclaration());
                } else {
                    Token unknown = advance();
                    System.out.println("Skipping unknown token: " + unknown.getValue() + " at line " + unknown.getLine());
                }
            } catch (Exception e) {
                System.err.println("Parse error: " + e.getMessage());
                if (!isAtEnd()) {
                    advance();
                }
            }
        }

        return program;
    }

    private ASTNode parseTaskDeclaration() {
        ASTNode node = new ASTNode("TASK_DECLARATION");

        if (match(TokenType.CARDS, TokenType.WORDS, TokenType.NUMBERS, TokenType.EQUATIONS,
                TokenType.BALLS, TokenType.DIVISIBILITY, TokenType.REMAINDERS, TokenType.CHESS)) {
            node.addChild(new ASTNode("TASK_TYPE", previous().getValue()));
        } else {
            System.err.println("Unknown task type: " + (isAtEnd() ? "EOF" : peek().getValue()));
            while (!isAtEnd() && !isNextCommand()) {
                advance();
            }
            return node;
        }

        if (match(TokenType.STRING)) {
            String taskName = previous().getValue().replaceAll("\"", "");
            node.addChild(new ASTNode("TASK_NAME", taskName));
        } else {
            node.addChild(new ASTNode("TASK_NAME", ""));
        }

        return node;
    }

    private ASTNode parseAlphabetDeclaration() {
        ASTNode node = new ASTNode("ALPHABET_DECLARATION");

        if (match(TokenType.STRING)) {
            String alphabet = previous().getValue().replaceAll("\"", "");
            node.addChild(new ASTNode("ALPHABET", alphabet));
        } else {
            node.addChild(new ASTNode("ALPHABET", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        }

        return node;
    }

    private ASTNode parseLengthDeclaration() {
        ASTNode node = new ASTNode("LENGTH_DECLARATION");

        if (match(TokenType.INTEGER)) {
            node.addChild(new ASTNode("LENGTH", Integer.parseInt(previous().getValue())));
        } else {
            node.addChild(new ASTNode("LENGTH", 5));
        }

        return node;
    }

    private ASTNode parseUniqueDeclaration() {
        ASTNode node = new ASTNode("UNIQUE_DECLARATION");

        boolean isUniqueDeclaration = previous().getType() == TokenType.UNIQUE;
        boolean uniqueValue = true;

        if (match(TokenType.BOOLEAN)) {
            String boolValue = previous().getValue().toUpperCase();
            uniqueValue = "YES".equals(boolValue) || "TRUE".equals(boolValue);
        } else if (isUniqueDeclaration) {
            uniqueValue = true;
        } else {
            uniqueValue = false;
        }

        node.addChild(new ASTNode("UNIQUE", uniqueValue));
        return node;
    }

    private ASTNode parseDeckDeclaration() {
        ASTNode node = new ASTNode("DECK_DECLARATION");

        if (match(TokenType.STANDARD, TokenType.FRENCH, TokenType.SPANISH, TokenType.CUSTOM)) {
            node.addChild(new ASTNode("DECK_TYPE", previous().getValue()));
        } else {
            node.addChild(new ASTNode("DECK_TYPE", "STANDARD"));
        }

        if (match(TokenType.INTEGER)) {
            node.addChild(new ASTNode("DECK_SIZE", Integer.parseInt(previous().getValue())));
        } else {
            node.addChild(new ASTNode("DECK_SIZE", 52));
        }

        return node;
    }

    private ASTNode parseTargetDeclaration() {
        ASTNode node = new ASTNode("TARGET_DECLARATION");

        if (match(TokenType.LBRACKET)) {
            node.addChild(parseTargetList());
        } else {
            try {
                if (checkCardComponents()) {
                    node.addChild(parseSingleCard());
                } else {
                    node.addChild(parseSingleCondition());
                }
            } catch (Exception e) {
                System.err.println("Error parsing target: " + e.getMessage());
                while (!isAtEnd() && !isNextCommand()) {
                    advance();
                }
            }
        }

        return node;
    }

    private ASTNode parseTargetList() {
        ASTNode node = new ASTNode("TARGET_LIST");

        while (!isAtEnd() && !check(TokenType.RBRACKET)) {
            try {
                if (checkCardComponents()) {
                    node.addChild(parseSingleCard());
                } else {
                    node.addChild(parseSingleCondition());
                }

                if (match(TokenType.COMMA)) {
                    continue;
                }
            } catch (Exception e) {
                System.err.println("Error parsing target in list: " + e.getMessage());
                break;
            }
        }

        if (!match(TokenType.RBRACKET)) {
            System.err.println("Expected ']' but found: " + (isAtEnd() ? "EOF" : peek().getValue()));
        }

        return node;
    }

    private ASTNode parseSingleCard() {
        ASTNode node = new ASTNode("CARD");

        String rank = null;
        if (match(TokenType.RANK, TokenType.ACE, TokenType.KING, TokenType.QUEEN, TokenType.JACK)) {
            rank = normalizeRank(previous().getValue());
        } else if (match(TokenType.INTEGER)) {
            rank = previous().getValue();
        } else {
            throw new RuntimeException("Expected card rank, found: " + (isAtEnd() ? "EOF" : peek().getValue()));
        }
        node.addChild(new ASTNode("RANK", rank));

        String suit = null;
        if (match(TokenType.HEARTS, TokenType.DIAMONDS, TokenType.CLUBS, TokenType.SPADES)) {
            suit = normalizeSuit(previous().getValue());
        } else {
            throw new RuntimeException("Expected card suit, found: " + (isAtEnd() ? "EOF" : peek().getValue()));
        }
        node.addChild(new ASTNode("SUIT", suit));

        return node;
    }

    private ASTNode parseSingleCondition() {
        ASTNode node = new ASTNode("CONDITION");

        if (match(TokenType.PALINDROME, TokenType.ALTERNATING,
                TokenType.CONSONANT_FOLLOWED_BY_VOWEL, TokenType.VOWEL_FOLLOWED_BY_CONSONANT,
                TokenType.MORE_VOWELS_THAN_CONSONANTS, TokenType.MORE_CONSONANTS_THAN_VOWELS,
                TokenType.EQUAL_VOWELS_CONSONANTS)) {
            node.addChild(new ASTNode("CONDITION_TYPE", previous().getValue()));
        } else {
            if (match(TokenType.STRING)) {
                String condition = previous().getValue().replaceAll("\"", "");
                node.addChild(new ASTNode("CONDITION_TYPE", condition));
            } else if (!isAtEnd() && !check(TokenType.RBRACKET) && !check(TokenType.COMMA)) {
                Token token = advance();
                node.addChild(new ASTNode("CONDITION_TYPE", token.getValue()));
            } else {
                throw new RuntimeException("Expected condition type");
            }
        }

        return node;
    }

    private ASTNode parseDrawDeclaration() {
        ASTNode node = new ASTNode("DRAW_DECLARATION");

        if (match(TokenType.INTEGER)) {
            node.addChild(new ASTNode("DRAW_COUNT", Integer.parseInt(previous().getValue())));
        } else {
            node.addChild(new ASTNode("DRAW_COUNT", 1));
        }

        if (match(TokenType.REPLACEMENT, TokenType.NO_REPLACEMENT)) {
            node.addChild(new ASTNode("REPLACEMENT", previous().getValue()));
        } else {
            node.addChild(new ASTNode("REPLACEMENT", "NO_REPLACEMENT"));
        }

        return node;
    }

    private ASTNode parseCondition() {
        ASTNode node = new ASTNode("CONDITION");
        StringBuilder conditionBuilder = new StringBuilder();
        while (!isAtEnd() && !check(TokenType.CALCULATE)) {
            conditionBuilder.append(previous().getValue()).append(" ");
            advance();
        }
        node.addChild(new ASTNode("CONDITION_EXPR", conditionBuilder.toString().trim()));
        return node;
    }

    private ASTNode parseCalculate() {
        ASTNode node = new ASTNode("CALCULATE");

        if (match(TokenType.PROBABILITY, TokenType.COMBINATIONS, TokenType.EXPECTATION)) {
            node.addChild(new ASTNode("CALCULATION_TYPE", previous().getValue()));
        } else {
            node.addChild(new ASTNode("CALCULATION_TYPE", "PROBABILITY"));
        }

        return node;
    }

    // Шахматы
    private ASTNode parseChessDeclaration() {
        ASTNode node = new ASTNode("CHESS_DECLARATION");

        if (previous().getType() == TokenType.BOARD_HEIGHT) {
            if (match(TokenType.INTEGER)) {
                node.addChild(new ASTNode("BOARD_HEIGHT", Integer.parseInt(previous().getValue())));
            }
        }

        if (previous().getType() == TokenType.BOARD_WIDTH || check(TokenType.BOARD_WIDTH)) {
            if (match(TokenType.BOARD_WIDTH)) {
                if (match(TokenType.INTEGER)) {
                    node.addChild(new ASTNode("BOARD_WIDTH", Integer.parseInt(previous().getValue())));
                }
            }
        }

        if (previous().getType() == TokenType.PIECES || check(TokenType.PIECES)) {
            if (match(TokenType.PIECES)) {
                if (match(TokenType.LBRACKET)) {
                    node.addChild(parsePieceList());
                }
            }
        }

        if (previous().getType() == TokenType.ATTACKING || check(TokenType.ATTACKING)) {
            if (match(TokenType.ATTACKING)) {
                node.addChild(new ASTNode("ATTACKING_CONDITION", true));
            }
        }

        if (previous().getType() == TokenType.NON_ATTACKING || check(TokenType.NON_ATTACKING)) {
            if (match(TokenType.NON_ATTACKING)) {
                node.addChild(new ASTNode("ATTACKING_CONDITION", false));
            }
        }

        return node;
    }

    private ASTNode parsePieceList() {
        ASTNode node = new ASTNode("PIECE_LIST");

        do {
            if (match(TokenType.STRING)) {
                String pieceType = previous().getValue().replaceAll("\"", "");
                if (match(TokenType.INTEGER)) {
                    int count = Integer.parseInt(previous().getValue());
                    ASTNode pieceNode = new ASTNode("PIECE");
                    pieceNode.addChild(new ASTNode("PIECE_TYPE", pieceType));
                    pieceNode.addChild(new ASTNode("PIECE_COUNT", count));
                    node.addChild(pieceNode);
                }
            }
        } while (match(TokenType.COMMA));

        if (!match(TokenType.RBRACKET)) {
            System.err.println("Expected ']' in piece list");
        }

        return node;
    }

    // Остатки
    private ASTNode parseRemaindersDeclaration() {
        ASTNode node = new ASTNode("REMAINDERS_DECLARATION");

        if (previous().getType() == TokenType.DIVIDEND) {
            if (match(TokenType.STRING) || match(TokenType.INTEGER)) {
                node.addChild(new ASTNode("DIVIDEND", previous().getValue()));
            }
        }

        if (previous().getType() == TokenType.DIVISOR || check(TokenType.DIVISOR)) {
            if (match(TokenType.DIVISOR)) {
                if (match(TokenType.INTEGER)) {
                    node.addChild(new ASTNode("DIVISOR", Integer.parseInt(previous().getValue())));
                }
            }
        }

        if (previous().getType() == TokenType.REMAINDER || check(TokenType.REMAINDER)) {
            if (match(TokenType.REMAINDER)) {
                if (match(TokenType.INTEGER)) {
                    node.addChild(new ASTNode("REMAINDER", Integer.parseInt(previous().getValue())));
                }
            }
        }

        return node;
    }

    // Делимости
    private ASTNode parseDivisibilityDeclaration() {
        ASTNode node = new ASTNode("DIVISIBILITY_DECLARATION");

        if (previous().getType() == TokenType.NUMBER_LENGTH) {
            if (match(TokenType.INTEGER)) {
                node.addChild(new ASTNode("NUMBER_LENGTH", Integer.parseInt(previous().getValue())));
            }
        }

        if (previous().getType() == TokenType.TRANSFORMATION || check(TokenType.TRANSFORMATION)) {
            if (match(TokenType.TRANSFORMATION)) {
                if (match(TokenType.LBRACKET)) {
                    node.addChild(parseTransformationList());
                }
            }
        }

        if (previous().getType() == TokenType.INCREASES_BY_FACTOR ||
                previous().getType() == TokenType.DECREASES_BY_FACTOR ||
                previous().getType() == TokenType.UNCHANGED ||
                previous().getType() == TokenType.INCREASES_BY ||
                previous().getType() == TokenType.DECREASES_BY) {

            TokenType conditionType = previous().getType();
            ASTNode conditionNode = new ASTNode("DIVISIBILITY_CONDITION");
            conditionNode.addChild(new ASTNode("CONDITION_TYPE", conditionType.toString()));

            if (conditionType == TokenType.INCREASES_BY || conditionType == TokenType.DECREASES_BY) {
                if (match(TokenType.INTEGER)) {
                    conditionNode.addChild(new ASTNode("FACTOR", Integer.parseInt(previous().getValue())));
                }
            } else if (conditionType == TokenType.INCREASES_BY_FACTOR || conditionType == TokenType.DECREASES_BY_FACTOR) {
                if (match(TokenType.INTEGER)) {
                    conditionNode.addChild(new ASTNode("FACTOR", Integer.parseInt(previous().getValue())));
                }
            }

            node.addChild(conditionNode);
        }

        return node;
    }

    private ASTNode parseTransformationList() {
        ASTNode node = new ASTNode("TRANSFORMATION_LIST");

        do {
            if (match(TokenType.STRING)) {
                String transformation = previous().getValue().replaceAll("\"", "");
                node.addChild(new ASTNode("TRANSFORMATION", transformation));
            }
        } while (match(TokenType.COMMA));

        if (!match(TokenType.RBRACKET)) {
            System.err.println("Expected ']' in transformation list");
        }

        return node;
    }

    // Шары и урны
    private ASTNode parseBallsDeclaration() {
        ASTNode node = new ASTNode("BALLS_DECLARATION");

        if (previous().getType() == TokenType.URN || check(TokenType.URN)) {
            if (match(TokenType.URN)) {
                node.addChild(new ASTNode("URN_NAME", previous().getValue()));
            }
        }

        if (previous().getType() == TokenType.CONTENTS || check(TokenType.CONTENTS)) {
            if (match(TokenType.CONTENTS)) {
                if (match(TokenType.LBRACKET)) {
                    node.addChild(parseUrnContents());
                }
            }
        }

        if (previous().getType() == TokenType.DRAW_SEQUENTIAL || check(TokenType.DRAW_SEQUENTIAL)) {
            if (match(TokenType.DRAW_SEQUENTIAL)) {
                node.addChild(new ASTNode("DRAW_TYPE", "SEQUENTIAL"));
            }
        }

        if (previous().getType() == TokenType.DRAW_SIMULTANEOUS || check(TokenType.DRAW_SIMULTANEOUS)) {
            if (match(TokenType.DRAW_SIMULTANEOUS)) {
                node.addChild(new ASTNode("DRAW_TYPE", "SIMULTANEOUS"));
            }
        }

        if (check(TokenType.DRAW_COUNT)) {
            if (match(TokenType.DRAW_COUNT)) {
                if (match(TokenType.INTEGER)) {
                    node.addChild(new ASTNode("DRAW_COUNT", Integer.parseInt(previous().getValue())));
                }
            }
        }

        return node;
    }

    private ASTNode parseUrnContents() {
        ASTNode node = new ASTNode("URN_CONTENTS");

        do {
            if (match(TokenType.STRING)) {
                String color = previous().getValue().replaceAll("\"", "");
                if (match(TokenType.INTEGER)) {
                    int count = Integer.parseInt(previous().getValue());
                    ASTNode ballNode = new ASTNode("BALL_COLOR");
                    ballNode.addChild(new ASTNode("COLOR", color));
                    ballNode.addChild(new ASTNode("COUNT", count));
                    node.addChild(ballNode);
                }
            }
        } while (match(TokenType.COMMA));

        if (!match(TokenType.RBRACKET)) {
            System.err.println("Expected ']' in urn contents");
        }

        return node;
    }

    // Уравнения
    private ASTNode parseEquationsDeclaration() {
        ASTNode node = new ASTNode("EQUATIONS_DECLARATION");

        if (previous().getType() == TokenType.UNKNOWNS) {
            if (match(TokenType.INTEGER)) {
                node.addChild(new ASTNode("UNKNOWNS", Integer.parseInt(previous().getValue())));
            }
        }

        if (previous().getType() == TokenType.COEFFICIENTS || check(TokenType.COEFFICIENTS)) {
            if (match(TokenType.COEFFICIENTS)) {
                if (match(TokenType.LBRACKET)) {
                    node.addChild(parseCoefficientsList());
                }
            }
        }

        if (previous().getType() == TokenType.SUM || check(TokenType.SUM)) {
            if (match(TokenType.SUM)) {
                if (match(TokenType.INTEGER)) {
                    node.addChild(new ASTNode("SUM", Integer.parseInt(previous().getValue())));
                }
            }
        }

        if (previous().getType() == TokenType.DOMAIN || check(TokenType.DOMAIN)) {
            if (match(TokenType.DOMAIN)) {
                if (match(TokenType.STRING)) {
                    node.addChild(new ASTNode("DOMAIN", previous().getValue().replaceAll("\"", "")));
                }
            }
        }

        if (previous().getType() == TokenType.CONSTRAINTS || check(TokenType.CONSTRAINTS)) {
            if (match(TokenType.CONSTRAINTS)) {
                if (match(TokenType.LBRACKET)) {
                    node.addChild(parseConstraintsList());
                }
            }
        }

        return node;
    }

    private ASTNode parseCoefficientsList() {
        ASTNode node = new ASTNode("COEFFICIENTS_LIST");

        do {
            if (match(TokenType.INTEGER)) {
                node.addChild(new ASTNode("COEFFICIENT", Integer.parseInt(previous().getValue())));
            }
        } while (match(TokenType.COMMA));

        if (!match(TokenType.RBRACKET)) {
            System.err.println("Expected ']' in coefficients list");
        }

        return node;
    }

    private ASTNode parseConstraintsList() {
        ASTNode node = new ASTNode("CONSTRAINTS_LIST");

        do {
            if (match(TokenType.STRING)) {
                String constraint = previous().getValue().replaceAll("\"", "");
                node.addChild(new ASTNode("CONSTRAINT", constraint));
            }
        } while (match(TokenType.COMMA));

        if (!match(TokenType.RBRACKET)) {
            System.err.println("Expected ']' in constraints list");
        }

        return node;
    }

    // Числа
    private ASTNode parseNumbersDeclaration() {
        ASTNode node = new ASTNode("NUMBERS_DECLARATION");

        if (previous().getType() == TokenType.DIGITS) {
            if (match(TokenType.INTEGER)) {
                node.addChild(new ASTNode("DIGITS", Integer.parseInt(previous().getValue())));
            }
        }

        if (previous().getType() == TokenType.DISTINCT || check(TokenType.DISTINCT)) {
            if (match(TokenType.DISTINCT)) {
                if (match(TokenType.BOOLEAN)) {
                    String boolValue = previous().getValue().toUpperCase();
                    node.addChild(new ASTNode("DISTINCT", "YES".equals(boolValue) || "TRUE".equals(boolValue)));
                } else {
                    node.addChild(new ASTNode("DISTINCT", true));
                }
            }
        }

        if (previous().getType() == TokenType.ADJACENT_DIFFERENT || check(TokenType.ADJACENT_DIFFERENT)) {
            if (match(TokenType.ADJACENT_DIFFERENT)) {
                if (match(TokenType.BOOLEAN)) {
                    String boolValue = previous().getValue().toUpperCase();
                    node.addChild(new ASTNode("ADJACENT_DIFFERENT", "YES".equals(boolValue) || "TRUE".equals(boolValue)));
                } else {
                    node.addChild(new ASTNode("ADJACENT_DIFFERENT", true));
                }
            }
        }

        if (previous().getType() == TokenType.INCREASING ||
                previous().getType() == TokenType.NON_DECREASING ||
                previous().getType() == TokenType.DECREASING ||
                previous().getType() == TokenType.NON_INCREASING) {

            node.addChild(new ASTNode("ORDER", previous().getValue()));
        }

        return node;
    }

    // Вспомогательные методы
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean checkCardComponents() {
        int savePos = current;
        try {
            return (check(TokenType.RANK) || check(TokenType.ACE) || check(TokenType.KING) ||
                    check(TokenType.QUEEN) || check(TokenType.JACK) || check(TokenType.INTEGER)) &&
                    (checkNext(TokenType.HEARTS) || checkNext(TokenType.DIAMONDS) ||
                            checkNext(TokenType.CLUBS) || checkNext(TokenType.SPADES));
        } finally {
            current = savePos;
        }
    }

    private boolean checkNext(TokenType type) {
        if (current + 1 >= tokens.size()) return false;
        return tokens.get(current + 1).getType() == type;
    }

    private boolean isNextCommand() {
        return check(TokenType.DECK) || check(TokenType.ALPHABET) || check(TokenType.LENGTH) ||
                check(TokenType.UNIQUE) || check(TokenType.TARGET) || check(TokenType.DRAW) ||
                check(TokenType.CONDITION) || check(TokenType.CALCULATE) ||
                check(TokenType.BOARD_HEIGHT) || check(TokenType.BOARD_WIDTH) || check(TokenType.PIECES) ||
                check(TokenType.DIVIDEND) || check(TokenType.DIVISOR) || check(TokenType.REMAINDER) ||
                check(TokenType.NUMBER_LENGTH) || check(TokenType.TRANSFORMATION) ||
                check(TokenType.URN) || check(TokenType.CONTENTS) ||
                check(TokenType.UNKNOWNS) || check(TokenType.COEFFICIENTS) || check(TokenType.SUM) ||
                check(TokenType.DIGITS) || check(TokenType.DISTINCT);
    }

    private String normalizeRank(String rank) {
        return switch (rank.toUpperCase()) {
            case "A" -> "ACE";
            case "K" -> "KING";
            case "Q" -> "QUEEN";
            case "J" -> "JACK";
            default -> rank;
        };
    }

    private String normalizeSuit(String suit) {
        return switch (suit.toUpperCase()) {
            case "H" -> "HEARTS";
            case "D" -> "DIAMONDS";
            case "C" -> "CLUBS";
            case "S" -> "SPADES";
            default -> suit;
        };
    }
}