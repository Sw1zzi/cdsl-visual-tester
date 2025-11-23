
---

# **CDSL — Полная документация**

## *Combinatorics Domain Specific Language*

---

# **Оглавление**

1. [Общее описание](#общее-описание)
2. [Структура программы](#структура-программы)
3. [Типы задач](#типы-задач)
4. [Детальный синтаксис по типам задач](#детальный-синтаксис-по-типам-задач)
5. [Токены и значения](#токены-и-значения)
6. [Примеры полных задач](#примеры-полных-задач)
7. [Особенности реализации](#особенности-реализации)

---

# **Общее описание**

**CDSL (Combinatorics Domain Specific Language)** — специализированный язык для описания комбинаторных задач.

Характеристики:

* строгий синтаксис;
* нечувствительность к регистру;
* поддержка *8 типов задач*;
* множественные значения через `[]`;
* расширяемость.

---

# **Структура программы**

Каждая программа — последовательность объявлений:

```
TASK <тип_задачи> "<название>"
[параметры задачи]
TARGET <цель | список целей>
[условия]
DRAW <параметры извлечения>
CALCULATE <тип расчёта>
```

Порядок:

1. `TASK`
2. параметры задачи
3. `TARGET`
4. `DRAW`
5. `CALCULATE`

---

# **Типы задач**

Ниже краткая сводка. Детали — далее.

| Тип            | Название  | Описание                                        |
| -------------- | --------- | ----------------------------------------------- |
| `CARDS`        | Карты     | Колоды, карты, вероятности                      |
| `WORDS`        | Слова     | Алфавиты, длины, условия                        |
| `CHESS`        | Шахматы   | Доски, фигуры, атака                            |
| `REMAINDERS`   | Остатки   | Делимое, делитель, остаток                      |
| `DIVISIBILITY` | Делимости | Преобразования чисел                            |
| `BALLS`        | Шары      | Урны, последовательное/одновременное извлечение |
| `EQUATIONS`    | Уравнения | Линейные уравнения на ограниченных множествах   |
| `NUMBERS`      | Числа     | Последовательности цифр с условиями             |

---

# **Детальный синтаксис по типам задач**

---

## **1. CARDS — Карточные задачи**

Параметры:

```
DECK STANDARD|FRENCH|SPANISH|CUSTOM <размер>
TARGET <ранг> <масть> 
TARGET [<ранг> <масть>, ...]
DRAW <n> REPLACEMENT|NO_REPLACEMENT
```

### Ранги:

`2–10, JACK(J), QUEEN(Q), KING(K), ACE(A)`

### Масти:

`HEARTS(H), DIAMONDS(D), CLUBS(C), SPADES(S)`

### Пример:

```cdsl
TASK CARDS "Вероятность туза пик"
DECK STANDARD 36
TARGET ACE SPADES
DRAW 5 NO_REPLACEMENT
CALCULATE PROBABILITY
```

---

## **2. WORDS — Слова**

Параметры:

```
ALPHABET "<строка>"
LENGTH <n>
UNIQUE YES|NO
TARGET [условия]
```

### Условия:

* `PALINDROME`
* `ALTERNATING`
* `CONSONANT_FOLLOWED_BY_VOWEL`
* `VOWEL_FOLLOWED_BY_CONSONANT`
* `MORE_VOWELS_THAN_CONSONANTS`
* `MORE_CONSONANTS_THAN_VOWELS`
* `EQUAL_VOWELS_CONSONANTS`

### Пример:

```cdsl
TASK WORDS "Палиндромы 5 букв"
ALPHABET "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
LENGTH 5
UNIQUE YES
TARGET [PALINDROME, EQUAL_VOWELS_CONSONANTS]
CALCULATE COMBINATIONS
```

---

## **3. CHESS — Шахматные задачи**

Параметры:

```
BOARD_HEIGHT <n>
BOARD_WIDTH <n>
PIECES ["<фигура>" <количество>, ...]
ATTACKING | NON_ATTACKING
```

### Пример:

```cdsl
TASK CHESS "Неатакующие ладьи"
BOARD_HEIGHT 8
BOARD_WIDTH 8
PIECES ["ROOK" 2, "KNIGHT" 3]
NON_ATTACKING
CALCULATE COMBINATIONS
```

---

## **4. REMAINDERS — Остатки**

Параметры:

```
DIVIDEND "<выражение>"
DIVISOR <n>
REMAINDER <n>
```

### Пример:

```cdsl
TASK REMAINDERS "Числа ≡ 2 (mod 7)"
DIVIDEND "X"
DIVISOR 7
REMAINDER 2
CALCULATE COUNT
```

---

## **5. DIVISIBILITY — Делимости**

Параметры:

```
NUMBER_LENGTH <n>
TRANSFORMATION ["<правило1>", ...]
INCREASES_BY_FACTOR <n> |
DECREASES_BY_FACTOR <n> |
UNCHANGED |
INCREASES_BY <n> |
DECREASES_BY <n>
```

### Пример:

```cdsl
TASK DIVISIBILITY "Увеличение в 2 раза при перестановке"
NUMBER_LENGTH 8
TRANSFORMATION ["12345678", "87654321"]
INCREASES_BY_FACTOR 2
CALCULATE COMBINATIONS
```

---

## **6. BALLS — Шары и урны**

Параметры:

```
URN ["<цвет>" <количество>, ...]
DRAW_SEQUENTIAL | DRAW_SIMULTANEOUS
DRAW_COUNT <n>
TARGET ["<цвет>" <количество>, ...]
```

### Пример:

```cdsl
TASK BALLS "1 красный, 1 синий, 1 белый"
URN ["RED" 3, "BLUE" 5, "GREEN" 2, "WHITE" 1]
DRAW_SIMULTANEOUS
DRAW_COUNT 3
TARGET ["RED" 1, "BLUE" 1, "WHITE" 1]
CALCULATE PROBABILITY
```

---

## **7. EQUATIONS — Уравнения**

Параметры:

```
UNKNOWNS <n>
COEFFICIENTS [a1, a2, ...]
SUM <s>
DOMAIN "<множество>"
CONSTRAINTS ["<ограничение>", ...]
```

### Пример:

```cdsl
TASK EQUATIONS "Решения с ограничениями"
UNKNOWNS 4
COEFFICIENTS [1, 1, 1, 1]
SUM 25
DOMAIN "NATURAL"
CONSTRAINTS ["x2 <= 2", "x4 > 5"]
CALCULATE COMBINATIONS
```

---

## **8. NUMBERS — Числа**

Параметры:

```
DIGITS <n>
DISTINCT YES|NO
ADJACENT_DIFFERENT YES|NO
INCREASING | NON_DECREASING | DECREASING | NON_INCREASING
```

### Пример:

```cdsl
TASK NUMBERS "Возрастающие 6-значные числа"
DIGITS 6
DISTINCT YES
ADJACENT_DIFFERENT YES
INCREASING
CALCULATE COMBINATIONS
```

---

# **Токены и значения**

### Ключевые слова:

`TASK, DECK, TARGET, DRAW, CALCULATE`
`ALPHABET, LENGTH, UNIQUE`
`BOARD_HEIGHT, BOARD_WIDTH, PIECES`
`DIVIDEND, DIVISOR, REMAINDER`
`NUMBER_LENGTH, TRANSFORMATION`
`URN, DRAW_SEQUENTIAL, DRAW_SIMULTANEOUS`
`UNKNOWNS, COEFFICIENTS, SUM, DOMAIN, CONSTRAINTS`
`DIGITS, DISTINCT, ADJACENT_DIFFERENT`

### Типы расчётов:

* `PROBABILITY`
* `COMBINATIONS`
* `EXPECTATION`

### Значения:

* **INTEGER** — `123`
* **STRING** — `"ABC"`
* **BOOLEAN** — `YES, NO`

---

# **Примеры полных задач**

### Пример 1 — Карты

```cdsl
TASK CARDS "Probability of two Aces"
DECK STANDARD 52
TARGET [ACE SPADES, ACE HEARTS]
DRAW 2 NO_REPLACEMENT
CALCULATE PROBABILITY
```

### Пример 2 — Слова

```cdsl
TASK WORDS "5-letter palindromes"
ALPHABET "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
LENGTH 5
UNIQUE NO
TARGET [PALINDROME]
CALCULATE COMBINATIONS
```

### Пример 3 — Шахматы

```cdsl
TASK CHESS "8 Queens Problem"
BOARD_HEIGHT 8
BOARD_WIDTH 8
PIECES ["QUEEN" 8]
NON_ATTACKING
CALCULATE COMBINATIONS
```

### Пример 4 — Остатки

```cdsl
TASK REMAINDERS "Numbers divisible by 7 with remainder 3"
DIVIDEND "X"
DIVISOR 7
REMAINDER 3
CALCULATE COUNT
```

### Пример 5 — Делимости

```cdsl
TASK DIVISIBILITY "Number transformation divisibility"
NUMBER_LENGTH 3
TRANSFORMATION ["ABC", "CBA"]
INCREASES_BY_FACTOR 2
CALCULATE COMBINATIONS
```

### Пример 6 — Урны

```cdsl
TASK BALLS "Sequential ball drawing"
URN ["RED" 5, "BLUE" 3, "GREEN" 2]
DRAW_SEQUENTIAL
DRAW_COUNT 3
TARGET ["RED" 2, "BLUE" 1]
CALCULATE PROBABILITY
```

### Пример 7 — Уравнения

```cdsl
TASK EQUATIONS "Natural number solutions"
UNKNOWNS 3
COEFFICIENTS [1, 2, 3]
SUM 10
DOMAIN "NATURAL"
CONSTRAINTS ["x1 > 0", "x2 >= 2"]
CALCULATE COMBINATIONS
```

### Пример 8 — Числа

```cdsl
TASK NUMBERS "Special 6-digit numbers"
DIGITS 6
DISTINCT YES
ADJACENT_DIFFERENT YES
INCREASING
CALCULATE COMBINATIONS
```

---

# **Особенности реализации**

### Ошибки:

* неизвестные токены — предупреждение;
* недостающие параметры — подстановка значений по умолчанию.

### Значения по умолчанию:

* колода: **52**;
* длина слова: **5**;
* шахматная доска: **8×8**;
* тип извлечения: `NO_REPLACEMENT`;
* расчёт: `PROBABILITY`.

### Расширяемость:

* новые задачи — через `ProblemType`;
* новые токены — в `TokenType`;
* новые параметры — в `ProblemContext`.

---