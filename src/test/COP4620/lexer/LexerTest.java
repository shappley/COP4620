package COP4620.lexer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static COP4620.lexer.TokenType.*;
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
                arguments("void func(){}", 1, new Token(KEYWORD, "void")),
                arguments("int hello = 42", 1, new Token(KEYWORD, "int")),
                arguments("float hello = 42.44", 1, new Token(KEYWORD, "float")),
                arguments("return 11;", 1, new Token(KEYWORD, "return")),
                arguments("while(dlfgldfg >= 1.04)", 1, new Token(KEYWORD, "while")),
                arguments("while(tacos)", 3, new Token(ID, "tacos")),
                arguments("while(tacos+burritos < 13)", 3, new Token(ID, "tacos")),
                arguments("while(tacos-burritos < 13)", 5, new Token(ID, "burritos")),
                arguments("while(tacos + burritos < 13)", 4, new Token(SPECIAL_SYMBOL, "+")),
                arguments("while(tacos - burritos < 13)", 4, new Token(SPECIAL_SYMBOL, "-")),
                arguments("while(tacos * burritos < 13)", 4, new Token(SPECIAL_SYMBOL, "*")),
                arguments("while(tacos / burritos < 13)", 4, new Token(SPECIAL_SYMBOL, "/")),
                arguments("if(dlfgldfg >= 1.04)", 4, new Token(SPECIAL_SYMBOL, ">=")),
                arguments("if(dlfgldfg <= 1.04)", 4, new Token(SPECIAL_SYMBOL, "<=")),
                arguments("if(dlfgldfg != 1.04)", 4, new Token(SPECIAL_SYMBOL, "!=")),
                arguments("if(dlfgldfg > 1.04)", 4, new Token(SPECIAL_SYMBOL, ">")),
                arguments("if(dlfgldfg < 1.04)", 4, new Token(SPECIAL_SYMBOL, "<")),
                arguments("if(dlfgldfg >= 1.04)", 5, new Token(NUM, "1.04")),
                arguments("if(dlfgldfg >= 1.04E2)", 5, new Token(NUM, "1.04E2")),
                arguments("if(dlfgldfg >= 1.04E-2)", 5, new Token(NUM, "1.04E-2")),
                arguments("if(dlfgldfg >= /*/*foo*/*/ 1.04E-2)", 5, new Token(NUM, "1.04E-2")),
                arguments("/*6*/test = 5", 2, new Token(ID, "test"))
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
                                new Token(KEYWORD, "if"),
                                new Token(SPECIAL_SYMBOL, "("),
                                new Token(ID, "hello"),
                                new Token(SPECIAL_SYMBOL, "=="),
                                new Token(NUM, "42"),
                                new Token(SPECIAL_SYMBOL, ")"),
                                new Token(SPECIAL_SYMBOL, "{"),
                                new Token(KEYWORD, "return"),
                                new Token(NUM, "11"),
                                new Token(SPECIAL_SYMBOL, ";"),
                                new Token(SPECIAL_SYMBOL, "}")
                        }
                ));
    }
}