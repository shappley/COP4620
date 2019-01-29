package COP4620.lexer;

import COP4620.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    public static final String TOKEN_PATTERN = "^(?<token>(\\s*(%s)))(.+)?$";
    public static final String CHAR_TOKEN = "[a-zA-Z]+";
    public static final String NUM_TOKEN = "\\d+(\\.\\d+)?(E(\\+|-)?\\d+)?";
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
        if (tokenString == null) {
            return null;
        }
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
        for (int i = 0; i < TOKEN_PATTERNS.length; i++) {
            final Matcher m = TOKEN_PATTERNS[i].matcher(source);
            if (m.find()) {
                final String token = m.group("token");
                System.out.println("we found " + token);
                final String trimmed = token.trim();
                if (trimmed.equals(SpecialSymbol.LINE_COMMENT.getValue())) {
                    //strip the comment until a newline
                    this.source = this.source.replaceFirst(LINE_COMMENT, "$1");
                    //start the process over to find the next good token
                    i = -1;
                } else if (trimmed.equals(SpecialSymbol.OPEN_COMMENT.getValue())) {
                    this.source = stripBlockComment(this.source);
                    i = -1;
                } else {
                    this.source = this.source.substring(token.length());
                    return trimmed;
                }
            }
        }
        return null;
    }

    private String stripBlockComment(String string) {
        Matcher m = Pattern.compile("(?<!/)\\*/").matcher(string);
        int close = string.length();
        if (m.find()) {
            close = m.start();
        }
        int open = StringUtil.lastIndexLessThan(string, "/*", close);
        String s1 = string.substring(0, open);
        String s2 = string.substring(close + 2);
        return s1.concat(s2);
    }
}