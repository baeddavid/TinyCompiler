public class Token {
    public String text;
    public TokenType kind;

    public Token(String tokenText, TokenType tokenKind) {
        text = tokenText;
        kind = tokenKind;
    }
}
