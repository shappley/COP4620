package COP4620.parser;

import COP4620.lexer.Lexer;
import COP4620.lexer.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserSemanticsTest {
    private static Parser getParser(String source) {
        Lexer lexer = new Lexer(source);
        Token[] tokens = lexer.getTokens();
        return new Parser(tokens);
    }

    private static void test(String source, boolean valid) {
        final Parser parser = getParser(source);
        assertEquals(valid, parser.isValid());
    }

    @DisplayName("Correct Return Type")
    @ParameterizedTest
    @CsvSource({
            "int main(void) { return 4; }, true",
            "float main(void) { return 4.0E-13; }, true",
            "int main(void) { return 4.0E-13; }, false",
            "float main(void) { return 4; }, false"
    })
    void correctReturnType(String source, boolean valid) {
        test(source, valid);
    }

    @DisplayName("Void Returns")
    @ParameterizedTest
    @CsvSource({
            "void main(void) {}, true",
            "void main(void) { return; }, true",
            "void main(void) { return 2; }, false"
    })
    void voidReturns(String source, boolean valid) {
        test(source, valid);
    }
}
