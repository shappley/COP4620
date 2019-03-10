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

    //1. All variables and functions must be declared before they are used.
    @DisplayName("1. References declared before use")
    @ParameterizedTest
    @CsvSource({
            "void main(void){ int x; x=5;}, true",
            "int fun(void){return 1;} void main(void){ int x; x=fun();}, true",
            "void main(void){x=5;}, false",
            "void main(void){x=fun();}, false"
    })
    void referencesDeclared(String source, boolean valid) {
        test(source, valid);
    }

    //2. The last declaration in a program must be a function declaration with the name `main`
    @DisplayName("2. Main is last declaration")
    @ParameterizedTest
    @CsvSource({
            "void main(void){}, true",
            "void main(void){ int x; x=5; }, true",
            "void x(void){} void main(void){}, true",
            "void main(void){} int x;, false",
            "void main(void){} void x(void){}, false"
    })
    void mainLastDeclaration(String source, boolean valid) {
        test(source, valid);
    }

    //3. In a variable declaration, only type specifier `int` and `float` can be used.
    @DisplayName("3. Variable declaration types")
    @ParameterizedTest
    @CsvSource({
            "void main(void){int x;}, true",
            "void main(void){float x;}, true",
            "void main(void){void x;}, false"
    })
    void variableDeclarationTypes(String source, boolean valid) {
        test(source, valid);
    }

    //4. If the return type of the function is `void`, then the function returns no value.
    @DisplayName("4. Void returns")
    @ParameterizedTest
    @CsvSource({
            "void main(void) {}, true",
            "void main(void) { return; }, true",
            "void main(void) { return 2; }, false"
    })
    void voidReturns(String source, boolean valid) {
        test(source, valid);
    }

    //5. Functions not declared `void` must return values of the correct type (`int`, `float`).
    @DisplayName("5. Correct return type")
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

    //6. There are no parameters of type function (can't pass a `void` type as an argument).
    @DisplayName("6. Void parameters")
    @ParameterizedTest
    @CsvSource({
            "void fun(void){} void main(void){fun(main());}, false"
    })
    void voidParameters(String source, boolean valid) {
        test(source, valid);
    }

    //7. No mixed arithmetic (if one operand is `int`, all are `int`; same for `float`).
    @DisplayName("7. Mixed Arithmetic")
    @ParameterizedTest
    @CsvSource({
            "void main(void){ int a; int b; int c; a=5; b=11; c=a+b; }, true",
            "void main(void){ float a; float b; float c; a=5; b=11; c=a+b; }, true",
            "int b(void) { return 1; } void main(void){ int a; int c; a=5; c=a+b(); }, true",
            "float b(void) { return 1.0; } void main(void){ float a; float c; a=5; c=a+b(); }, true",
            "void main(void){ int a; float b; int c; a=5; b=11; c=a+b; }, false",
            "void main(void){ int a; int b; float c; a=5; b=11; c=a+b; }, false",
            "float b(void) { return 1.0; } void main(void){ int a; int c; a=5; c=a+b(); }, false",
    })
    void mixedArithmetic(String source, boolean valid) {
        test(source, valid);
    }

    //8. Array indexes must be type `int`.
    @DisplayName("8. Array indexes")
    @ParameterizedTest
    @CsvSource({
            "void main(void) { int x[1]; x[1] = 5;}, true",
            "int size(void) {return 1;} void main(void) { int x[size()]; x[0] = 5;}, true",
            "int size(void) {return 1;} void main(void) { int x[size()+1]; x[0] = 5;}, true",
            "int size(void) {return 1;} void main(void) { int x[1]; x[size()] = 5;}, true",
            "int size(void) {return 1;} void main(void) { int x[1]; x[size()+1] = 5;}, true",
            "void main(void) { int x[1.0]; x[1] = 5;}, false",
            "void main(void) { int x[1]; x[1.0] = 5;}, false",
            "float size(void) {return 1.0;} void main(void) { int x[size()]; x[0] = 5;}, false",
            "float size(void) {return 1.0;} void main(void) { int x[1]; x[size()] = 5;}, false",
            "float size(void) {return 1.0;} void main(void) { int x[size()+1.0]; x[0] = 5;}, false",
            "float size(void) {return 1.0;} void main(void) { int x[1]; x[size()+1.0] = 5;}, false"
    })
    void arrayIndexes(String source, boolean valid) {
        test(source, valid);
    }

    //9. Function parameters and arguments must agree in number and type.
    @DisplayName("9. Function arguments")
    @ParameterizedTest
    @CsvSource({
            "'int fun(void){return 1;} void main(void){fun();}', true",
            "'int fun(int x){return x;} void main(void){fun(5);}', true",
            "'int fun(float x){return 1;} void main(void){fun(1.0);}', true",
            "'int fun(int x, int y){return 1;} void main(void){fun(1,2);}', true",
            "'int fun(int x, float y){return 1;} void main(void){fun(1, 1.0);}', true",
            "'int fun(int x){return 1;} void main(void){fun(fun());}', true",
            "'int fun(void){return 1;} void main(void){fun(1);}', false",
            "'int fun(int x){return 1;} void main(void){fun(1.0);}', false",
            "'int fun(float x){return 1;} void main(void){fun(1);}', false",
            "'int fun(int x, float y){return 1;} void main(void){fun(1.0, 1);}', false",
            "'int fun(int x, int y){return 1;} void main(void){fun(1, 2, 3);}', false",
    })
    void functionArguments(String source, boolean valid) {
        test(source, valid);
    }
}
