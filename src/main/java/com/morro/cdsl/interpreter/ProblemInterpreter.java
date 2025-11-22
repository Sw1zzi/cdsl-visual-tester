package com.morro.cdsl.interpreter;

import com.morro.cdsl.model.Card;
import com.morro.cdsl.model.ProblemType;
import com.morro.cdsl.parser.ASTNode;

import java.util.List;

public class ProblemInterpreter {

    public static ProblemContext interpret(ASTNode ast) {
        ProblemContext context = new ProblemContext();

        for (ASTNode node : ast.getChildren()) {
            switch (node.getType()) {
                case "TASK_DECLARATION":
                    interpretTaskDeclaration(node, context);
                    break;
                case "DECK_DECLARATION":
                    interpretDeckDeclaration(node, context);
                    break;
                case "ALPHABET_DECLARATION":
                    interpretAlphabetDeclaration(node, context);
                    break;
                case "LENGTH_DECLARATION":
                    interpretLengthDeclaration(node, context);
                    break;
                case "UNIQUE_DECLARATION":
                    interpretUniqueDeclaration(node, context);
                    break;
                case "TARGET_DECLARATION":
                    interpretTargetDeclaration(node, context);
                    break;
                case "DRAW_DECLARATION":
                    interpretDrawDeclaration(node, context);
                    break;
                case "CALCULATE":
                    interpretCalculate(node, context);
                    break;
                case "CHESS_DECLARATION":
                    interpretChessDeclaration(node, context);
                    break;
                case "REMAINDERS_DECLARATION":
                    interpretRemaindersDeclaration(node, context);
                    break;
                case "DIVISIBILITY_DECLARATION":
                    interpretDivisibilityDeclaration(node, context);
                    break;
                case "BALLS_DECLARATION":
                    interpretBallsDeclaration(node, context);
                    break;
                case "EQUATIONS_DECLARATION":
                    interpretEquationsDeclaration(node, context);
                    break;
                case "NUMBERS_DECLARATION":
                    interpretNumbersDeclaration(node, context);
                    break;
            }
        }

        return context;
    }

    private static void interpretTaskDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            switch (child.getType()) {
                case "TASK_TYPE":
                    String taskType = (String) child.getValue();
                    try {
                        context.setProblemType(ProblemType.fromCdslName(taskType));
                    } catch (IllegalArgumentException e) {
                        context.setProblemType(ProblemType.CARDS);
                    }
                    break;
                case "TASK_NAME":
                    context.setTaskName((String) child.getValue());
                    break;
            }
        }
    }

    private static void interpretDeckDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            switch (child.getType()) {
                case "DECK_TYPE":
                    context.setDeckType((String) child.getValue());
                    break;
                case "DECK_SIZE":
                    context.setDeckSize((Integer) child.getValue());
                    break;
            }
        }
    }

    private static void interpretAlphabetDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            if ("ALPHABET".equals(child.getType())) {
                context.setAlphabet((String) child.getValue());
            }
        }
    }

    private static void interpretLengthDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            if ("LENGTH".equals(child.getType())) {
                context.setWordLength((Integer) child.getValue());
            }
        }
    }

    private static void interpretUniqueDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            if ("UNIQUE".equals(child.getType())) {
                context.setUniqueLetters((Boolean) child.getValue());
            }
        }
    }

    private static void interpretTargetDeclaration(ASTNode node, ProblemContext context) {
        ASTNode targetNode = node.getChildren().isEmpty() ? null : node.getChildren().get(0);

        if (targetNode != null) {
            if ("TARGET_LIST".equals(targetNode.getType())) {
                for (ASTNode itemNode : targetNode.getChildren()) {
                    interpretTargetItem(itemNode, context);
                }
            } else {
                interpretTargetItem(targetNode, context);
            }
        }
    }

    private static void interpretTargetItem(ASTNode itemNode, ProblemContext context) {
        if ("CARD".equals(itemNode.getType())) {
            Card card = interpretCard(itemNode);
            if (card != null) {
                context.getTargetCards().add(card);
                if (context.getTargetCard() == null) {
                    context.setTargetCard(card);
                }
            }
        } else if ("CONDITION".equals(itemNode.getType())) {
            String condition = interpretCondition(itemNode);
            if (condition != null) {
                context.addWordCondition(condition);
            }
        }
    }

    private static Card interpretCard(ASTNode cardNode) {
        String rank = null;
        String suit = null;

        for (ASTNode child : cardNode.getChildren()) {
            switch (child.getType()) {
                case "RANK":
                    rank = (String) child.getValue();
                    break;
                case "SUIT":
                    suit = (String) child.getValue();
                    break;
            }
        }

        if (rank != null && suit != null) {
            return new Card(rank, suit);
        }
        return null;
    }

    private static String interpretCondition(ASTNode conditionNode) {
        for (ASTNode child : conditionNode.getChildren()) {
            if ("CONDITION_TYPE".equals(child.getType())) {
                return (String) child.getValue();
            }
        }
        return null;
    }

    private static void interpretDrawDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            switch (child.getType()) {
                case "DRAW_COUNT":
                    context.setDrawCount((Integer) child.getValue());
                    break;
                case "REPLACEMENT":
                    String replacement = (String) child.getValue();
                    context.setWithReplacement("REPLACEMENT".equals(replacement));
                    break;
            }
        }
    }

    private static void interpretCalculate(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            if ("CALCULATION_TYPE".equals(child.getType())) {
                context.setCalculationType((String) child.getValue());
            }
        }
    }

    // Шахматы
    private static void interpretChessDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            switch (child.getType()) {
                case "BOARD_HEIGHT":
                    context.setBoardHeight((Integer) child.getValue());
                    break;
                case "BOARD_WIDTH":
                    context.setBoardWidth((Integer) child.getValue());
                    break;
                case "PIECE_LIST":
                    interpretPieceList(child, context);
                    break;
                case "ATTACKING_CONDITION":
                    context.setAttacking((Boolean) child.getValue());
                    break;
            }
        }
    }

    private static void interpretPieceList(ASTNode node, ProblemContext context) {
        for (ASTNode pieceNode : node.getChildren()) {
            if ("PIECE".equals(pieceNode.getType())) {
                String pieceType = null;
                Integer count = null;

                for (ASTNode child : pieceNode.getChildren()) {
                    if ("PIECE_TYPE".equals(child.getType())) {
                        pieceType = (String) child.getValue();
                    } else if ("PIECE_COUNT".equals(child.getType())) {
                        count = (Integer) child.getValue();
                    }
                }

                if (pieceType != null && count != null) {
                    context.getPieces().put(pieceType, count);
                }
            }
        }
    }

    // Остатки
    private static void interpretRemaindersDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            switch (child.getType()) {
                case "DIVIDEND":
                    context.setDividend((String) child.getValue());
                    break;
                case "DIVISOR":
                    context.setDivisor((String) child.getValue());
                    break;
                case "REMAINDER":
                    context.setRemainder((String) child.getValue());
                    break;
            }
        }
    }

    // Делимости
    private static void interpretDivisibilityDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            switch (child.getType()) {
                case "NUMBER_LENGTH":
                    context.setNumberLength((Integer) child.getValue());
                    break;
                case "TRANSFORMATION_LIST":
                    interpretTransformationList(child, context);
                    break;
                case "DIVISIBILITY_CONDITION":
                    interpretDivisibilityCondition(child, context);
                    break;
            }
        }
    }

    private static void interpretTransformationList(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            if ("TRANSFORMATION".equals(child.getType())) {
                context.getTransformations().add((String) child.getValue());
            }
        }
    }

    private static void interpretDivisibilityCondition(ASTNode node, ProblemContext context) {
        String conditionType = null;
        Integer factor = null;

        for (ASTNode child : node.getChildren()) {
            if ("CONDITION_TYPE".equals(child.getType())) {
                conditionType = (String) child.getValue();
            } else if ("FACTOR".equals(child.getType())) {
                factor = (Integer) child.getValue();
            }
        }

        if (conditionType != null) {
            String fullCondition = conditionType;
            if (factor != null) {
                fullCondition += " " + factor;
            }
            context.setDivisibilityCondition(fullCondition);
        }
    }

    // Шары и урны
    private static void interpretBallsDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            switch (child.getType()) {
                case "URN_NAME":
                    context.setAdditionalParam("urnName", child.getValue());
                    break;
                case "URN_CONTENTS":
                    interpretUrnContents(child, context);
                    break;
                case "DRAW_TYPE":
                    context.setSequentialDraw("SEQUENTIAL".equals(child.getValue()));
                    break;
                case "DRAW_COUNT":
                    context.setBallDrawCount((Integer) child.getValue());
                    break;
            }
        }
    }

    private static void interpretUrnContents(ASTNode node, ProblemContext context) {
        for (ASTNode ballNode : node.getChildren()) {
            if ("BALL_COLOR".equals(ballNode.getType())) {
                String color = null;
                Integer count = null;

                for (ASTNode child : ballNode.getChildren()) {
                    if ("COLOR".equals(child.getType())) {
                        color = (String) child.getValue();
                    } else if ("COUNT".equals(child.getType())) {
                        count = (Integer) child.getValue();
                    }
                }

                if (color != null && count != null) {
                    context.getUrnContents().put(color, count);
                }
            }
        }
    }

    // Уравнения
    private static void interpretEquationsDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            switch (child.getType()) {
                case "UNKNOWNS":
                    context.setUnknowns((Integer) child.getValue());
                    break;
                case "COEFFICIENTS_LIST":
                    interpretCoefficientsList(child, context);
                    break;
                case "SUM":
                    context.setSum((Integer) child.getValue());
                    break;
                case "DOMAIN":
                    context.setDomain((String) child.getValue());
                    break;
                case "CONSTRAINTS_LIST":
                    interpretConstraintsList(child, context);
                    break;
            }
        }
    }

    private static void interpretCoefficientsList(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            if ("COEFFICIENT".equals(child.getType())) {
                context.getCoefficients().add((Integer) child.getValue());
            }
        }
    }

    private static void interpretConstraintsList(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            if ("CONSTRAINT".equals(child.getType())) {
                context.getConstraints().add((String) child.getValue());
            }
        }
    }

    // Числа
    private static void interpretNumbersDeclaration(ASTNode node, ProblemContext context) {
        for (ASTNode child : node.getChildren()) {
            switch (child.getType()) {
                case "DIGITS":
                    context.setDigits((Integer) child.getValue());
                    break;
                case "DISTINCT":
                    context.setDistinctDigits((Boolean) child.getValue());
                    break;
                case "ADJACENT_DIFFERENT":
                    context.setAdjacentDifferent((Boolean) child.getValue());
                    break;
                case "ORDER":
                    context.setNumberOrder((String) child.getValue());
                    break;
            }
        }
    }
}