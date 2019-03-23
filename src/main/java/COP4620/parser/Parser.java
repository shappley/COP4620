package COP4620.parser;

import COP4620.lexer.Token;
import COP4620.lexer.TokenType;
import COP4620.parser.semantics.Node;
import COP4620.parser.semantics.nodes.*;

import static COP4620.util.ArrayUtil.addArrays;
import static COP4620.util.ArrayUtil.asArray;

public class Parser {
    private Token[] tokens;
    private int cursor = 0;

    public Parser(Token[] tokens) {
        this.tokens = tokens;
    }

    public boolean isValid() {
        return program() != null && isDone();
    }

    public boolean isDone() {
        return cursor == tokens.length;
    }

    //Rule #1
    public Program program() {
        DeclarationList declarationList = declarationList();
        if (declarationList != null) {
            return new Program(declarationList);
        }
        return null;
    }

    //Rule #2
    //declaration-list -> declaration declaration-list | declaration
    public DeclarationList declarationList() {
        int save = cursor;
        Declaration declaration = declaration();
        if (declaration != null) {
            DeclarationList declarationList = declarationList();
            if (declarationList != null) {
                return new DeclarationList(addArrays(asArray(declaration), declarationList.getDeclarations()));
            }
        }
        backtrack(save);
        return null;
    }

    //Rule #3
    //declaration -> var-declaration | fun-declaration
    public Declaration declaration() {
        int save = cursor;
        Node node = varDeclaration();
        if (node != null) {
            return new Declaration(Declaration.Type.VARIABLE, node);
        }
        backtrack(save);
        node = funDeclaration();
        if (node != null) {
            return new Declaration(Declaration.Type.FUNCTION, node);
        }
        return null;
    }

    //Rule #4
    //var-declaration -> type-specifier ID ; | type-specifier ID [ NUM ] ;
    public VarDeclaration varDeclaration() {
        TypeSpecifier type = typeSpecifier();
        if (type != null && check(TokenType.ID)) {
            String id = nextToken().getValue();
            if (check(";")) {
                nextToken();
                return new VarDeclaration(type, id);
            } else if (check("[") && check(1, TokenType.NUM) && check(2, "]") && check(3, ";")) {
                nextToken();
                String size = nextToken().getValue();
                nextToken();
                nextToken();
                return new VarDeclaration(type, id, size);
            }
        }
        return null;
    }

    //Rule #5
    //type-specifier -> int | float | void
    public TypeSpecifier typeSpecifier() {
        if (check("int")) {
            return new TypeSpecifier(TypeSpecifier.Type.INT, nextToken().getValue());
        } else if (check("float")) {
            return new TypeSpecifier(TypeSpecifier.Type.FLOAT, nextToken().getValue());
        } else if (check("void")) {
            return new TypeSpecifier(TypeSpecifier.Type.VOID, nextToken().getValue());
        }
        return null;
    }

    //Rule #6
    //fun-declaration -> type-specifier ID ( params ) compound-stmt
    public FunDeclaration funDeclaration() {
        TypeSpecifier type = typeSpecifier();
        if (type != null && check(TokenType.ID)) {
            String id = nextToken().getValue();
            if (match("(")) {
                Params params = params();
                if (params != null && match(")")) {
                    CompoundStmt body = compoundStmt();
                    if (body != null) {
                        return new FunDeclaration(type, id, params, body);
                    }
                }
            }
        }
        return null;
    }

    //Rule #7
    //params -> param-list | void
    public Params params() {
        int save = cursor;
        ParamList paramList = paramList();
        if (paramList != null) {
            return new Params(paramList);
        } else if (backtrack(save) && match("void")) {
            return new Params();
        }
        return null;
    }

    //Rule #8
    //param-list -> param param-list-prime
    //param-list-prime -> , param param-list-prime | empty
    public ParamList paramList() {
        Param param = param();
        if (param != null) {
            ParamListPrime prime = paramListPrime();
            return new ParamList(param, prime);
        }
        return null;
    }

    private ParamListPrime paramListPrime() {
        int save = cursor;
        if (match(",")) {
            Param param = param();
            if (param != null) {
                ParamListPrime prime = paramListPrime();
                return new ParamListPrime(param, prime);
            }
        }
        backtrack(save);
        return null;
    }

    //param -> type-specifier ID | type-specifier ID [ ]
    public Param param() {
        int save = cursor;
        TypeSpecifier type = typeSpecifier();
        if (type != null && check(TokenType.ID)) {
            Token id = nextToken();
            if (match("[") && match("]")) {
                return new Param(type, id.getValue(), Param.Type.ARRAY);
            }
            backtrack(save);
            return new Param(type, id.getValue(), Param.Type.SINGLE);
        }
        return null;
    }

    //compound-stmt -> { local-declarations statement-list }
    public CompoundStmt compoundStmt() {
        if (match("{")) {
            LocalDeclarations localDeclarations = localDeclarations();
            StatementList statementList = statementList();
            if (match("}")) {
                return new CompoundStmt(localDeclarations, statementList);
            }
        }
        return null;
    }

    //local-declarations -> var-declaration local-declarations | empty
    public LocalDeclarations localDeclarations() {
        int save = cursor;
        VarDeclaration varDeclaration = varDeclaration();
        if (varDeclaration != null) {
            return new LocalDeclarations(varDeclaration, localDeclarations());
        }
        backtrack(save);
        return null;
    }

    //statement-list -> statement statement-list | empty
    public StatementList statementList() {
        int save = cursor;
        Statement statement = statement();
        if (statement != null) {
            return new StatementList(statement, statementList());
        }
        backtrack(save);
        return null;
    }

    //statement -> expression-stmt | compound-stmt | selection-stmt | iteration-stmt | return-stmt
    public Node statement() {
        int save = cursor;
        Node statement = expressionStmt();
        if (statement != null) {
            return statement;
        } else if (backtrack(save) && (statement = compoundStmt()) != null) {
            return statement;
        } else if (backtrack(save) && (statement = selectionStmt()) != null) {
            return statement;
        } else if (backtrack(save) && (statement = iterationStmt()) != null) {
            return statement;
        } else if (backtrack(save) && (statement = returnStmt()) != null) {
            return statement;
        }
        return null;
    }

    //expression-stmt -> expression ; | ;
    public ExpressionStmt expressionStmt() {
        int save = cursor;
        if (match(";")) {
            return new ExpressionStmt();
        } else if (backtrack(save)) {
            Expression expression = expression();
            if (expression != null && match(";")) {
                return new ExpressionStmt(expression);
            }
        }
        return null;
    }

    //selection-stmt -> if ( expression ) statement | if ( expression ) statement else statement
    public SelectionStmt selectionStmt() {
        if (match("if") && match("(")) {
            Expression expression = expression();
            if (expression != null && match(")")) {
                Statement statement = statement();
                if (statement != null && check("else")) {
                    nextToken();
                    Statement elseStatement = statement();
                    if (elseStatement != null) {
                        return new SelectionStmt(statement, elseStatement);
                    }
                } else {
                    return new SelectionStmt(statement);
                }
            }
        }
        return null;
    }

    //iteration-stmt -> while ( expression ) statement
    public IterationStmt iterationStmt() {
        if (match("while") && match("(")) {
            Expression expression = expression();
            if (expression != null && match(")")) {
                Statement statement = statement();
                if (statement != null) {
                    return new IterationStmt(statement);
                }
            }
        }
        return null;
    }

    //return-stmt -> return ; | return expression ;
    public ReturnStmt returnStmt() {
        if (match("return")) {
            if (match(";")) {
                return new ReturnStmt();
            }
            Expression expression = expression();
            if (expression != null && match(";")) {
                return new ReturnStmt(expression);
            }
        }
        return null;
    }

    //expression -> var = expression | simple-expression
    public Expression expression() {
        int save = cursor;
        Var var = var();
        if (var != null && match("=")) {
            Expression expression = expression();
            if (expression != null) {
                return new Expression(var, expression);
            }
        } else if (backtrack(save)) {
            SimpleExpression expression = simpleExpression();
            if (expression != null) {
                return new Expression(expression);
            }
        }
        return null;
    }

    //var -> ID | ID [ expression ]
    public Var var() {
        if (check(TokenType.ID)) {
            Token id = nextToken();
            if (match("[")) {
                Expression expression = expression();
                if (expression != null && match("]")) {
                    return new Var(id.getValue(), expression);
                }
            }
            return new Var(id.getValue());
        }
        return null;
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
                || (backtrack(save) && call())
                || (backtrack(save) && var())
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

    private boolean print(String state) {
        System.out.println(state + ", at " + cursor);
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

    private boolean check(String expected) {
        return check(0, expected);
    }

    private boolean check(int offset, String expected) {
        return cursor + offset < tokens.length
                && expected.equals(tokens[cursor + offset].getValue());
    }

    private boolean check(TokenType expected) {
        return check(0, expected);
    }

    private boolean check(int offset, TokenType expected) {
        return cursor + offset < tokens.length
                && expected == tokens[cursor + offset].getType();
    }
}
