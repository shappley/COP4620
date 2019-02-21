package COP4620.parser;

import COP4620.lexer.Token;
import COP4620.lexer.TokenType;

public class Parser {
    private int cursor = 0;
    private int backtrack = 0;

    private Token[] tokens;

    public Parser(Token[] tokens) {
        this.tokens = tokens;
    }

    //Rule #4
    //var-declaration -> type-specifier ID ; | type-specifier ID [ NUM ] ;
    public boolean varDeclaration() {
        return ((save() && typeSpecifier() && match(TokenType.ID) && match(";"))
                || (backtrack() && save() && typeSpecifier() && match(TokenType.ID) && match("[") && match(TokenType.NUM) && match("]") && match(";")));
    }

    //Rule #5
    //type-specifier -> int | float | void
    public boolean typeSpecifier() {
        return (save() && match("int")
                || (backtrack() && save() && match("float"))
                || (backtrack() && save() && match("void")));
    }

    private boolean save() {
        backtrack = cursor;
        return true;
    }

    private boolean backtrack() {
        cursor = backtrack;
        return true;
    }

    private boolean match(String expected) {
        final Token token = nextToken();
        return token != null && expected.equals(token.getValue());
    }

    private boolean match(TokenType expected) {
        final Token token = nextToken();
        return token != null && expected == token.getType();
    }

    private Token nextToken() {
        if (cursor == tokens.length) {
            return null;
        }
        return tokens[cursor++];
    }
}
