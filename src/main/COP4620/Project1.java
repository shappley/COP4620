package COP4620;

import COP4620.lexer.Lexer;
import COP4620.lexer.Token;

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
                System.out.println(t);
            }
        }
    }

    private static String readFile(String filename) {
        try {
            List<String> input = Files.readAllLines(Paths.get(filename));
            String s = String.join("\n", input);
            System.out.println(s);
            return s;
        } catch (IOException e) {
            System.out.println("Unable to read input file \"" + filename + "\"");
        }
        return null;
    }
}
