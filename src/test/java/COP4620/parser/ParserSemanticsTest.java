package COP4620.parser;

import COP4620.BaseTest;
import COP4620.lexer.Lexer;
import COP4620.lexer.Token;
import COP4620.parser.semantics.SemanticAnalyzer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserSemanticsTest extends BaseTest {
    private static void test(String source, boolean valid) {
        Lexer lexer = new Lexer(source);
        Token[] tokens = lexer.getTokens();
        Parser parser = new Parser(tokens);
        SemanticAnalyzer semantics = new SemanticAnalyzer(parser.program());
        assertEquals(valid, semantics.isValid());
    }

    //All variables and functions must be declared before they are used.
    @DisplayName("1. Scope resolution")
    @ParameterizedTest
    @CsvSource({
            "void main(void){ int x; x=5;}, true",
            "int x; void main(void){ x=5;}, true",
            "int fun(void){return 1;} void main(void){ int x; x=fun();}, true",
            "void main(void){ if(0) { int x; x=5; } }, true",
            "void main(void){ while(0) { int x; x=5; } }, true",
            "void main(void) { int x; while(1){int x;} }, true",
            "void main(void) { int x; if(1){int x;} }, true",
            "int x(void){return 1;} void main(void){int x;x=x();}, true",
            "int x(void){int x; return 1;} void main(void){int x;x=x();}, true",
            "void main(void){x=5;}, false",
            "void main(void){int x; int x;}, false",
            "void main(void){int x; float x;}, false",
            "void main(void){int x; x=fun();}, false",
            "void main(void){ if(0) { int x; } x=5; }, false",
            "void main(void){ while(0) { int x; } x=5; }, false",
            "int x(void){int x; return 1;} void main(void){x=x();}, false",
            "int x(int x){int x; return x;} void main(void){int x;x=x();}, false",
    })
    void scopeResolution(String source, boolean valid) {
        test(source, valid);
    }

    //The last declaration in a program must be a function declaration with the name `main`
    @DisplayName("2. Main is last declaration")
    @ParameterizedTest
    @CsvSource({
            "int x; void main(void){}, true",
            "void main(void){}, true",
            "void main(void){ int x; x=5; }, true",
            "void x(void){} void main(void){}, true",
            "void main(void){} int x;, false",
            "void main(void){} void x(void){}, false",
            "void fun(void){}, false"
    })
    void mainLastDeclaration(String source, boolean valid) {
        test(source, valid);
    }

    //In a variable declaration, only type specifier `int` and `float` can be used.
    @DisplayName("3. Variable declaration types")
    @ParameterizedTest
    @CsvSource({
            "void main(void){int x;}, true",
            "void main(void){float x;}, true",
            "void main(void){int x[0];}, true",
            "void main(void){float x[0];}, true",
            "void main(void){void x;}, false",
            "void main(void){void x[0];}, false",
    })
    void variableDeclarationTypes(String source, boolean valid) {
        test(source, valid);
    }

    //Functions not declared `void` must return values of the correct type (`int`, `float`).
    //If the return type of the function is `void`, then the function returns no value.
    @DisplayName("4. Correct return type")
    @ParameterizedTest
    @CsvSource({
            "void main(void) {}, true",
            "void main(void) { return; }, true",
            "int f(void){return 4;} void main(void){}, true",
            "int f(void){int x[0]; return x[0];} void main(void){}, true",
            "float f(void){return 4.0E-13;} void main(void){}, true",
            "int f(void) {return f();} void main(void){}, true",
            "int f(void){return f()+1*f();} void main(void){}, true",
            "int f(void) { return 4.0E-13; } void main(void){}, false",
            "float f(void) { return 4; } void main(void){}, false",
            "void main(void) { return 2; }, false",
            "int f(void) { int x[0]; return x; } void main(void){}, false",
            "int f(void) { return; } void main(void){}, false",
            "int f(void) {} void main(void){}, false"
    })
    void correctReturnType(String source, boolean valid) {
        test(source, valid);
    }

    //No mixed arithmetic (if one operand is `int`, all are `int`; same for `float`).
    @DisplayName("5. Arithmetic")
    @ParameterizedTest
    @CsvSource({
            "void main(void) { int x; x=1+2; },true",
            "void main(void){ int x; x=1+2+3; }, true",
            "void main(void){ float x; x=1.0+2.0+3.0; }, true",
            "void main(void){ int a; int b; int c; a=5; b=11; c=a+b; }, true",
            "void main(void){ float a; float b; float c; a=5.0; b=11.0; c=a+b; }, true",
            "int b(void) { return 1; } void main(void){ int a; int c; a=5; c=a+b(); }, true",
            "float b(void) { return 1.0; } void main(void){ float a; float c; a=5.0; c=a+b(); }, true",
            "void main(void){ int x; x=1+2+3.0; }, false",
            "void main(void){ float x; x=1.0+2.0+3; }, false",
            "void main(void){ int a; float b; int c; a=5; b=11; c=a+b; }, false",
            "void main(void){ int a; int b; float c; a=5; b=11; c=a+b; }, false",
            "float b(void) { return 1.0; } void main(void){ int a; int c; a=5; c=a+b(); }, false",
            "void b(void){} void main(void){ int a; a=1+b(); }, false",
            "void a(void){} void main(void) { a() + a(); }, false"
    })
    void mixedArithmetic(String source, boolean valid) {
        test(source, valid);
    }

    //Array indexes must be type `int`.
    @DisplayName("6. Array indexes")
    @ParameterizedTest
    @CsvSource({
            "void main(void) { int x[0]; x[1] = 5;}, true",
            "int size(void) {return 1;} void main(void) { int x[1]; x[size()] = 5;}, true",
            "int size(void) {return 1;} void main(void) { int x[1]; x[size()+1] = 5;}, true",
            "void main(void) { int x[1.0]; x[1] = 5;}, false",
            "void main(void) { int x[1]; x[1.0] = 5;}, false",
            "float size(void) {return 1.0;} void main(void) { int x[1]; x[size()] = 5;}, false",
            "float size(void) {return 1.0;} void main(void) { int x[1]; x[size()+1.0] = 5;}, false"
    })
    void arrayIndexes(String source, boolean valid) {
        test(source, valid);
    }

    //Function parameters and arguments must agree in number and type.
    @DisplayName("7. Function arguments")
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
            "void fun(void){} void main(void){fun(main());}, false"
    })
    void functionArguments(String source, boolean valid) {
        test(source, valid);
    }

    @TestFactory
    @DisplayName("8. Test Files")
    List<DynamicTest> testFiles() {
        List<DynamicTest> tests = buildFileTestsForDirectory("semantics/ACCEPT", true);
        tests.addAll(buildFileTestsForDirectory("semantics/REJECT", false));
        return tests;
    }

    private List<DynamicTest> buildFileTestsForDirectory(String dir, boolean expected) {
        List<DynamicTest> tests = new ArrayList<>();
        File[] files = getResourceFilesInDirectory(dir);
        for (File f : files) {
            tests.add(DynamicTest.dynamicTest("File " + f.getName() + ", expected " + expected, () -> {
                String source = getSource(f.getAbsolutePath());
                test(source, expected);
            }));
        }
        return tests;
    }
}
