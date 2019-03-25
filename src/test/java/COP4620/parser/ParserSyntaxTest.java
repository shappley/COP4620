package COP4620.parser;

import COP4620.BaseTest;
import COP4620.lexer.Lexer;
import COP4620.lexer.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ParserSyntaxTest extends BaseTest {

    @TestFactory
    @DisplayName("Rule #0: Test Files")
    List<DynamicTest> testFiles() throws Exception {
        List<DynamicTest> tests = buildFileTestsForDirectory("syntax/ACCEPT", true);
        tests.addAll(buildFileTestsForDirectory("syntax/REJECT", false));
        return tests;
    }

    private List<DynamicTest> buildFileTestsForDirectory(String dir, boolean expected) {
        List<DynamicTest> tests = new ArrayList<>();
        File[] files = getResourceFilesInDirectory(dir);
        for (File f : files) {
            tests.add(DynamicTest.dynamicTest("File " + f.getName() + ", expected " + expected, () -> {
                String source = getSource(f.getAbsolutePath());
                Parser parser = getParser(source);
                assertEquals(expected, parser.program() != null && parser.isDone());
            }));
        }
        return tests;
    }

    private Parser getParser(String source) {
        Lexer lexer = new Lexer(source);
        Token[] tokens = lexer.getTokens();
        return new Parser(tokens);
    }

    @ParameterizedTest
    @DisplayName("Rule #2: declaration-list")
    @MethodSource("declarationListArgs")
    void declarationList(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.declarationList() != null && p.isDone());
    }

    private static Stream<Arguments> declarationListArgs() {
        return Stream.of(arguments("int main(void){int i;i=5;}", true));
    }

    @ParameterizedTest
    @DisplayName("Rule #3: declaration")
    @MethodSource("declarationArgs")
    void declaration(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.declaration() != null && p.isDone());
    }

    private static Stream<Arguments> declarationArgs() {
        return Stream.of(
                arguments("int main;", true),
                arguments("int main(void){}", true),
                arguments("int main[1];", true)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #4: var-declaration")
    @MethodSource("varDecArgs")
    void varDeclaration(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.varDeclaration() != null && p.isDone());
    }

    private static Stream<Arguments> varDecArgs() {
        return Stream.of(
                arguments("int main;", true),
                arguments("int main[1];", true),
                arguments("int main[];", false)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #5: type-specifier")
    @MethodSource("typeSpecArgs")
    void typeSpecifier(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.typeSpecifier() != null && p.isDone());
    }

    private static Stream<Arguments> typeSpecArgs() {
        return Stream.of(
                arguments("int", true),
                arguments("float", true),
                arguments("void", true),
                arguments("double", false)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #6: fun-declaration")
    @MethodSource("funDecArgs")
    void funDeclaration(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.funDeclaration() != null && p.isDone());
    }

    private static Stream<Arguments> funDecArgs() {
        return Stream.of(
                arguments("int main(void){}", true),
                arguments("int main(int i){}", true),
                arguments("int main(int i, int j){}", true),
                arguments("int main(int i int j){}", false),
                arguments("int main(int i, int j){", false)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #7: params")
    @MethodSource("paramsArgs")
    void params(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.params() != null && p.isDone());
    }

    private static Stream<Arguments> paramsArgs() {
        return Stream.of(
                arguments("void", true),
                arguments("int i", true),
                arguments("int i[]", true)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #8: param-list")
    @MethodSource("paramListArgs")
    void paramList(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.paramList() != null && p.isDone());
    }

    private static Stream<Arguments> paramListArgs() {
        return Stream.of(
                arguments("int i, float j, int k", true)
        );
    }


    @ParameterizedTest
    @DisplayName("Rule #9: param")
    @MethodSource("paramArgs")
    void param(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.param() != null && p.isDone());
    }

    private static Stream<Arguments> paramArgs() {
        return Stream.of(
                arguments("int i", true),
                arguments("int i[]", true)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #10: compound-stmt")
    @MethodSource("compoundStmtArgs")
    void compoundStmt(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.compoundStmt() != null && p.isDone());
    }

    private static Stream<Arguments> compoundStmtArgs() {
        return Stream.of(
                arguments("{}", true),
                arguments("{int i; i=5;}", true),
                arguments("{int i; i=5;", false)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #11: local-declarations")
    @MethodSource("localDeclarationsArgs")
    void localDeclarations(String source, boolean valid) {
        final Parser p = getParser(source);
        if (source.isEmpty()) {
            assertEquals(valid, p.localDeclarations() == null && p.isDone());
        } else {
            assertEquals(valid, p.localDeclarations() != null && p.isDone());
        }
    }

    private static Stream<Arguments> localDeclarationsArgs() {
        return Stream.concat(Stream.of(arguments("", true)), varDecArgs());
    }

    @ParameterizedTest
    @DisplayName("Rule #12: statement-list")
    @MethodSource("statementArgs")
    void statementList(String source, boolean valid) {
        statement(source, valid);//statementList is just a bunch of statements
    }

    @ParameterizedTest
    @DisplayName("Rule #13: statement")
    @MethodSource("statementArgs")
    void statement(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.statement() != null && p.isDone());
    }

    private static Stream<Arguments> statementArgs() {
        Stream<Arguments> stream = expressionStmtArgs();
        stream = Stream.concat(stream, compoundStmtArgs());
        stream = Stream.concat(stream, selectionStmtArgs());
        stream = Stream.concat(stream, iterationStmtArgs());
        stream = Stream.concat(stream, returnStmtArgs());
        return stream;
    }

    @ParameterizedTest
    @DisplayName("Rule #14: expression-stmt")
    @MethodSource("expressionStmtArgs")
    void expressionStmt(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.expressionStmt() != null && p.isDone());
    }

    private static Stream<Arguments> expressionStmtArgs() {
        return Stream.of(arguments("i=5;", true), arguments("i[3]=5;", true));
    }

    @ParameterizedTest
    @DisplayName("Rule #15: selection-stmt")
    @MethodSource("selectionStmtArgs")
    void selectionStmt(String source, boolean valid) {
        Parser p = getParser(source);
        assertEquals(valid, p.selectionStmt() != null && p.isDone());
    }

    private static Stream<Arguments> selectionStmtArgs() {
        return Stream.of(
                arguments("if(x){}", true),
                arguments("if(x){}else{}", true),
                arguments("if(v == 0){return u;}else{return p;}", true),
                arguments("if(v == 0) return u; else return gcd(v, u-u/v*v);", true),
                arguments("if(v != 0) return u else return gcd(v, u-u/v*v);", false)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #16: iteration-stmt")
    @MethodSource("iterationStmtArgs")
    void iterationStmt(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.iterationStmt() != null && p.isDone());
    }

    private static Stream<Arguments> iterationStmtArgs() {
        return Stream.of(arguments("while(1>2){}", true));
    }

    @ParameterizedTest
    @DisplayName("Rule #17: return-stmt")
    @MethodSource("returnStmtArgs")
    void returnStmt(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.returnStmt() != null && p.isDone());
    }

    private static Stream<Arguments> returnStmtArgs() {
        return Stream.of(
                arguments("return;", true),
                arguments("return 5;", true),
                arguments("return 5<3;", true),
                arguments("return x;", true),
                arguments("return 5", false),
                arguments("return x", false)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #18: expression")
    @MethodSource("expressionArgs")
    void expression(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.expression() != null && p.isDone());
    }

    private static Stream<Arguments> expressionArgs() {
        return Stream.of(
                arguments("i=5+3", true),
                arguments("i[3]=5", true)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #19: var")
    @MethodSource("varArgs")
    void var(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.var() != null && p.isDone());
    }

    private static Stream<Arguments> varArgs() {
        return Stream.of(arguments("i", true), arguments("i[3]", true));
    }

    @ParameterizedTest
    @DisplayName("Rule #20: simple-expression")
    @MethodSource("simpleExpressionArgs")
    void simpleExpression(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.simpleExpression() != null && p.isDone());
    }

    private static Stream<Arguments> simpleExpressionArgs() {
        return Stream.of(arguments("i+3>2", true));
    }

    @ParameterizedTest
    @DisplayName("Rule #21: relop")
    @MethodSource("relopArgs")
    void relop(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.relop() != null && p.isDone());
    }

    private static Stream<Arguments> relopArgs() {
        return Stream.of(
                arguments("<=", true),
                arguments("<", true),
                arguments(">", true),
                arguments(">=", true),
                arguments("==", true),
                arguments("!=", true),
                arguments("!", false)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #22: additive-expression")
    @MethodSource("additiveExpArgs")
    void additiveExpression(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.additiveExpression() != null && p.isDone());
    }

    private static Stream<Arguments> additiveExpArgs() {
        return Stream.of(
                arguments("5", true),
                arguments("x+5", true),
                arguments("5+5", true)
        );
    }

    @ParameterizedTest
    @DisplayName("Rule #23: addop")
    @MethodSource("addopArgs")
    void addop(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.addop() != null && p.isDone());
    }

    private static Stream<Arguments> addopArgs() {
        return Stream.of(arguments("+", true), arguments("-", true), arguments("*", false));
    }

    @Test
    @DisplayName("Rule #24: term")
    void term() {
        //TODO
    }

    @ParameterizedTest
    @DisplayName("Rule #25: mulop")
    @MethodSource("mulopArgs")
    void mulop(String source, boolean valid) {
        final Parser p = getParser(source);
        assertEquals(valid, p.mulop() != null && p.isDone());
    }

    private static Stream<Arguments> mulopArgs() {
        return Stream.of(arguments("*", true), arguments("/", true), arguments("+", false));
    }

    @Test
    @DisplayName("Rule #26: factor")
    void factor() {
        //TODO
    }

    @Test
    @DisplayName("Rule #27: call")
    void call() {
        //TODO
    }

    @Test
    @DisplayName("Rule #28: args")
    void args() {
        //TODO
    }

    @Test
    @DisplayName("Rule #29: arg-list")
    void argList() {
        //TODO
    }
}