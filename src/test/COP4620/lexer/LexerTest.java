package COP4620.lexer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static COP4620.lexer.TokenType.ID;
import static COP4620.lexer.TokenType.KEYWORD;
import static COP4620.lexer.TokenType.NUM;
import static COP4620.lexer.TokenType.SPECIAL_SYMBOL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class LexerTest {

    private void getNextToken(String source, int index, Token expected) {
        Lexer lexer = new Lexer(source);
        Token actual = null;
        for (int i = 0; i < index; i++) {
            actual = lexer.getNextToken();
        }
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @DisplayName("Keywords")
    @MethodSource("getKeywordsArguments")
    void testKeywords(String source, int index, Token expected) {
        getNextToken(source, index, expected);
    }

    private static Stream<Arguments> getKeywordsArguments() {
        return Stream.of(
                arguments("else", 1, new Token(KEYWORD, "else")),
                arguments("float", 1, new Token(KEYWORD, "float")),
                arguments("if", 1, new Token(KEYWORD, "if")),
                arguments("int", 1, new Token(KEYWORD, "int")),
                arguments("return", 1, new Token(KEYWORD, "return")),
                arguments("void", 1, new Token(KEYWORD, "void")),
                arguments("while", 1, new Token(KEYWORD, "while")),
                /* capitalization test */
                arguments("ELSE", 1, new Token(ID, "ELSE")),
                arguments("FLOAT", 1, new Token(ID, "FLOAT")),
                arguments("IF", 1, new Token(ID, "IF")),
                arguments("INT", 1, new Token(ID, "INT")),
                arguments("RETURN", 1, new Token(ID, "RETURN")),
                arguments("VOID", 1, new Token(ID, "VOID")),
                arguments("WHILE", 1, new Token(ID, "WHILE"))
        );
    }

    @ParameterizedTest
    @DisplayName("IDs")
    @MethodSource("getIdsArguments")
    void testIds(String source, int index, Token expected) {
        getNextToken(source, index, expected);
    }

    private static Stream<Arguments> getIdsArguments() {
        return Stream.of(
                arguments("djfdjfgh", 1, new Token(ID, "djfdjfgh")),
                arguments("int hello", 2, new Token(ID, "hello")),
                arguments("int hello=42", 2, new Token(ID, "hello")),
                arguments("if(tacos>burritos)", 3, new Token(ID, "tacos")),
                arguments("if(tacos>burritos)", 5, new Token(ID, "burritos"))
        );
    }

    @ParameterizedTest
    @DisplayName("Numbers")
    @MethodSource("getNumbersArguments")
    void testNumbers(String source, int index, Token expected) {
        getNextToken(source, index, expected);
    }

    private static Stream<Arguments> getNumbersArguments() {
        return Stream.of(
                arguments("1", 1, new Token(NUM, "1")),
                arguments("1.0", 1, new Token(NUM, "1.0")),
                arguments("1.000000", 1, new Token(NUM, "1.000000")),
                arguments("11111111", 1, new Token(NUM, "11111111")),
                arguments("11111111.00000000000", 1, new Token(NUM, "11111111.00000000000")),
                arguments("1E5", 1, new Token(NUM, "1E5")),
                arguments("1E+5", 1, new Token(NUM, "1E+5")),
                arguments("1E-5", 1, new Token(NUM, "1E-5")),
                arguments("1.0E5", 1, new Token(NUM, "1.0E5")),
                arguments("1.0E+5", 1, new Token(NUM, "1.0E+5")),
                arguments("1.0E-5", 1, new Token(NUM, "1.0E-5"))
        );
    }

    @ParameterizedTest
    @DisplayName("Special Symbols")
    @MethodSource("getSpecialSymbolsArguments")
    void testSpecialCharacters(String source, int index, Token expected) {
        getNextToken(source, index, expected);
    }

    private static Stream<Arguments> getSpecialSymbolsArguments() {
        return Stream.of(
                arguments(">=", 1, new Token(SPECIAL_SYMBOL, ">=")),
                arguments("<=", 1, new Token(SPECIAL_SYMBOL, "<=")),
                arguments("!=", 1, new Token(SPECIAL_SYMBOL, "!=")),
                arguments("==", 1, new Token(SPECIAL_SYMBOL, "==")),
                arguments("+", 1, new Token(SPECIAL_SYMBOL, "+")),
                arguments("-", 1, new Token(SPECIAL_SYMBOL, "-")),
                arguments("*", 1, new Token(SPECIAL_SYMBOL, "*")),
                arguments("/", 1, new Token(SPECIAL_SYMBOL, "/")),
                arguments("<", 1, new Token(SPECIAL_SYMBOL, "<")),
                arguments(">", 1, new Token(SPECIAL_SYMBOL, ">")),
                arguments("=", 1, new Token(SPECIAL_SYMBOL, "=")),
                arguments(";", 1, new Token(SPECIAL_SYMBOL, ";")),
                arguments(",", 1, new Token(SPECIAL_SYMBOL, ",")),
                arguments("(", 1, new Token(SPECIAL_SYMBOL, "(")),
                arguments(")", 1, new Token(SPECIAL_SYMBOL, ")")),
                arguments("[", 1, new Token(SPECIAL_SYMBOL, "[")),
                arguments("]", 1, new Token(SPECIAL_SYMBOL, "]")),
                arguments("}", 1, new Token(SPECIAL_SYMBOL, "}")),
                arguments("{", 1, new Token(SPECIAL_SYMBOL, "{")),
                arguments("while(tacos + burritos < 13)", 4, new Token(SPECIAL_SYMBOL, "+")),
                arguments("while(tacos - burritos < 13)", 4, new Token(SPECIAL_SYMBOL, "-")),
                arguments("while(tacos * burritos < 13)", 4, new Token(SPECIAL_SYMBOL, "*")),
                arguments("while(tacos / burritos < 13)", 4, new Token(SPECIAL_SYMBOL, "/"))
        );
    }

    @ParameterizedTest
    @DisplayName("Comments")
    @MethodSource("getCommentsArguments")
    void testComments(String source, int index, Token expected) {
        getNextToken(source, index, expected);
    }

    private static Stream<Arguments> getCommentsArguments() {
        return Stream.of(
                arguments("djfdjfgh//dfgdjfg", 2, null),
                arguments("/*hello*/ world", 1, new Token(ID, "world")),
                arguments("/*/* hello */*/ world", 1, new Token(ID, "world")),
                //multiline block comment
                arguments("/*/* \nhello */*/ world", 1, new Token(ID, "world"))
        );
    }
}