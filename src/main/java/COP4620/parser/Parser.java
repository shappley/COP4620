package COP4620.parser;

import COP4620.lexer.Token;
import COP4620.lexer.TokenType;
import COP4620.parser.semantics.Node;
import COP4620.parser.semantics.TerminalNode;
import COP4620.parser.semantics.nodes.AdditiveExpression;
import COP4620.parser.semantics.nodes.AdditiveExpressionPrime;
import COP4620.parser.semantics.nodes.Addop;
import COP4620.parser.semantics.nodes.ArgList;
import COP4620.parser.semantics.nodes.Args;
import COP4620.parser.semantics.nodes.Call;
import COP4620.parser.semantics.nodes.CompoundStmt;
import COP4620.parser.semantics.nodes.Declaration;
import COP4620.parser.semantics.nodes.DeclarationList;
import COP4620.parser.semantics.nodes.Expression;
import COP4620.parser.semantics.nodes.ExpressionStmt;
import COP4620.parser.semantics.nodes.Factor;
import COP4620.parser.semantics.nodes.FunDeclaration;
import COP4620.parser.semantics.nodes.IterationStmt;
import COP4620.parser.semantics.nodes.LocalDeclarations;
import COP4620.parser.semantics.nodes.Mulop;
import COP4620.parser.semantics.nodes.Param;
import COP4620.parser.semantics.nodes.ParamList;
import COP4620.parser.semantics.nodes.ParamListPrime;
import COP4620.parser.semantics.nodes.Params;
import COP4620.parser.semantics.nodes.Program;
import COP4620.parser.semantics.nodes.Relop;
import COP4620.parser.semantics.nodes.ReturnStmt;
import COP4620.parser.semantics.nodes.SelectionStmt;
import COP4620.parser.semantics.nodes.SimpleExpression;
import COP4620.parser.semantics.nodes.Statement;
import COP4620.parser.semantics.nodes.StatementList;
import COP4620.parser.semantics.nodes.Term;
import COP4620.parser.semantics.nodes.TermPrime;
import COP4620.parser.semantics.nodes.TypeSpecifier;
import COP4620.parser.semantics.nodes.Var;
import COP4620.parser.semantics.nodes.VarDeclaration;

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
        Declaration declaration = declaration();
        if (declaration != null) {
            return new DeclarationList(declaration, declarationList());
        }
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
            return new ParamList(param, paramListPrime());
        }
        return null;
    }

    private ParamListPrime paramListPrime() {
        int save = cursor;
        if (match(",")) {
            Param param = param();
            if (param != null) {
                return new ParamListPrime(param, paramListPrime());
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
            save = cursor;
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
        Node statement = statement();
        if (statement != null) {
            return new StatementList(statement, statementList());
        }
        backtrack(save);
        return null;
    }

    //statement -> expression-stmt | compound-stmt | selection-stmt | iteration-stmt | return-stmt
    public Statement statement() {
        int save = cursor;
        Statement statement = expressionStmt();
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
            if (check(";")) {
                nextToken();
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
        int save = cursor;
        if (check(TokenType.ID)) {
            Token id = nextToken();
            if (check("[")) {
                nextToken();
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
    public SimpleExpression simpleExpression() {
        AdditiveExpression left = additiveExpression();
        if (left != null) {
            Relop relop = relop();
            if (relop != null) {
                AdditiveExpression right = additiveExpression();
                if (right != null) {
                    return new SimpleExpression(left, relop, right);
                }
            }
            return new SimpleExpression(left);
        }
        return null;
    }

    //relop -> <= | < | > | >= | == | !=
    public Relop relop() {
        if (check("<=") || check("<") || check(">") || check(">=") || check("==") || check("!=")) {
            return new Relop(nextToken().getValue());
        }
        return null;
    }

    //additive-expression -> term additive-expression-prime
    //additive-expression-prime -> addop term additive-expression-prime | empty
    public AdditiveExpression additiveExpression() {
        Term term = term();
        if (term != null) {
            return new AdditiveExpression(term, additiveExpressionPrime());
        }
        return null;
    }

    private AdditiveExpressionPrime additiveExpressionPrime() {
        int save = cursor;
        Addop addop = addop();
        if (addop != null) {
            Term term = term();
            if (term != null) {
                return new AdditiveExpressionPrime(addop, term, additiveExpressionPrime());
            }
        }
        backtrack(save);
        return null;
    }

    //term -> factor term-prime
    //term-prime -> mulop factor term-prime | empty
    public Term term() {
        Factor factor = factor();
        if (factor != null) {
            return new Term(factor, termPrime());
        }
        return null;
    }

    private TermPrime termPrime() {
        int save = cursor;
        Mulop mulop = mulop();
        if (mulop != null) {
            Factor factor = factor();
            if (factor != null) {
                return new TermPrime(mulop, factor, termPrime());
            }
        }
        backtrack(save);
        return null;
    }

    //addop -> + | -
    public Addop addop() {
        if (check("+") || check("-")) {
            return new Addop(nextToken().getValue());
        }
        return null;
    }

    //mulop -> * | /
    public Mulop mulop() {
        if (check("*") || check("/")) {
            return new Mulop(nextToken().getValue());
        }
        return null;
    }

    //factor -> ( expression ) | var | call | NUM
    public Factor factor() {
        int save = cursor;
        Node node;
        if (match("(") && ((node = expression()) != null) && match(")")) {
            return new Factor(node);
        } else if (backtrack(save) && (node = call()) != null) {
            return new Factor(node);
        } else if (backtrack(save) && (node = var()) != null) {
            return new Factor(node);
        } else if (backtrack(save) && check(TokenType.NUM)) {
            return new Factor(new TerminalNode(nextToken().getValue()));
        }
        return null;
    }

    //call -> ID ( args )
    public Call call() {
        if (check(TokenType.ID)) {
            Token id = nextToken();
            if (match("(")) {
                Args args = args();
                if (match(")")) {
                    return new Call(id.getValue(), args);
                }
            }
        }
        return null;
    }

    //args -> arg-list | empty
    public Args args() {
        int save = cursor;
        ArgList args = argList();
        if (args != null) {
            return new Args(args);
        }
        backtrack(save);
        return null;
    }

    //arg-list -> expression arg-list-prime
    //arg-list-prime -> , expression arg-list-prime | empty
    public ArgList argList() {
        Expression expression = expression();
        if (expression != null) {
            return new ArgList(expression, argListPrime());
        }
        return null;
    }

    private ArgList argListPrime() {
        int save = cursor;
        Expression expression;
        if (match(",") && (expression = expression()) != null) {
            return new ArgList(expression, argListPrime());
        }
        backtrack(save);
        return null;
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
