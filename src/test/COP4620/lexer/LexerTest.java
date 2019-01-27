package COP4620.lexer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LexerTest {

    @ParameterizedTest
    @DisplayName("getNextToken")
    @MethodSource("getNextTokenArguments")
    void getNextToken(String source, Token expected) {
        Lexer lexer = new Lexer(source);
        Token actual = lexer.getNextToken();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> getNextTokenArguments() {
        return Stream.of(Arguments.of("if(hello)", new Token(TokenType.KEYWORD, "if")));
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
        return Stream.of(Arguments.of(
                "if(hello==42)", new Token[]{
                        new Token(TokenType.KEYWORD, "if"),
                        new Token(TokenType.SPECIAL_SYMBOL, "("),
                        new Token(TokenType.ID, "hello"),
                        new Token(TokenType.SPECIAL_SYMBOL, "=="),
                        new Token(TokenType.NUM, "42"),
                        new Token(TokenType.SPECIAL_SYMBOL, ")")
                }
        ));
    }
}