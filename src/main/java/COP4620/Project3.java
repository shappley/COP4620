package COP4620;

import COP4620.lexer.Lexer;
import COP4620.lexer.Token;
import COP4620.parser.Parser;
import COP4620.parser.semantics.SemanticAnalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Project3 {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Must specify input file name");
            return;
        }
        String input = readFile(args[0]);
        if (input != null) {
            try {
                Lexer lexer = new Lexer(input);
                Token[] tokens = lexer.getTokens();
                Parser parser = new Parser(tokens);
                SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.program());
                System.out.println(semanticAnalyzer.isValid() ? "ACCEPT" : "REJECT");
            } catch (Exception e) {
                System.out.println("REJECT");
            }
        }
    }

    private static String readFile(String filename) {
        try {
            List<String> input = Files.readAllLines(Paths.get(filename));
            return String.join("\n", input);
        } catch (IOException e) {
            System.out.println("Unable to read input file \"" + filename + "\"");
        }
        return null;
    }
}
