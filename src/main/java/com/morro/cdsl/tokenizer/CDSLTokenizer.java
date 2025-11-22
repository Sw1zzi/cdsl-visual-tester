package com.morro.cdsl.tokenizer;

import java.util.*;
import java.util.regex.*;

public class CDSLTokenizer {

    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        if (input == null || input.trim().isEmpty()) {
            return tokens;
        }

        String[] lines = input.split("\n");
        int lineNumber = 1;

        for (String line : lines) {
            tokens.addAll(tokenizeLine(line.trim(), lineNumber));
            lineNumber++;
        }

        if (!tokens.isEmpty()) {
            tokens.add(new Token(TokenType.EOF, "", lineNumber, 1));
        }
        return tokens;
    }

    private static List<Token> tokenizeLine(String line, int lineNumber) {
        List<Token> tokens = new ArrayList<>();
        if (line.isEmpty()) return tokens;

        String upperLine = line.toUpperCase();
        int pos = 0;
        int column = 1;

        while (pos < upperLine.length()) {
            boolean matched = false;

            // Пропускаем пробелы
            if (Character.isWhitespace(upperLine.charAt(pos))) {
                pos++;
                column++;
                continue;
            }

            // Сначала проверяем строки в кавычках
            if (upperLine.charAt(pos) == '"') {
                int endQuote = upperLine.indexOf('"', pos + 1);
                if (endQuote != -1) {
                    String stringValue = upperLine.substring(pos, endQuote + 1);
                    tokens.add(new Token(TokenType.STRING, stringValue, lineNumber, column));
                    pos = endQuote + 1;
                    column += stringValue.length();
                    matched = true;
                    continue;
                }
            }

            // Затем проверяем числа
            if (Character.isDigit(upperLine.charAt(pos))) {
                int start = pos;
                while (pos < upperLine.length() && Character.isDigit(upperLine.charAt(pos))) {
                    pos++;
                }
                String number = upperLine.substring(start, pos);
                tokens.add(new Token(TokenType.INTEGER, number, lineNumber, column));
                column += number.length();
                matched = true;
                continue;
            }

            // Сначала проверяем многословные токены (более длинные шаблоны)
            List<TokenType> orderedTokenTypes = getOrderedTokenTypes();

            for (TokenType tokenType : orderedTokenTypes) {
                if (tokenType == TokenType.UNKNOWN || tokenType == TokenType.STRING || tokenType == TokenType.INTEGER) {
                    continue;
                }

                Pattern pattern = Pattern.compile("^" + tokenType.getPattern());
                Matcher matcher = pattern.matcher(upperLine.substring(pos));

                if (matcher.find()) {
                    String value = matcher.group();
                    // Проверяем, что это отдельное слово
                    if (isWholeWord(value, upperLine, pos)) {
                        tokens.add(new Token(tokenType, value, lineNumber, column));
                        pos += value.length();
                        column += value.length();
                        matched = true;
                        break;
                    }
                }
            }

            if (!matched) {
                // Проверяем отдельные символы и короткие токены
                matched = checkSingleCharacters(upperLine, pos, tokens, lineNumber, column);
                if (matched) {
                    pos++;
                    column++;
                    continue;
                }

                // Неизвестный токен
                int start = pos;
                while (pos < upperLine.length() && !Character.isWhitespace(upperLine.charAt(pos)) &&
                        upperLine.charAt(pos) != '[' && upperLine.charAt(pos) != ']' &&
                        upperLine.charAt(pos) != ',' && upperLine.charAt(pos) != '(' &&
                        upperLine.charAt(pos) != ')') {
                    pos++;
                }
                if (start < pos) {
                    String unknown = upperLine.substring(start, pos);
                    tokens.add(new Token(TokenType.UNKNOWN, unknown, lineNumber, column));
                    column += unknown.length();
                } else {
                    // Если не нашли неизвестный токен, продвигаемся на один символ
                    String unknown = String.valueOf(upperLine.charAt(pos));
                    tokens.add(new Token(TokenType.UNKNOWN, unknown, lineNumber, column));
                    pos++;
                    column++;
                }
            }
        }

        return tokens;
    }

    private static List<TokenType> getOrderedTokenTypes() {
        // Сначала проверяем более длинные и специфичные токены, затем общие
        return Arrays.asList(
                // Длинные условия для слов
                TokenType.EQUAL_VOWELS_CONSONANTS,
                TokenType.CONSONANT_FOLLOWED_BY_VOWEL,
                TokenType.VOWEL_FOLLOWED_BY_CONSONANT,
                TokenType.MORE_VOWELS_THAN_CONSONANTS,
                TokenType.MORE_CONSONANTS_THAN_VOWELS,
                TokenType.ALTERNATING,
                TokenType.ALLOW_DUPLICATES,

                // Общие условия
                TokenType.PALINDROME,

                // Параметры задач
                TokenType.ALPHABET, TokenType.LENGTH, TokenType.UNIQUE, TokenType.SET,

                // Ключевые слова
                TokenType.TASK, TokenType.DECK, TokenType.TARGET, TokenType.DRAW,
                TokenType.CONDITION, TokenType.CALCULATE,

                // Типы задач
                TokenType.CARDS, TokenType.WORDS, TokenType.NUMBERS, TokenType.EQUATIONS,
                TokenType.BALLS, TokenType.DIVISIBILITY, TokenType.REMAINDERS, TokenType.CHESS,

                // Типы колод
                TokenType.STANDARD, TokenType.FRENCH, TokenType.SPANISH, TokenType.CUSTOM,

                // Замена
                TokenType.REPLACEMENT, TokenType.NO_REPLACEMENT,

                // Вероятности
                TokenType.PROBABILITY, TokenType.COMBINATIONS, TokenType.EXPECTATION,

                // Достоинства карт
                TokenType.ACE, TokenType.KING, TokenType.QUEEN, TokenType.JACK,
                TokenType.RANK,

                // Масти
                TokenType.HEARTS, TokenType.DIAMONDS, TokenType.CLUBS, TokenType.SPADES,

                // Логические операторы
                TokenType.AND, TokenType.OR, TokenType.NOT,

                // Операторы сравнения
                TokenType.EQUALS, TokenType.NOT_EQUALS, TokenType.GREATER, TokenType.LESS,
                TokenType.GREATER_EQUAL, TokenType.LESS_EQUAL,

                // Булевы значения
                TokenType.BOOLEAN
        );
    }

    private static boolean checkSingleCharacters(String line, int pos, List<Token> tokens, int lineNumber, int column) {
        char c = line.charAt(pos);
        switch (c) {
            case '[':
                tokens.add(new Token(TokenType.LBRACKET, "[", lineNumber, column));
                return true;
            case ']':
                tokens.add(new Token(TokenType.RBRACKET, "]", lineNumber, column));
                return true;
            case '(':
                tokens.add(new Token(TokenType.LPAREN, "(", lineNumber, column));
                return true;
            case ')':
                tokens.add(new Token(TokenType.RPAREN, ")", lineNumber, column));
                return true;
            case ',':
                tokens.add(new Token(TokenType.COMMA, ",", lineNumber, column));
                return true;
            default:
                return false;
        }
    }

    private static boolean isWholeWord(String value, String line, int pos) {
        // Проверяем, что перед токеном нет буквенно-цифровых символов или подчеркивания
        if (pos > 0) {
            char prevChar = line.charAt(pos - 1);
            if (Character.isLetterOrDigit(prevChar) || prevChar == '_') {
                return false;
            }
        }

        // Проверяем, что после токена нет буквенно-цифровых символов или подчеркивания
        int endPos = pos + value.length();
        if (endPos < line.length()) {
            char nextChar = line.charAt(endPos);
            if (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
                return false;
            }
        }

        return true;
    }
}