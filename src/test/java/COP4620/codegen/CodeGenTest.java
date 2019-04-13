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
    private void test(String source, List<Quadruple> instructions) throws Exception {
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
        List<Quadruple> list = codeGenerator.getInstructions();

        assertEquals(quadrupleListToString(instructions), quadrupleListToString(list));
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
                arguments("void main(void) {}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.END, "FUNC", "main", "")
                })
        );
    }
}
