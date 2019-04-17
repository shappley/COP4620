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
    @MethodSource("COP4620.codegen.FunctionDeclarationTestProvider#functionDeclarationArgs")
    void functionDeclaration(String source, Quadruple[] instructions) throws Exception {
        test(source, Arrays.asList(instructions));
    }

    @DisplayName("2. Declarations")
    @ParameterizedTest(name = "2.{index}. {0}")
    @MethodSource("COP4620.codegen.DeclarationsTestProvider#declarationsArgs")
    void declarations(String source, Quadruple[] instructions) throws Exception {
        test(source, Arrays.asList(instructions));
    }

    @DisplayName("3. Expressions")
    @ParameterizedTest(name = "3.{index}. {0}")
    @MethodSource("COP4620.codegen.ExpressionsTestProvider#expressionsArgs")
    void expressions(String source, Quadruple[] instructions) throws Exception {
        test(source, Arrays.asList(instructions));
    }

    @DisplayName("4. Order of Operations")
    @ParameterizedTest(name = "4.{index}. {0}")
    @MethodSource("COP4620.codegen.OrderOfOperationsTestProvider#orderOfOperationsArgs")
    void orderOfOperations(String source, Quadruple[] instructions) throws Exception {
        test(source, Arrays.asList(instructions));
    }

    @DisplayName("5. Selection Statements")
    @ParameterizedTest(name = "5.{index}. {0}")
    @MethodSource("COP4620.codegen.SelectionStatementsTestProvider#args")
    void selectionStatements(String source, Quadruple[] instructions) throws Exception {
        test(source, Arrays.asList(instructions));
    }

    @DisplayName("6. Iteration Statements")
    @ParameterizedTest(name = "5.{index}. {0}")
    @MethodSource("COP4620.codegen.IterationStatementTestProvider#args")
    void iterationStatements(String source, Quadruple[] instructions) throws Exception {
        test(source, Arrays.asList(instructions));
    }

    @DisplayName("7. Eggen Tests")
    @ParameterizedTest(name = "5.{index}. {0}")
    @MethodSource("COP4620.codegen.EggenTestProvider#args")
    void eggenTests(String source, Quadruple[] instructions) throws Exception {
        test(source, Arrays.asList(instructions));
    }
}
