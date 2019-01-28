package COP4620.lexer;

import COP4620.SpecialSymbol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    public static final String TOKEN_PATTERN = "^(?<token>(\\s*(%s)))(.+)?$";
    public static final String CHAR_TOKEN = "[a-zA-Z]+";
    public static final String NUM_TOKEN = "\\d+";
    public static final String LINE_COMMENT = "//.*(\n\r?)?";
    private static final Pattern[] TOKEN_PATTERNS = {
            Pattern.compile(TOKEN_PATTERN.replace("%s", CHAR_TOKEN)),
            Pattern.compile(TOKEN_PATTERN.replace("%s", NUM_TOKEN)),
            Pattern.compile(TOKEN_PATTERN.replace("%s", SpecialSymbol.toRegexPatternString()))
    };

    private String source;

    public Lexer(String source) {
        this.source = source;
    }

    public String getRemainingSource() {
        return this.source;
    }

    public Token getNextToken() {
        final String tokenString = readToken();
        final TokenType tokenType = TokenType.getTypeOf(tokenString);
        return new Token(tokenType, tokenString);
    }

    public Token[] getTokens() {
        final List<Token> tokens = new ArrayList<>();
        String token;
        while ((token = readToken()) != null) {
            final TokenType type = TokenType.getTypeOf(token);
            tokens.add(new Token(type, token));
        }
        return tokens.toArray(new Token[tokens.size()]);
    }

    private String readToken() {
        for (Pattern p : TOKEN_PATTERNS) {
            final Matcher m = p.matcher(source);
            if (m.find()) {
                final String token = m.group("token");
                this.source = this.source.substring(token.length());
                return token.trim();
            }
        }
        return null;
    }

    public void stripComments() {
        this.source = this.source.replaceAll(LINE_COMMENT, "$1");
    }
}