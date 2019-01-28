package COP4620;

public enum SpecialSymbol {
    OPEN_COMMENT("/*"),
    LINE_COMMENT("//"),
    LESS_OR_EQUAL("<="),
    GREATER_OR_EQUAL(">="),
    NOT_EQUAL("!="),
    EQUAL("=="),
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    ASSIGNMENT("="),
    SEMICOLON(";"),
    COMMA(","),
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    LEFT_BRACKET("["),
    RIGHT_BRACKET("]"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}");

    private String value;

    SpecialSymbol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isSpecialSymbol(String value) {
        return getSpecialSymbol(value) != null;
    }

    public static SpecialSymbol getSpecialSymbol(String value) {
        for (SpecialSymbol s : values()) {
            if (s.getValue().equals(value)) {
                return s;
            }
        }
        return null;
    }

    public static String toRegexPatternString() {
        SpecialSymbol[] symbols = values();
        StringBuilder pattern = new StringBuilder(escape(symbols[0].getValue()));
        for (int i = 1; i < symbols.length; i++) {
            pattern.append("|").append(escape(symbols[i].getValue()));
        }
        return pattern.toString();
    }

    private static String escape(String s) {
        return s.replace("[", "\\[")
                .replace("]", "\\]")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace(">", "\\>")
                .replace("<", "\\<")
                .replace("*", "\\*")
                .replace("+", "\\+");
    }
}

