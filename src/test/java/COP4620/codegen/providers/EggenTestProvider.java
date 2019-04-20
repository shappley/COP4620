package COP4620.codegen.providers;

import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class EggenTestProvider {
    static Stream<Arguments> args() {
        return Stream.of(
                arguments("eggen_test_1.txt", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(4, Operation.ALLOC, "4", "", "z"),
                        new Quadruple(5, Operation.ALLOC, "4", "", "m"),
                        new Quadruple(6, Operation.MUL, "3", "y", "t1"),
                        new Quadruple(7, Operation.ADD, "x", "t1", "t2"),
                        new Quadruple(8, Operation.COMPR, "t2", "5", "t3"),
                        new Quadruple(9, Operation.BRLE, "t3", "", "19"),
                        new Quadruple(10, Operation.DIV, "m", "z", "t4"),
                        new Quadruple(11, Operation.ADD, "y", "t4", "t5"),
                        new Quadruple(12, Operation.ASGN, "t5", "", "x"),
                        new Quadruple(13, Operation.SUB, "x", "y", "t6"),
                        new Quadruple(14, Operation.MUL, "z", "m", "t7"),
                        new Quadruple(15, Operation.DIV, "t7", "z", "t8"),
                        new Quadruple(16, Operation.ADD, "t6", "t7", "t9"),
                        new Quadruple(17, Operation.ASGN, "t9", "", "m"),
                        new Quadruple(18, Operation.BR, "", "", "6"),
                        new Quadruple(19, Operation.END, "FUNC", "main", "")
                }),
                arguments("eggen_test_2.txt", new Quadruple[]{
                        new Quadruple(1, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(3, Operation.FUNC, "sub", "INT", "1"),
                        new Quadruple(4, Operation.PARAM, "", "", "z"),
                        new Quadruple(5, Operation.ALLOC, "4", "", "z"),
                        new Quadruple(6, Operation.COMPR, "x", "y", "t1"),
                        new Quadruple(7, Operation.BRLE, "t1", "", "11"),
                        new Quadruple(8, Operation.ADD, "z", "z", "t2"),
                        new Quadruple(9, Operation.RETURN, "", "", "t2"),
                        new Quadruple(10, Operation.BR, "", "", "12"),
                        new Quadruple(11, Operation.ASGN, "5", "", "x"),
                        new Quadruple(12, Operation.END, "FUNC", "sub", ""),

                        new Quadruple(13, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(14, Operation.ARG, "", "", "x"),
                        new Quadruple(15, Operation.CALL, "sub", "1", "t3"),
                        new Quadruple(16, Operation.ASGN, "t3", "", "y"),
                        new Quadruple(17, Operation.END, "FUNC", "main", "")
                }),
                arguments("eggen_test_3.txt", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "40", "", "x"),
                        new Quadruple(3, Operation.ALLOC, "4", "", "y"),
                        new Quadruple(4, Operation.MUL, "5", "4", "t1"),
                        new Quadruple(5, Operation.DISP, "x", "t1", "t2"),
                        new Quadruple(6, Operation.ADD, "t2", "2", "t3"),
                        new Quadruple(7, Operation.MUL, "t3", "y", "t4"),
                        new Quadruple(8, Operation.ASGN, "t4", "", "y"),
                        new Quadruple(9, Operation.END, "FUNC", "main", "")
                })
        );
    }
}
