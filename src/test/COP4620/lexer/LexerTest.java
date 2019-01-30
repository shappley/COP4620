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
                arguments("/*6*/test = 5", 1, new Token(ID, "test")),
                arguments("/*6*/\ntest = 5", 1, new Token(ID, "test"))
        );
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
}