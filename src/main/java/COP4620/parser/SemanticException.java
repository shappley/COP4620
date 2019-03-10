package COP4620.parser;

import COP4620.lexer.Token;

public class SemanticException extends Exception {
    public SemanticException(String error, Token... tokens) {
        super(error + " for statement \"" + stringify(tokens) + "\"");
    }

    private static String stringify(Token[] tokens) {
        StringBuilder msg = new StringBuilder();
        for (Token t : tokens) {
            msg.append(t.getValue());
        }
        return msg.toString();
    }
}
