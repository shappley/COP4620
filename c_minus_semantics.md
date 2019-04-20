## Semantics
(*) denotes modified for Eggen's spec.
1. All variables and functions must be declared before they are used.
2. The last declaration in a program must be a function declaration with the signature `void main(void)`
3. In a variable declaration, only type specifier `int` and `float` can be used.*
4. If the return type of the function is `void`, then the function returns no value.
5. Functions not declared `void` must return values of the correct type (`int`, `float`).* 
6. There are no parameters of type function (can't pass a `void` type as an argument).
7. No mixed arithmetic (if one operand is `int`, all are `int`; same for `float`).*
8. Array indexes must be type `int`. *
9. Function parameters and arguments must agree in number and type.*