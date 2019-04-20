package COP4620.codegen.providers;

import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class IterationStatementTestProvider {
    static Stream<Arguments> args() {
        return Stream.of(
                arguments("void main(void) { int x; while(1 > 2){ x = x + 1; } }", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.COMPR, "1", "2", "t1"),
                        new Quadruple(4, Operation.BRLE, "t1", "", "8"),
                        new Quadruple(5, Operation.ADD, "x", "1", "t2"),
                        new Quadruple(6, Operation.ASGN, "t2", "", "x"),
                        new Quadruple(7, Operation.BR, "", "", "3"),
                        new Quadruple(8, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void) { int x; while(1+1 > 2){ x = x + 1; } }", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "1", "t1"),
                        new Quadruple(4, Operation.COMPR, "t1", "2", "t2"),
                        new Quadruple(5, Operation.BRLE, "t2", "", "9"),
                        new Quadruple(6, Operation.ADD, "x", "1", "t3"),
                        new Quadruple(7, Operation.ASGN, "t3", "", "x"),
                        new Quadruple(8, Operation.BR, "", "", "3"),
                        new Quadruple(9, Operation.END, "FUNC", "main", "")
                })
        );
    }
}
