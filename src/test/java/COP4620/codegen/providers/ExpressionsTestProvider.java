package COP4620.codegen.providers;

import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class ExpressionsTestProvider {
    static Stream<Arguments> args() {
        return Stream.of(
                arguments("void main(void){int x; x=1;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ASGN, "1", "", "x"),
                        new Quadruple(4, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=1+2;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "2", "t1"),
                        new Quadruple(4, Operation.ASGN, "t1", "", "x"),
                        new Quadruple(5, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=1+2+3;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "2", "t1"),
                        new Quadruple(4, Operation.ADD, "t1", "3", "t2"),
                        new Quadruple(5, Operation.ASGN, "t2", "", "x"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x[1]; x[0]=5;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.MUL, "0", "4", "t1"),
                        new Quadruple(4, Operation.DISP, "x", "t1", "t2"),
                        new Quadruple(5, Operation.ASGN, "5", "", "t2"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x[1]; int y; y = x[0];}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(4, Operation.MUL, "0", "4", "t1"),
                        new Quadruple(5, Operation.DISP, "x", "t1", "t2"),
                        new Quadruple(6, Operation.ASGN, "t2", "", "y"),
                        new Quadruple(7, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x[1]; int y; y = x[0]+2;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(4, Operation.MUL, "0", "4", "t1"),
                        new Quadruple(5, Operation.DISP, "x", "t1", "t2"),
                        new Quadruple(6, Operation.ADD, "t2", "2", "t3"),
                        new Quadruple(7, Operation.ASGN, "t3", "", "y"),
                        new Quadruple(8, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x[1]; int y; y = x[2+3];}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(4, Operation.ADD, "2", "3", "t1"),
                        new Quadruple(5, Operation.MUL, "t1", "4", "t2"),
                        new Quadruple(6, Operation.DISP, "x", "t2", "t3"),
                        new Quadruple(7, Operation.ASGN, "t3", "", "y"),
                        new Quadruple(8, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; int y; x=1; y=x;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(4, Operation.ASGN, "1", "", "x"),
                        new Quadruple(5, Operation.ASGN, "x", "", "y"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; int y; x=1; y=1+x;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(4, Operation.ASGN, "1", "", "x"),
                        new Quadruple(5, Operation.ADD, "1", "x", "t1"),
                        new Quadruple(6, Operation.ASGN, "t1", "", "y"),
                        new Quadruple(7, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; int y; x=1; y=x+1;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(4, Operation.ASGN, "1", "", "x"),
                        new Quadruple(5, Operation.ADD, "x", "1", "t1"),
                        new Quadruple(6, Operation.ASGN, "t1", "", "y"),
                        new Quadruple(7, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(void){return f();} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "0"),
                        new Quadruple(2, Operation.CALL, "f", "0", "t1"),
                        new Quadruple(3, Operation.RETURN, "", "", "t1"),
                        new Quadruple(4, Operation.END, "FUNC", "f", ""),
                        new Quadruple(5, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(void){int x; x=1+1; return 0;} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "1", "t1"),
                        new Quadruple(4, Operation.ASGN, "t1", "", "x"),
                        new Quadruple(5, Operation.RETURN, "", "", "0"),
                        new Quadruple(6, Operation.END, "FUNC", "f", ""),
                        new Quadruple(7, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(8, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(void){return 1+1;} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "0"),
                        new Quadruple(2, Operation.ADD, "1", "1", "t1"),
                        new Quadruple(3, Operation.RETURN, "", "", "t1"),
                        new Quadruple(4, Operation.END, "FUNC", "f", ""),
                        new Quadruple(5, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(void){return f()+1;} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "0"),
                        new Quadruple(2, Operation.CALL, "f", "0", "t1"),
                        new Quadruple(3, Operation.ADD, "t1", "1", "t2"),
                        new Quadruple(4, Operation.RETURN, "", "", "t2"),
                        new Quadruple(5, Operation.END, "FUNC", "f", ""),
                        new Quadruple(6, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(7, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(int x){return x;} void main(void){ f(1); }", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "1"),
                        new Quadruple(2, Operation.PARAM, "", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(4, Operation.RETURN, "", "", "x"),
                        new Quadruple(5, Operation.END, "FUNC", "f", ""),
                        new Quadruple(6, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(7, Operation.ARG, "", "", "1"),
                        new Quadruple(8, Operation.CALL, "f", "1", "t1"),
                        new Quadruple(9, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(int x, int y){return x;} void main(void){ f(1, 2); }", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "2"),
                        new Quadruple(2, Operation.PARAM, "", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(4, Operation.PARAM, "", "", "y"),
                        new Quadruple(5, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(6, Operation.RETURN, "", "", "x"),
                        new Quadruple(7, Operation.END, "FUNC", "f", ""),
                        new Quadruple(8, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(9, Operation.ARG, "", "", "1"),
                        new Quadruple(10, Operation.ARG, "", "", "2"),
                        new Quadruple(11, Operation.CALL, "f", "2", "t1"),
                        new Quadruple(12, Operation.END, "FUNC", "main", "")
                })
        );
    }
}
