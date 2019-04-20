# COP4620 - Compilers, Spring 2019
I would like to thank Dr. Roger Eggen, without whom this project (and the anxiety caused by it) would not exist.

## Project Structure
This is a complete, four-stage compiler for the C-Minus language.
Each stage of the compilation process corresponded to a project in the course. These projects were:
1. Lexical Analysis
2. Syntactic Analysis
3. Semantic Analysis
4. Intermediate Code Generation

### Lexical Analysis
Lexical Analysis was achieved with regular expressions; 
it finds the longest string that matches a given pattern, assigns a `TokenType`, 
and returns a corresponding `Token`. 
Invalid input is created as a Token with type `ERROR`.

### Syntactic Analysis
Syntactic Analysis accepts a list of Tokens generated by the Lexical Analyzer. 
To verify that the order of the tokens is syntactically correct (that is, it follows the grammar),
it uses a recursive descent parsing technique. 

Unlike LR(1), however, the grammar does not need to be left-factored to remove ambiguity.
The reason for this is that the parser is a *backtracking* parser; 
that is, it matches the production rule for as long as it can but will *backtrack* to the last good match when it fails.
On failure, after it *backtracks*, it will try the next production rule.
As the parser traverses the token list, it builds the parse tree.

### Semantic Analysis
Semantic Analysis is done to verify restrictions that cannot be specified in the grammar.
For example, in most programming languages you cannot assign a string to an integer or vice-versa. 
This is not *semantically* correct and the compiler will complain about it.
C-Minus maintains a list of its own semantics and a few were added to this project by Dr. Eggen. 
These include things such as: 
- variables and functions being declared before they used;
- assigning values to the correct type variable (can't assign a `float` to an `int`);
- function calls having the correct number and type of arguments;

This project achieved Semantic Analysis through a divide-and-conquer technique. 
By their nature, trees are recursive data structures and the parse tree for this project is no different.
Taking advantage of this allowed for an incredibly small amount of code.

The semantic validity of the tree is evaluated using a simple principle: a Node is valid when all of its children are valid.
That is, `program` is valid when `declaration-list` is valid.
`declaration-list` is valid when `declaration` is valid 
and `declaration-list-prime` is valid, and so on.

Each Node type is responsible for evaluating itself for semantic validity then 
calling its children's validity functions.
For example, a function declaration is responsible for checking for duplicate function names, 
adding itself to the symbol table, and then checking the validity of its children (params, compound-stmt).
Each of these is then responsible for checking themselves and their children, and so on.

### Intermediate Code Generation
Intermediate Code Generation is the final step of the compilation process - 
where the source code we started with ends up as assembly-like instructions.

Code Generation also took significant advantage of the recursive nature of the parse tree, 
again exploiting it for a divide-and-conquer technique:
The instructions of a Node are the instructions of its children.
This technique, again, allowed for a a very small amount of code to be written.

As an example, an `if` statement (`selection-statement`) has three major components: 
an `expression` for the condition, a `statement` for the body, 
and optionally a `statement` for the `else` body.
To generate the instructions for this, it:
1. Calls to generate for the condition `expression`
2. Adds a `COMPR` instruction
3. Calls to generate the body `statement`
4. Adds a `BR` instruction (for branching)
4. Calls to generate the else-body `statement`

## Test Suite
At the time of writing, there are 413 tests in this suite with greater than 90% coverage.
All unit tests were written for JUnit 5 and rely on the `jupiter-params` packages for parameterized tests.

Tests are broken into categories corresponding to each stage of the compilation process.
There are then several subcategories for each compilation stage that tests specific functionality.
For example, under Syntactic Analysis, there is a subcategory for `var-declaration` 
which contains several several tests to verify that they match the `var-declaration` grammar rule.