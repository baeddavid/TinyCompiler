public class Parser {
    public Token curToken;
    public Token peekToken;
    public Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        curToken = null;
        peekToken = null;
        nextToken();
        nextToken();
    }

    public boolean checkToken(TokenType candidate) {
        String enumKey = candidate.getKey();
        return enumKey.equals(curToken.kind.getKey());
    }

    public boolean checkPeek(TokenType candidate) {
        String enumKey = candidate.getKey();
        return enumKey.equals(peekToken.kind.getKey());
    }

    public void match(TokenType candidate) {
        if(!checkToken(candidate)) {
            abort("Expected " + candidate.getKey() + ", got " + curToken.kind);
        }
        nextToken();
    }

    public void nextToken() {
        curToken = peekToken;
        peekToken = lexer.getToken();
    }

    public void abort(String message) {
        System.out.println("Error. " + message);
        System.exit(0);
    }

    public void program() {
        System.out.println("PROGRAM");

        while(!checkToken(TokenType.EOF)) {
            statement();
        }
    }

    public void statement() {
        if(checkToken(TokenType.PRINT)) {
            System.out.println("STATEMENT-PRINT");
            nextToken();
        }

        if(checkToken(TokenType.STRING)) {
            nextToken();
        } else {
            System.out.println("HMMMM");
        }
        nl();
    }

    public void nl() {
        System.out.println("NEWLINE");
        match(TokenType.NEWLINE);
        while(checkToken(TokenType.NEWLINE)) {
            nextToken();
        }
    }
}
