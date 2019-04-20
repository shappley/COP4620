package COP4620.codegen.providers;

import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class FunctionDeclarationTestProvider {
    static Stream<Arguments> args() {
        return Stream.of(
                arguments("void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(void){return 1;} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "0"),
                        new Quadruple(2, Operation.RETURN, "", "", "1"),
                        new Quadruple(3, Operation.END, "FUNC", "f", ""),
                        new Quadruple(4, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(5, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(void){ int x; return x;} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.RETURN, "", "", "x"),
                        new Quadruple(4, Operation.END, "FUNC", "f", ""),
                        new Quadruple(5, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("float f(void){ float x; return x;} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "FLOAT", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.RETURN, "", "", "x"),
                        new Quadruple(4, Operation.END, "FUNC", "f", ""),
                        new Quadruple(5, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(int x){return x;} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "1"),
                        new Quadruple(2, Operation.PARAM, "", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(4, Operation.RETURN, "", "", "x"),
                        new Quadruple(5, Operation.END, "FUNC", "f", ""),
                        new Quadruple(6, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(7, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(int x, int y){return x;} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "2"),
                        new Quadruple(2, Operation.PARAM, "", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(4, Operation.PARAM, "", "", "y"),
                        new Quadruple(5, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(6, Operation.RETURN, "", "", "x"),
                        new Quadruple(7, Operation.END, "FUNC", "f", ""),
                        new Quadruple(8, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(9, Operation.END, "FUNC", "main", "")
                })
        );
    }
}
