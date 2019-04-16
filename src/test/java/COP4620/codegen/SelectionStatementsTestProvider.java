package COP4620.codegen;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class SelectionStatementsTestProvider {
    static Stream<Arguments> args() {
        return Stream.of(
                arguments("void main(void) { int x; if(1 > 2) {x=5;} }", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.COMPR, "1", "2", "t1"),
                        new Quadruple(4, Operation.BLTE, "t1", "", "6"),
                        new Quadruple(5, Operation.ASGN, "5", "", "x"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void) { int x; if(1 > 2){x=5;}else{x=3;} }", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.COMPR, "1", "2", "t1"),
                        new Quadruple(4, Operation.BLTE, "t1", "", "6"),
                        new Quadruple(5, Operation.ASGN, "5", "", "x"),
                        new Quadruple(6, Operation.BR, "", "", "8"),
                        new Quadruple(7, Operation.ASGN, "3", "", "x"),
                        new Quadruple(8, Operation.END, "FUNC", "main", "")
                })
        );
    }
}
