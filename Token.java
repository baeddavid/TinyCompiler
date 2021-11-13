public class Token {
    public String text;
    public Enum kind;

    public Token(String tokenText, Enum tokenKind) {
        text = tokenText;
        kind = tokenKind;
    }
}
