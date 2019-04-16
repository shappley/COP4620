package COP4620;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Quadruple;
import COP4620.lexer.Lexer;
import COP4620.lexer.Token;
import COP4620.parser.Parser;
import COP4620.parser.SemanticAnalyzer;
import COP4620.tree.nodes.Node;

import java.util.List;

public class Project4 {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("void main(void){ int x; x=1;}");
        Token[] tokens = lexer.getTokens();
        Parser parser = new Parser(tokens);
        Node root = parser.program();
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(root);
        if (semanticAnalyzer.isValid()) {
            CodeGenerator codeGen = new CodeGenerator(root);
            List<Quadruple> instructions = codeGen.getInstructions();
            for (Quadruple e : instructions) {
                System.out.println(e);
            }
        }
    }
}
