package COP4620.parser;

import COP4620.lexer.Lexer;
import COP4620.lexer.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static COP4620.lexer.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ParserTest {

    @Test
    void isValid() {
    }

    @Test
    @DisplayName("declarationList (Rule #2)")
    void declarationList() {
        Lexer lexer = new Lexer("int main(void){int i;i=5;}");
        Token[] tokens = lexer.getTokens();
        Parser parser = new Parser(tokens);
        assertTrue(parser.declarationList());
    }

    @Test
    @DisplayName("declaration (Rule #3)")
    void declaration() {
        Stream<Arguments> concat = Stream.concat(varDecArgs(), funDecArgs());
        concat.forEach(e -> {
            Parser parser = new Parser((Token[]) e.get()[0]);
            boolean actual = parser.declaration();
            assertEquals((Boolean) e.get()[0], actual);
        });
    }

    @ParameterizedTest
    @DisplayName("varDeclaration (Rule #4)")
    @MethodSource("varDecArgs")
    void varDeclaration(Token[] tokens, boolean expected) {
        Parser parser = new Parser(tokens);
        boolean actual = parser.varDeclaration();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> varDecArgs() {
        return Stream.of(
                arguments(
                        new Token[]{
                                new Token(KEYWORD, "int"),
                                new Token(ID, "n"),
                                new Token(SPECIAL_SYMBOL, ";")
                        },
                        true
                ),
                arguments(
                        new Token[]{
                                new Token(KEYWORD, "int"),
                                new Token(ID, "n"),
                                new Token(SPECIAL_SYMBOL, "["),
                                new Token(NUM, "1"),
                                new Token(SPECIAL_SYMBOL, "]"),
                                new Token(SPECIAL_SYMBOL, ";")
                        },
                        true
                )
        );
    }

    @ParameterizedTest
    @DisplayName("typeSpecifier (Rule #5)")
    @MethodSource("typeSpecArgs")
    void typeSpecifier(Token token) {
        Parser parser = new Parser(new Token[]{token});
        boolean actual = parser.typeSpecifier();
        assertEquals(true, actual);
    }

    private static Stream<Arguments> typeSpecArgs() {
        return Stream.of(
                arguments(new Token(KEYWORD, "int")),
                arguments(new Token(KEYWORD, "float")),
                arguments(new Token(KEYWORD, "void"))
        );
    }

    @ParameterizedTest
    @DisplayName("funDeclaration (Rule #6)")
    @MethodSource("funDecArgs")
    void funDeclaration(Token[] tokens, boolean expected) {
        Parser parser = new Parser(tokens);
        boolean actual = parser.funDeclaration();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> funDecArgs() {
        return Stream.of(
                arguments(
                        new Token[]{
                                new Token(KEYWORD, "int"),
                                new Token(ID, "n"),
                                new Token(SPECIAL_SYMBOL, "("),
                                new Token(SPECIAL_SYMBOL, "void"),
                                new Token(SPECIAL_SYMBOL, ")"),
                                new Token(SPECIAL_SYMBOL, "{"),
                                new Token(SPECIAL_SYMBOL, "}")
                        },
                        true
                )
        );
    }

    @Test
    void params() {
    }

    @Test
    void paramList() {
    }

    @Test
    void param() {
    }

    @Test
    void compoundStmt() {
    }

    @Test
    void localDeclarations() {
    }

    @Test
    void statementList() {
    }

    @Test
    void statement() {
    }

    @Test
    void expressionStmt() {
    }

    @Test
    void selectionStmt() {
    }

    @Test
    void iterationStmt() {
    }

    @Test
    void returnStmt() {
    }

    @Test
    void expression() {
    }

    @ParameterizedTest
    @DisplayName("var (Rule #19)")
    @MethodSource("varArgs")
    void var(Token token) {
        Parser parser = new Parser(new Token[]{token});
        boolean actual = parser.var();
        assertEquals(true, actual);
    }

    private static Stream<Arguments> varArgs() {
        return Stream.of(
                arguments(new Token(ID, "hello"))
        );
    }

    @Test
    void simpleExpression() {
    }

    @ParameterizedTest
    @DisplayName("relop (Rule #21)")
    @MethodSource("relopArgs")
    void relop(Token token) {
        Parser parser = new Parser(new Token[]{token});
        boolean actual = parser.relop();
        assertEquals(true, actual);
    }

    private static Stream<Arguments> relopArgs() {
        return Stream.of(
                arguments(new Token(SPECIAL_SYMBOL, "<=")),
                arguments(new Token(SPECIAL_SYMBOL, "<")),
                arguments(new Token(SPECIAL_SYMBOL, ">")),
                arguments(new Token(SPECIAL_SYMBOL, ">=")),
                arguments(new Token(SPECIAL_SYMBOL, "==")),
                arguments(new Token(SPECIAL_SYMBOL, "!="))
        );
    }

    @Test
    void additiveExpression() {
    }

    @Test
    void term() {
    }

    @ParameterizedTest
    @DisplayName("addop (Rule #23)")
    @MethodSource("addopArgs")
    void addop(Token token) {
        Parser parser = new Parser(new Token[]{token});
        boolean actual = parser.addop();
        assertEquals(true, actual);
    }

    private static Stream<Arguments> addopArgs() {
        return Stream.of(
                arguments(new Token(SPECIAL_SYMBOL, "+")),
                arguments(new Token(SPECIAL_SYMBOL, "-"))
        );
    }

    @Test
    void mulop() {
    }

    @Test
    void factor() {
    }

    @Test
    void call() {
    }

    @Test
    void args() {
    }

    @Test
    void argList() {
    }
}