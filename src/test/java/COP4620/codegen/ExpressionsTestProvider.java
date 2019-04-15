package COP4620.codegen;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class ExpressionsTestProvider {
    static Stream<Arguments> expressionsArgs() {
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
                        new Quadruple(2, Operation.CALL, "f", "", "t1"),
                        new Quadruple(3, Operation.RETURN, "", "", "t1"),
                        new Quadruple(4, Operation.END, "FUNC", "f", ""),
                        new Quadruple(5, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("int f(void){return f()+1;} void main(void){}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "f", "INT", "0"),
                        new Quadruple(2, Operation.CALL, "f", "", "t1"),
                        new Quadruple(2, Operation.ADD, "t1", "1", "t2"),
                        new Quadruple(3, Operation.RETURN, "", "", "t2"),
                        new Quadruple(4, Operation.END, "FUNC", "f", ""),
                        new Quadruple(5, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                })
        );
    }
}
