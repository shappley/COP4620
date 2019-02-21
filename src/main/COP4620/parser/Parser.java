package COP4620.parser;

import COP4620.lexer.Token;
import COP4620.lexer.TokenType;

public class Parser {
    private int cursor = 0;
    private int backtrack = 0;

    private Token[] tokens;

    public Parser(Token[] tokens) {
        this.tokens = tokens;
    }

    //Rule #1
    public boolean program() {
        return save() && declarationList();
    }

    //Rule #2
    //declaration-list -> declaration declaration-list-prime
    //declaration-list-prime -> declaration declaration-list-prime | empty
    public boolean declarationList() {
        return save() && declaration() && declarationListPrime();
    }

    private boolean declarationListPrime() {
        return save() && declaration() && declarationListPrime();
    }

    //Rule #3
    //declaration -> var-declaration | fun-declaration
    public boolean declaration() {
        return (save() && varDeclaration())
                || (backtrack() && save() && funDeclaration());
    }

    //Rule #4
    //var-declaration -> type-specifier ID ; | type-specifier ID [ NUM ] ;
    public boolean varDeclaration() {
        return ((save() && typeSpecifier() && match(TokenType.ID) && match(";"))
                || (backtrack() && save() && typeSpecifier() && match(TokenType.ID) && match("[") && match(TokenType.NUM) && match("]") && match(";")));
    }

    //Rule #5
    //type-specifier -> int | float | void
    public boolean typeSpecifier() {
        return (save() && match("int")
                || (backtrack() && save() && match("float"))
                || (backtrack() && save() && match("void")));
    }

    //Rule #6
    //fun-declaration -> type-specifier ID ( params ) compound-stmt
    public boolean funDeclaration() {
        return save() && typeSpecifier()
                && match(TokenType.ID) && match("(")
                && params() && match(")") && compoundStmt();
    }

    //Rule #7
    //params -> param-list | void
    public boolean params() {
        return (save() && paramList())
                || (backtrack() && save() && match("void"));
    }

    //Rule #8
    //param-list -> param param-list-prime
    //param-list-prime -> , param param-list-prime | empty
    public boolean paramList() {
        return false;
    }

    //param -> type-specifier ID | type-specifier ID [ ]
    public boolean param() {
        return false;
    }

    //compound-stmt -> { local-declarations statement-list }
    public boolean compoundStmt() {
        return false;
    }

    //local-declarations -> var-declarations local-declarations | empty
    public boolean localDeclarations() {
        return false;
    }

    //statement-list -> statement statement-list | empty
    public boolean statementList() {
        return false;
    }

    //statement -> expression-stmt | compound-stmt | selection-stmt | iteration-stmt | return-stmt
    public boolean statement() {
        return false;
    }

    //expression-stmt -> expression ; | ;
    public boolean expressionStmt() {
        return false;
    }

    //selection-stmt -> if ( expression ) statement | if ( expression ) statement else statement
    public boolean selectionStmt() {
        return false;
    }

    //iteration-stmt -> while ( expression ) statement
    public boolean iterationStmt() {
        return false;
    }

    //return-stmt -> return ; | return expression ;
    public boolean returnStmt() {
        return false;
    }

    //expression -> var = expression | simple-expression
    public boolean expression() {
        return false;
    }

    //var -> ID | ID [ expression ]
    public boolean var() {
        return false;
    }

    //simple-expression -> additive-expression relop additive-expression | additive-expression
    public boolean simpleExpression() {
        return false;
    }

    //relop -> <= | < | > | >= | == | !=
    public boolean relop() {
        return false;
    }

    //additive-expression -> term additive-expression-prime
    //additive-expression-prime -> addop term additive-expression-prime | empty
    public boolean additiveExpression() {
        return false;
    }

    private boolean additiveExpressionPrime() {
        return false;
    }

    //term -> factor term-prime
    //term-prime -> mulop factor term-prime | empty
    public boolean term() {
        return false;
    }

    private boolean termPrime() {
        return false;
    }

    //addop -> + | -
    public boolean addop() {
        return false;
    }

    //mulop -> * | /
    public boolean mulop() {
        return false;
    }

    //factor -> ( expression ) | var | call | NUM
    public boolean factor() {
        return false;
    }

    //call -> ID ( args )
    public boolean call() {
        return false;
    }

    //args -> arg-list | empty
    public boolean args() {
        return false;
    }

    //arg-list -> expression arg-list-prime
    //arg-list-prime -> , expression arg-list-prime | empty
    public boolean argList() {
        return false;
    }

    private boolean argListPrime() {
        return false;
    }

    private boolean save() {
        backtrack = cursor;
        return true;
    }

    private boolean backtrack() {
        cursor = backtrack;
        return true;
    }

    private boolean match(String expected) {
        final Token token = nextToken();
        return token != null && expected.equals(token.getValue());
    }

    private boolean match(TokenType expected) {
        final Token token = nextToken();
        return token != null && expected == token.getType();
    }

    private Token nextToken() {
        if (cursor == tokens.length) {
            return null;
        }
        return tokens[cursor++];
    }
}
