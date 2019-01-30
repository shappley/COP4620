package COP4620;

import COP4620.lexer.Lexer;
import COP4620.lexer.Token;
import COP4620.lexer.TokenType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Project1 {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Must supply an input file name");
            System.exit(1);
        }
        final String input = readFile(args[0]);
        if (input != null) {
            Lexer lexer = new Lexer(input);
            Token[] tokens = lexer.getTokens();
            for (Token t : tokens) {
                if (t.getType() == TokenType.SPECIAL_SYMBOL) {
                    System.out.println(t.getValue());
                } else {
                    System.out.println(t.getType() + ": " + t.getValue());
                }
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
