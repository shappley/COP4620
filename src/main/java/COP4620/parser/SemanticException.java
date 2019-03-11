package COP4620.parser;

import COP4620.lexer.Token;

public class SemanticException {
    private String message;

    public SemanticException(String error, Token... tokens) {
        this.message = ("Error: " + error + " for statement \"" + stringify(tokens) + "\"");
    }

    public String getMessage() {
        return message;
    }

    private static String stringify(Token[] tokens) {
        StringBuilder msg = new StringBuilder();
        for (Token t : tokens) {
            msg.append(t.getValue()).append(" ");
        }
        return msg.toString().trim();
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
