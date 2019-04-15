package COP4620.codegen;

import COP4620.BaseTest;
import COP4620.lexer.Lexer;
import COP4620.lexer.Token;
import COP4620.parser.Parser;
import COP4620.parser.SemanticAnalyzer;
import COP4620.tree.nodes.Node;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("4. Code Generation")
class CodeGenTest extends BaseTest {
    private void test(String source, List<Quadruple> expected) throws Exception {
        if (source.endsWith(".txt")) {
            source = readResourceFile("codegen/" + source);
        }
        Lexer lexer = new Lexer(source);
        Token[] tokens = lexer.getTokens();
        Parser parser = new Parser(tokens);
        Node root = parser.program();
        SemanticAnalyzer semantics = new SemanticAnalyzer(root);
        assertTrue(semantics.isValid());
        CodeGenerator codeGenerator = new CodeGenerator(root);
        List<Quadruple> actual = codeGenerator.getInstructions();
        String actualString = quadrupleListToString(actual);

        System.out.println(actualString);

        assertEquals(quadrupleListToString(expected), actualString);
    }

    private String quadrupleListToString(List<Quadruple> list) {
        StringBuilder s = new StringBuilder();
        for (Quadruple e : list) {
            s.append(e.toString()).append("\n");
        }
        return s.toString();
    }

    @DisplayName("1. Function Declarations")
    @ParameterizedTest(name = "1.{index}. {0}")
    @MethodSource("functionDeclarationArgs")
    void functionDeclaration(String source, Quadruple[] instructions) throws Exception {
        test(source, Arrays.asList(instructions));
    }

    private static Stream<Arguments> functionDeclarationArgs() {
        return Stream.of(
                arguments("void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(void){return 1;} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "0"),
                        new Quadruple(2, Operation.RETURN, "", "", "1"),
                        new Quadruple(3, Operation.END, "FUNC", "f", ""),
                        new Quadruple(4, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(5, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(void){ int x; return x;} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.RETURN, "", "", "x"),
                        new Quadruple(4, Operation.END, "FUNC", "f", ""),
                        new Quadruple(5, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                })
        );
    }

    @DisplayName("2. Declarations")
    @ParameterizedTest(name = "2.{index}. {0}")
    @MethodSource("declarationsArgs")
    void declarations(String source, Quadruple[] instructions) throws Exception {
        test(source, Arrays.asList(instructions));
    }

    private static Stream<Arguments> declarationsArgs() {
        return Stream.of(
                arguments("void main(void) {int x;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void) {int x[10];}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "40", "", "x"),
                        new Quadruple(3, Operation.END, "FUNC", "main", "")
                })
        );
    }

    @DisplayName("3. Expressions")
    @ParameterizedTest(name = "3.{index}. {0}")
    @MethodSource("expressionsArgs")
    void expressions(String source, Quadruple[] instructions) throws Exception {
        test(source, Arrays.asList(instructions));
    }

    private static Stream<Arguments> expressionsArgs() {
        return Stream.of(
                arguments("void main(void){int x; x=1;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ASGN, "1", "", "x"),
                        new Quadruple(4, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=1+2;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "2", "t1"),
                        new Quadruple(4, Operation.ASGN, "t1", "", "x"),
                        new Quadruple(5, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=1+2+3;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "2", "t1"),
                        new Quadruple(4, Operation.ADD, "t1", "3", "t2"),
                        new Quadruple(5, Operation.ASGN, "t2", "", "x"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; int y; x=1; y=x;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(4, Operation.ASGN, "1", "", "x"),
                        new Quadruple(5, Operation.ASGN, "x", "", "y"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; int y; x=1; y=1+x;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(4, Operation.ASGN, "1", "", "x"),
                        new Quadruple(5, Operation.ADD, "1", "x", "t1"),
                        new Quadruple(6, Operation.ASGN, "t1", "", "y"),
                        new Quadruple(7, Operation.END, "FUNC", "main", "")
                })
        );
    }

    @DisplayName("4. Order of Operations")
    @ParameterizedTest(name = "4.{index}. {0}")
    @MethodSource("orderOfOperationsArgs")
    void orderOfOperations(String source, Quadruple[] instructions) throws Exception {
        test(source, Arrays.asList(instructions));
    }

    private static Stream<Arguments> orderOfOperationsArgs() {
        return Stream.of(
                arguments("void main(void){int x; x=1+2*3;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.MUL, "2", "3", "t1"),
                        new Quadruple(4, Operation.ADD, "1", "t1", "t2"),
                        new Quadruple(5, Operation.ASGN, "t2", "", "x"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=1*2+3;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.MUL, "1", "2", "t1"),
                        new Quadruple(4, Operation.ADD, "t1", "3", "t2"),
                        new Quadruple(5, Operation.ASGN, "t2", "", "x"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=1*2+3*4;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.MUL, "1", "2", "t1"),
                        new Quadruple(4, Operation.MUL, "3", "4", "t2"),
                        new Quadruple(5, Operation.ADD, "t1", "t2", "t3"),
                        new Quadruple(6, Operation.ASGN, "t3", "", "x"),
                        new Quadruple(7, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=(1+2)*3;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "2", "t1"),
                        new Quadruple(4, Operation.MUL, "t1", "3", "t2"),
                        new Quadruple(5, Operation.ASGN, "t2", "", "x"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=1+(2*3);}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.MUL, "2", "3", "t1"),
                        new Quadruple(4, Operation.ADD, "1", "t1", "t2"),
                        new Quadruple(5, Operation.ASGN, "t2", "", "x"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=(1+2)*(3+4);}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "2", "t1"),
                        new Quadruple(4, Operation.ADD, "3", "4", "t2"),
                        new Quadruple(5, Operation.MUL, "t1", "t2", "t3"),
                        new Quadruple(6, Operation.ASGN, "t3", "", "x"),
                        new Quadruple(7, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=(1+2)*(3+4)+5;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "2", "t1"),
                        new Quadruple(4, Operation.ADD, "3", "4", "t2"),
                        new Quadruple(5, Operation.MUL, "t1", "t2", "t3"),
                        new Quadruple(6, Operation.ADD, "t3", "5", "t4"),
                        new Quadruple(7, Operation.ASGN, "t4", "", "x"),
                        new Quadruple(8, Operation.END, "FUNC", "main", "")
                })
        );
    }
}
