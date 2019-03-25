package COP4620.parser.semantics.nodes;

import COP4620.lexer.Token;

public class TerminalNode extends Node {
    private Token token;

    public TerminalNode(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
