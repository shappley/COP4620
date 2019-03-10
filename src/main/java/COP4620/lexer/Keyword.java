package COP4620.lexer;

public enum Keyword {
    ELSE("else"),
    FLOAT("float"),
    IF("if"),
    INT("int"),
    RETURN("return"),
    VOID("void"),
    WHILE("while");

    private String value;

    Keyword(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static Keyword getKeyword(String value) {
        for (Keyword k : values()) {
            if (k.getValue().equals(value)) {
                return k;
            }
        }
        return null;
    }

    public static boolean isKeyword(String value) {
        return getKeyword(value) != null;
    }
}
