package COP4620.lexer;

public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Token) {
            Token comp = (Token) obj;
            return this.getType() == comp.getType()
                    && this.getValue().equals(((Token) obj).getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + "'" + getType() + "', '" + getValue() + "']";
    }
}
