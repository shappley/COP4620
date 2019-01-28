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
                arguments("void func(){}", 1, new Token(TokenType.KEYWORD, "void")),
                arguments("int hello = 42", 1, new Token(TokenType.KEYWORD, "int")),
                arguments("float hello = 42.44", 1, new Token(TokenType.KEYWORD, "float")),
                arguments("return 11;", 1, new Token(TokenType.KEYWORD, "return")),
                arguments("while(dlfgldfg >= 1.04)", 1, new Token(TokenType.KEYWORD, "while")),
                arguments("while(tacos)", 3, new Token(TokenType.ID, "tacos")),
                arguments("while(tacos+burritos < 13)", 3, new Token(TokenType.ID, "tacos")),
                arguments("while(tacos-burritos < 13)", 5, new Token(TokenType.ID, "burritos")),
                arguments("while(tacos + burritos < 13)", 4, new Token(TokenType.SPECIAL_SYMBOL, "+")),
                arguments("while(tacos - burritos < 13)", 4, new Token(TokenType.SPECIAL_SYMBOL, "-")),
                arguments("while(tacos * burritos < 13)", 4, new Token(TokenType.SPECIAL_SYMBOL, "*")),
                arguments("while(tacos / burritos < 13)", 4, new Token(TokenType.SPECIAL_SYMBOL, "/")),
                arguments("if(dlfgldfg >= 1.04)", 4, new Token(TokenType.SPECIAL_SYMBOL, ">=")),
                arguments("if(dlfgldfg <= 1.04)", 4, new Token(TokenType.SPECIAL_SYMBOL, "<=")),
                arguments("if(dlfgldfg != 1.04)", 4, new Token(TokenType.SPECIAL_SYMBOL, "!=")),
                arguments("if(dlfgldfg > 1.04)", 4, new Token(TokenType.SPECIAL_SYMBOL, ">")),
                arguments("if(dlfgldfg < 1.04)", 4, new Token(TokenType.SPECIAL_SYMBOL, "<")),
                arguments("if(dlfgldfg >= 1.04)", 5, new Token(TokenType.NUM, "1.04")),
                arguments("if(dlfgldfg >= 1.04E2)", 5, new Token(TokenType.NUM, "1.04E2")),
                arguments("if(dlfgldfg >= 1.04E-2)", 5, new Token(TokenType.NUM, "1.04E-2")),
                arguments("if(dlfgldfg >= /*/*foo*/*/ 1.04E-2)", 5, new Token(TokenType.NUM, "1.04E-2"))
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
                arguments("if(hello==42) { /*fgfgh/*int i = 5;*/fgfgh*/ return 11; }//int j = 12;", new Token[]{
                                new Token(TokenType.KEYWORD, "if"),
                                new Token(TokenType.SPECIAL_SYMBOL, "("),
                                new Token(TokenType.ID, "hello"),
                                new Token(TokenType.SPECIAL_SYMBOL, "=="),
                                new Token(TokenType.NUM, "42"),
                                new Token(TokenType.SPECIAL_SYMBOL, ")"),
                                new Token(TokenType.SPECIAL_SYMBOL, "{"),
                                new Token(TokenType.KEYWORD, "return"),
                                new Token(TokenType.NUM, "11"),
                                new Token(TokenType.SPECIAL_SYMBOL, ";"),
                                new Token(TokenType.SPECIAL_SYMBOL, "}")
                        }
                ));
    }
}