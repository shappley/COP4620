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

    public boolean isValid() {
        return program();
    }

    //Rule #1
    public boolean program() {
        return declarationList();
    }

    //Rule #2
    //declaration-list -> declaration declaration-list-prime
    //declaration-list-prime -> declaration declaration-list-prime | empty
    public boolean declarationList() {
        return (declaration() && declarationListPrime());
    }

    private boolean declarationListPrime() {
        int save = cursor;
        return (declaration() && declarationListPrime())
                || (backtrack(save) && empty());
    }

    //Rule #3
    //declaration -> var-declaration | fun-declaration
    public boolean declaration() {
        int save = cursor;
        return (varDeclaration())
                || (backtrack(save) && funDeclaration());
    }

    //Rule #4
    //var-declaration -> type-specifier ID ; | type-specifier ID [ NUM ] ;
    public boolean varDeclaration() {
        int save = cursor;
        return (typeSpecifier() && match(TokenType.ID) && match(";"))
                || (backtrack(save) && typeSpecifier() && match(TokenType.ID) && match("[") && match(TokenType.NUM) && match("]") && match(";"));
    }

    //Rule #5
    //type-specifier -> int | float | void
    public boolean typeSpecifier() {
        int save = cursor;
        return (match("int"))
                || (backtrack(save) && match("float"))
                || (backtrack(save) && match("void"));
    }

    //Rule #6
    //fun-declaration -> type-specifier ID ( params ) compound-stmt
    public boolean funDeclaration() {
        return typeSpecifier()
                && match(TokenType.ID) && match("(")
                && params() && match(")") && compoundStmt();
    }

    //Rule #7
    //params -> param-list | void
    public boolean params() {
        int save = cursor;
        return (paramList())
                || (backtrack(save) && match("void"));
    }

    //Rule #8
    //param-list -> param param-list-prime
    //param-list-prime -> , param param-list-prime | empty
    public boolean paramList() {
        return param() && paramListPrime();
    }

    private boolean paramListPrime() {
        int save = cursor;
        return (match(",") && param() && paramListPrime())
                || (backtrack(save) && empty());
    }

    //param -> type-specifier ID | type-specifier ID [ ]
    public boolean param() {
        int save = cursor;
        return (typeSpecifier() && match(TokenType.ID))
                || (backtrack(save) && typeSpecifier() && match(TokenType.ID) && match("[") && match("]"));
    }

    //compound-stmt -> { local-declarations statement-list }
    public boolean compoundStmt() {
        return match("{") && localDeclarations() && statementList() && match("}");
    }

    //local-declarations -> var-declaration local-declarations | empty
    public boolean localDeclarations() {
        int save = cursor;
        return (varDeclaration() && localDeclarations())
                || (backtrack(save) && empty());
    }

    //statement-list -> statement statement-list | empty
    public boolean statementList() {
        int save = cursor;
        return (statement() && statementList())
                || (backtrack(save) && empty());
    }

    //statement -> expression-stmt | compound-stmt | selection-stmt | iteration-stmt | return-stmt
    public boolean statement() {
        int save = cursor;
        return (expressionStmt())
                || (backtrack(save) && compoundStmt())
                || (backtrack(save) && selectionStmt())
                || (backtrack(save) && selectionStmt())
                || (backtrack(save) && iterationStmt())
                || (backtrack(save) && returnStmt());
    }

    //expression-stmt -> expression ; | ;
    public boolean expressionStmt() {
        int save = cursor;
        return (expression() && match(";"))
                || (backtrack(save) && match(";"));
    }

    //selection-stmt -> if ( expression ) statement | if ( expression ) statement else statement
    public boolean selectionStmt() {
        int save = cursor;
        return (match("if") && match("(") && expression() && match(")") && statement() && match("else") && statement())
                || (backtrack(save) && match("if") && match("(") && expression() && match(")") && statement());
    }

    //iteration-stmt -> while ( expression ) statement
    public boolean iterationStmt() {
        return match("while") && match("(") && expression() && match(")") && statement();
    }

    //return-stmt -> return ; | return expression ;
    public boolean returnStmt() {
        int save = cursor;
        return (match("return") && match(";"))
                || (backtrack(save) && match("return") && expression() && match(";"));
    }

    //expression -> var = expression | simple-expression
    public boolean expression() {
        int save = cursor;
        return (var() && match("=") && expression())
                || (backtrack(save) && simpleExpression());
    }

    //var -> ID | ID [ expression ]
    public boolean var() {
        int save = cursor;
        return (match(TokenType.ID) && match("[") && expression() && match("]"))
                || (backtrack(save) && match(TokenType.ID));
    }

    //simple-expression -> additive-expression relop additive-expression | additive-expression
    public boolean simpleExpression() {
        int save = cursor;
        return (additiveExpression() && relop() && additiveExpression())
                || (backtrack(save) && additiveExpression());
    }

    //relop -> <= | < | > | >= | == | !=
    public boolean relop() {
        int save = cursor;
        return (match("<="))
                || (backtrack(save) && match("<"))
                || (backtrack(save) && match(">"))
                || (backtrack(save) && match(">="))
                || (backtrack(save) && match("=="))
                || (backtrack(save) && match("!="));
    }

    //additive-expression -> term additive-expression-prime
    //additive-expression-prime -> addop term additive-expression-prime | empty
    public boolean additiveExpression() {
        return term() && additiveExpressionPrime();
    }

    private boolean additiveExpressionPrime() {
        int save = cursor;
        return (addop() && term() && additiveExpressionPrime())
                || (backtrack(save) && empty());
    }

    //term -> factor term-prime
    //term-prime -> mulop factor term-prime | empty
    public boolean term() {
        return factor() && termPrime();
    }

    private boolean termPrime() {
        int save = cursor;
        return (mulop() && factor() && termPrime())
                || (backtrack(save) && empty());
    }

    //addop -> + | -
    public boolean addop() {
        int save = cursor;
        return (match("+"))
                || (backtrack(save) && match("-"));
    }

    //mulop -> * | /
    public boolean mulop() {
        int save = cursor;
        return (match("*"))
                || (backtrack(save) && match("/"));
    }

    //factor -> ( expression ) | var | call | NUM
    public boolean factor() {
        int save = cursor;
        return (match("(") && expression() && match(")"))
                || (backtrack(save) && var())
                || (backtrack(save) && call())
                || (backtrack(save) && match(TokenType.NUM));
    }

    //call -> ID ( args )
    public boolean call() {
        return match(TokenType.ID) && match("(") && args() && match(")");
    }

    //args -> arg-list | empty
    public boolean args() {
        int save = cursor;
        return (argList())
                || (backtrack(save) && empty());
    }

    //arg-list -> expression arg-list-prime
    //arg-list-prime -> , expression arg-list-prime | empty
    public boolean argList() {
        return expression() && argListPrime();
    }

    private boolean argListPrime() {
        int save = cursor;
        return (match(",") && expression() && argListPrime())
                || (backtrack(save) && empty());
    }

    private boolean backtrack(int backtrack) {
        cursor = backtrack;
        return true;
    }

    private boolean empty() {
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
