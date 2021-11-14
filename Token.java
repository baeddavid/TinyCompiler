public class Token {
    public String text;
    public TokenType kind;

    public Token(String tokenText, TokenType tokenKind) {
        text = tokenText;
        kind = tokenKind;
    }

    public static TokenType checkIfKeyword(String tokenText) {
        try {
            TokenType candidate = TokenType.valueOf(tokenText);
            if(candidate.getKey().equals(tokenText) && candidate.getValue() >= 100 && candidate.getValue() < 200) {
                return candidate;
            }
        }
        catch(IllegalArgumentException i) {
            return null;
        }
        return null;
    }
}
