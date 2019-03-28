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

@DisplayName("3. Semantic Analysis")
class ParserSemanticsTest extends BaseTest {
    private static void test(String source, boolean valid) {
        Lexer lexer = new Lexer(source);
        Token[] tokens = lexer.getTokens();
        Parser parser = new Parser(tokens);
        SemanticAnalyzer semantics = new SemanticAnalyzer(parser.program());
        assertEquals(valid, semantics.isValid());
    }

    //All variables and functions must be declared before they are used.
    @DisplayName("1. Function, Variable, and Scope Resolution")
    @ParameterizedTest(name = "1.{index}. {2} (valid = {1})")
    @CsvSource({
            "void main(void){ int x; x=5;}, true, 'Declaration and initialization in same scope'",
            "int x; void main(void){ x=5;}, true, 'Declaration in global scope'",
            "int fun(void){return 1;} void main(void){ int x; x=fun();}, true, 'Calling function'",
            "void main(void){ int x; if(0) { x=5; } }, true, 'Initialization in if-statement'",
            "void main(void){ int x; while(0) { x=5; } }, true, 'Initialization in while-statement'",
            "void main(void) { int x; if(1){int x;} }, true, 'Variable of same name in if-statement'",
            "void main(void) { int x; while(1){int x;} }, true, 'Variable of same name in while-statement'",
            "int x(void){int x; return 1;} void main(void){int x;x=x();}, true, 'Variable of same name in different function'",
            "int x(void){return 1;} void main(void){int x;x=x();}, true, 'Function and variable of same name'",
            "void main(void){ int x; { int x; { int x; } } }, true, 'Variable of same name in nested Compound Statements'",
            "void main(void){x=5;}, false, 'Variable initialized without being declared'",
            "void main(void){x=5;int x;}, false, 'Variable initialized before being declared'",
            "void main(void){int x; int x;}, false, 'Variable of same name and type in same scope'",
            "void main(void){int x; float x;}, false, 'Variable of same name but different types in same scope'",
            "void main(void){int x; x=fun();}, false, 'Function not declared before call'",
            "void main(void){ if(0) { int x; } x=5; }, false, 'Variable declared in if-statement initialized in outer scope'",
            "void main(void){ while(0) { int x; } x=5; }, false, 'Variable declared in while-statement initialized in outer scope'",
            "void main(void){ {int x;} x=5; }, false, 'Variable declared in compound statement initialized in outer scope'",
            "void foo(int x){int x;} void main(void){}, false, 'Variable declared with same name as parameter'"
    })
    void scopeResolution(String source, boolean valid, String description) {
        test(source, valid);
    }

    //The last declaration in a program must be a function declaration with the signature `void main(void)`
    @DisplayName("2. Main Declarations")
    @ParameterizedTest(name = "2.{index}. {2} (valid = {1})")
    @CsvSource({
            "void main(void){}, true, 'Main only declaration'",
            "int x; void main(void){}, true, 'Variable declared before main in global scope'",
            "void main(void){ int x; x=5; }, true, 'Declarations inside main'",
            "void x(void){} void main(void){}, true, 'Function declared before main'",
            "void main(void){} int x;, false, 'Variable declaration after main in global scope'",
            "void main(void){} void x(void){}, false, 'Function declared after main'",
            "void fun(void){}, false, 'No main function declared'",
            "void main(void){} void main(void){}, false, 'Two main functions declared'",
            "int main(void){return 1;}, false, 'Main function not of type VOID'",
            "void main(int x){}, false, 'Main function declared with non-VOID parameter'"
    })
    void mainFunctionDeclarations(String source, boolean valid, String description) {
        test(source, valid);
    }

    //In a variable declaration, only type specifier `int` and `float` can be used.
    @DisplayName("3. Variable Declarations")
    @ParameterizedTest(name = "3.{index}. {2} (valid = {1})")
    @CsvSource({
            "void main(void){int x;}, true, 'INT declaration'",
            "void main(void){float x;}, true, 'FLOAT declaration'",
            "void main(void){int x[0];}, true, 'INT-ARRAY declaration'",
            "void main(void){float x[0];}, true, 'FLOAT-ARRAY array declaration'",
            "void main(void){ int x; x=5;}, true, 'INT initialized with INT literal'",
            "void main(void){ float x; x=5.0;}, true, 'FLOAT initialized with FLOAT literal'",
            "void main(void){ int x[0]; x[0]=5;}, true, 'INT-ARRAY index initialized with INT literal'",
            "void main(void){ float x[0]; x[0]=5.0;}, true, 'FLOAT-ARRAY index initialized with FLOAT literal'",
            "void main(void){void x;}, false, 'VOID declaration'",
            "void main(void){void x[0];}, false, 'VOID-ARRAY declaration'",
            "void main(void){ int x; x=5.0;}, false, 'INT initialized with FLOAT literal'",
            "void main(void){ float x; x=5;}, false, 'FLOAT initialized with INT literal'",
            "void main(void){ int x[0]; x=5.0;}, false, 'INT-ARRAY index initialized with FLOAT literal'",
            "void main(void){ float x; x[0]=5;}, false, 'FLOAT-ARRAY index initialized with INT literal'",
            "void main(void) { int x; 5=x;}, false, 'INT literal on left side'",
            "int f(void){return 1;} void main(void) { int x; f=x;}, false, 'Function on left side'",
            "int f(void){return 1;} void main(void) { int x; f()=x;}, false, 'Function call on left side'"
    })
    void variableDeclarationTypes(String source, boolean valid, String description) {
        test(source, valid);
    }

    //Functions not declared `void` must return values of the correct type (`int`, `float`).
    //If the return type of the function is `void`, then the function returns no value.
    @DisplayName("4. Function Return Types")
    @ParameterizedTest(name = "4.{index}. {2} (valid = {1})")
    @CsvSource({
            "void main(void) {}, true, 'VOID function with no RETURN'",
            "void main(void) { return; }, true, VOID function with RETURN but no value",
            "int f(void){return 4;} void main(void){}, true, 'INT function returns INT literal'",
            "int f(void){int x[0]; return x[0];} void main(void){}, true, 'INT function returns INT-ARRAY index'",
            "float f(void){float x[0]; return x[0];} void main(void){}, true, 'FLOAT function returns FLOAT-ARRAY index'",
            "float f(void){return 4.0E-13;} void main(void){}, true, 'FLOAT function returns FLOAT literal'",
            "int f(void) {return f();} void main(void){}, true, 'INT function returns INT function call'",
            "float f(void) {return f();} void main(void){}, true, 'FLOAT function returns FLOAT function call'",
            "int f(void){return f()+1*f();} void main(void){}, true, 'INT function returns mathematical expression'",
            "int f(void){if(0){return 1;}} void main(void){f();}, true, 'Return inside if-block'",
            "int f(void){while(0){return 1;}} void main(void){f();}, true, 'Return inside while-block'",
            "int f(void) { return 4.0E-13; } void main(void){}, false, 'INT function returns FLOAT literal'",
            "float f(void) { return 4; } void main(void){}, false, 'FLOAT function returns INT literal'",
            "void main(void) { return 2; }, false, 'VOID function returns INT'",
            "int f(void) { int x[0]; return x; } void main(void){}, false, 'INT function returns INT-ARRAY'",
            "int f(void) { return; } void main(void){}, false, 'INT function with RETURN but no value'",
            "int f(void) {} void main(void){}, false, 'INT function with no RETURN'"
    })
    void correctReturnType(String source, boolean valid, String description) {
        test(source, valid);
    }

    //No mixed arithmetic (if one operand is `int`, all are `int`; same for `float`).
    @DisplayName("5. Operator/Operand Agreement")
    @ParameterizedTest(name = "5.{index}. {2} (valid = {1})")
    @CsvSource({
            "void main(void) { int x; x=1+2+3; },true, 'INT initialized with sum of INT literals'",
            "void main(void){ float x; x=1.0+2.0+3.0; }, true, 'FLOAT initialized with sum of FLOAT literals'",
            "void main(void){ int a; int b; int c; a=5; b=11; c=a+b; }, true, 'INT initialized with sum of INT variables'",
            "void main(void){ float a; float b; float c; a=5.0; b=11.0; c=a+b; }, true, 'FLOAT initialized with sum of FLOAT literals'",
            "int b(void) { return 1; } void main(void){ int a; int c; a=5; c=a+b(); }, true, 'INT initialized with sum of INT functions'",
            "float b(void) { return 1.0; } void main(void){ float a; float c; a=5.0; c=a+b(); }, true, 'FLOAT initialized with sum of FLOAT functions'",
            "void main(void){ int x; x=1+2+3.0; }, false, 'INT initialized with sum of mixed literals'",
            "void main(void){ float x; x=1.0+2.0+3; }, false, 'FLOAT initialized with sum of mixed literals'",
            "void main(void){ int a; float b; int c; a=5; b=11; c=a+b; }, false, 'INT initialized with sum of mixed variables'",
            "void main(void){ int a; int b; float c; a=5; b=11; c=a+b; }, false, 'FLOAT initialized with sum of mixed variables'",
            "float b(void) { return 1.0; } void main(void){ int a; int c; a=5; c=a+b(); }, false, 'INT initialized with sum of mixed functions'",
            "void b(void){} void main(void){ int a; a=1+b(); }, false, 'Sum INT and VOID'",
            "void a(void){} void main(void) { a() + a(); }, false, 'Sum VOID and VOID'"
    })
    void operandAgreement(String source, boolean valid, String description) {
        test(source, valid);
    }

    //Array indexes must be type `int`.
    @DisplayName("6. Array Indexes")
    @ParameterizedTest(name = "6.{index}. {2} (valid = {1})")
    @CsvSource({
            "void main(void) { int x[0]; x[1] = 5;}, true, 'Array indexed with INT literal'",
            "void main(void) { int x[0]; int i; x[i] = 5;}, true, 'Array indexed with INT variable'",
            "int size(void) {return 1;} void main(void) { int x[1]; x[size()] = 5;}, true, 'Array indexed with INT function'",
            "int size(void) {return 1;} void main(void) { int x[1]; x[size()+1] = 5;}, true, 'Array indexed with sum of INT functions'",
            "void main(void) { int x[1.0]; x[1] = 5;}, false, 'Array size specified with FLOAT literal'",
            "void main(void) { int x[1]; x[1.0] = 5;}, false, 'Array indexed with FLOAT literal'",
            "void main(void) { int x[1]; float i; x[i] = 5;}, false, 'Array indexed with FLOAT variable'",
            "float size(void) {return 1.0;} void main(void) { int x[1]; x[size()] = 5;}, false, 'Array indexed with FLOAT function'",
            "float size(void) {return 1.0;} void main(void) { int x[1]; x[size()+1.0] = 5;}, false, 'Array indexed with sum of FLOAT functions'"
    })
    void arrayIndexes(String source, boolean valid, String description) {
        test(source, valid);
    }

    //Function parameters and arguments must agree in number and type.
    @DisplayName("7. Function Parameter/Argument Agreement")
    @ParameterizedTest(name = "7.{index}. {2} (valid = {1})")
    @CsvSource({
            "void main(void) { main(); }, true, 'VOID parameters function called with no arguments'",
            "'int fun(int x){return x;} void main(void){fun(5);}', true, 'INT parameter function called with INT literal'",
            "'int fun(float x){return 1;} void main(void){fun(1.0);}', true, 'FLOAT parameter function called with FLOAT literal'",
            "'int fun(int x){return x;} void main(void){int x; fun(x);}', true, 'INT parameter function called with INT variable'",
            "'int fun(float x){return 1;} void main(void){float x; fun(x);}', true, 'FLOAT parameter function called with FLOAT variable'",
            "'int fun(int x){return 1;} void main(void){fun(fun(1));}', true, 'INT parameter function called with INT function'",
            "'float fun(float x){return 1.0;} void main(void){ fun(fun(1.0)); }', true, 'FLOAT parameter function called with FLOAT function'",
            "'int fun(int x, int y){return 1;} void main(void){fun(1,2);}', true, '(INT, INT) parameter function called with INT literals'",
            "'int fun(int x, float y){return 1;} void main(void){fun(1, 1.0);}', true, '(INT, FLOAT) parameter function called with (INT, FLOAT) literals'",
            "'int fun(int x){return 1;} void main(void){fun();}', false, 'INT parameter function called with no arguments'",
            "'int fun(void){return 1;} void main(void){fun(1);}', false, 'VOID parameters function called with INT literal'",
            "'int fun(int x){return 1;} void main(void){fun(1.0);}', false, 'INT parameter function called with FLOAT literal'",
            "'int fun(float x){return 1;} void main(void){fun(1);}', false, 'FLOAT parameter function called with INT literal'",
            "'int fun(int x, float y){return 1;} void main(void){fun(1);}', false, '(INT, FLOAT) parameter function called with (INT)'",
            "'int fun(int x, float y){return 1;} void main(void){fun(1.0, 1);}', false, '(INT, FLOAT) parameter function called with (FLOAT, INT)'",
            "'int fun(int x, int y){return 1;} void main(void){fun(1, 2, 3);}', false, '(INT, INT) parameter function called with (INT, INT, INT)'",
            "void main(void) { main(main()); }, false, 'VOID parameter function called with VOID function'"
    })
    void functionArguments(String source, boolean valid, String description) {
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
