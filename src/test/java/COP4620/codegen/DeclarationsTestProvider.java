package COP4620.codegen;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class DeclarationsTestProvider {
    static Stream<Arguments> declarationsArgs() {
        return Stream.of(
                arguments("void main(void) {int x;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void) {int x[10];}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "40", "", "x"),
                        new Quadruple(3, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void) {float x;}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "4", "", "x"),
                        new Quadruple(3, Operation.END, "FUNC", "main", "")
                }),
                arguments("void main(void) {float x[10];}", new Quadruple[]{
                        new Quadruple(1, Operation.FUNC, "main", "VOID", "0"),
                        new Quadruple(2, Operation.ALLOC, "40", "", "x"),
                        new Quadruple(3, Operation.END, "FUNC", "main", "")
                })
        );
    }
}
