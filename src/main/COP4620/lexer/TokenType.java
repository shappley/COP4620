package COP4620.lexer;

public enum TokenType {
    KEYWORD, SPECIAL_SYMBOL, ID, NUM, ERROR;

    public static TokenType getTypeOf(String token) {
        if (Keyword.isKeyword(token)) {
            return KEYWORD;
        } else if (SpecialSymbol.isSpecialSymbol(token)) {
            return SPECIAL_SYMBOL;
        } else if (token.matches(Lexer.CHAR_TOKEN)) {
            return ID;
        } else if (token.matches(Lexer.NUM_TOKEN)) {
            return NUM;
        }
        return ERROR;
    }
}
