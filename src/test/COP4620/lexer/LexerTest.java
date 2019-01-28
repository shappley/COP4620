package COP4620.lexer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class LexerTest {

    @ParameterizedTest
    @DisplayName("getNextToken")
    @MethodSource("getNextTokenArguments")
    void getNextToken(String source, int index, Token expected) {
        Lexer lexer = new Lexer(source);
        Token actual = null;
        for (int i = 0; i < index; i++) {
            actual = lexer.getNextToken();
        }
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> getNextTokenArguments() {
        return Stream.of(
                arguments("if (hello == 42)", 1, new Token(TokenType.KEYWORD, "if")),
                arguments("if (hello == 42)", 2, new Token(TokenType.SPECIAL_SYMBOL, "(")),
                arguments("if (hello == 42)", 3, new Token(TokenType.ID, "hello")),
                arguments("if (hello == 42)", 4, new Token(TokenType.SPECIAL_SYMBOL, "==")),
                arguments("if (hello == 42)", 5, new Token(TokenType.NUM, "42")),
                arguments("if (hello == 42)", 6, new Token(TokenType.SPECIAL_SYMBOL, ")"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("getTokens")
    @MethodSource("getTokensArguments")
    void getTokens(String source, Token[] expected) {
        Lexer lexer = new Lexer(source);
        Token[] actual = lexer.getTokens();
        assertArrayEquals(expected, actual);
    }

    private static Stream<Arguments> getTokensArguments() {
        return Stream.of(
                arguments("if(hello==42)//int i = 5;", new Token[]{
                        new Token(TokenType.KEYWORD, "if"),
                        new Token(TokenType.SPECIAL_SYMBOL, "("),
                        new Token(TokenType.ID, "hello"),
                        new Token(TokenType.SPECIAL_SYMBOL, "=="),
                        new Token(TokenType.NUM, "42"),
                        new Token(TokenType.SPECIAL_SYMBOL, ")")
                }
        ));
    }

    @ParameterizedTest
    @DisplayName("stripComments")
    @MethodSource("stripCommentsArguments")
    void stripComments(String source, String expected) {
        Lexer lexer = new Lexer(source);
        lexer.stripComments();
        assertEquals(expected, lexer.getRemainingSource());
    }

    private static Stream<Arguments> stripCommentsArguments() {
        return Stream.of(
                arguments("if(hello==42)//int i = 5;", "if(hello==42)"),
                arguments("if(hello==42)/*int i = 5;*/", "if(hello==42)")
        );
    }
}