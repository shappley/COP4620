package COP4620.codegen;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class OrderOfOperationsTestProvider {
    static Stream<Arguments> orderOfOperationsArgs() {
        return Stream.of(
                arguments("void main(void){int x; x=1+2*3;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.MUL, "2", "3", "t1"),
                        new Quadruple(4, Operation.ADD, "1", "t1", "t2"),
                        new Quadruple(5, Operation.ASGN, "t2", "", "x"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=1*2+3;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.MUL, "1", "2", "t1"),
                        new Quadruple(4, Operation.ADD, "t1", "3", "t2"),
                        new Quadruple(5, Operation.ASGN, "t2", "", "x"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=1*2+3*4;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.MUL, "1", "2", "t1"),
                        new Quadruple(4, Operation.MUL, "3", "4", "t2"),
                        new Quadruple(5, Operation.ADD, "t1", "t2", "t3"),
                        new Quadruple(6, Operation.ASGN, "t3", "", "x"),
                        new Quadruple(7, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=(1+2)*3;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "2", "t1"),
                        new Quadruple(4, Operation.MUL, "t1", "3", "t2"),
                        new Quadruple(5, Operation.ASGN, "t2", "", "x"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=1+(2*3);}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.MUL, "2", "3", "t1"),
                        new Quadruple(4, Operation.ADD, "1", "t1", "t2"),
                        new Quadruple(5, Operation.ASGN, "t2", "", "x"),
                        new Quadruple(6, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=(1+2)*(3+4);}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "2", "t1"),
                        new Quadruple(4, Operation.ADD, "3", "4", "t2"),
                        new Quadruple(5, Operation.MUL, "t1", "t2", "t3"),
                        new Quadruple(6, Operation.ASGN, "t3", "", "x"),
                        new Quadruple(7, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void){int x; x=(1+2)*(3+4)+5;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.ADD, "1", "2", "t1"),
                        new Quadruple(4, Operation.ADD, "3", "4", "t2"),
                        new Quadruple(5, Operation.MUL, "t1", "t2", "t3"),
                        new Quadruple(6, Operation.ADD, "t3", "5", "t4"),
                        new Quadruple(7, Operation.ASGN, "t4", "", "x"),
                        new Quadruple(8, Operation.END, "FUNC", "main", "")
                })
        );
    }
}
