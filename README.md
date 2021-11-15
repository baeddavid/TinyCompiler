# Tiny Compiler

This was my attempt at implementing a very basic compiler that takes Tiny BASIC as input and generates C code. 

The following grammar is the grammar that was implemented.

## Grammar

    program = { statement }
    statement = "PRINT" ( expression | string ) nl | 
    "IF" comparison "THEN" nl { statement } "ENDIF" nl |
    "WHILE" comparison "REPEAT" nl { statement } "ENDWHILE" |
    "LABEL" ident nl | "GOTO" ident nl | "LET" ident "=" expression nl |
    "INPUT" ident nl
    comparison = expression(( "==" | "!=" | ">" | ">=" | "<" | "<=" ) expression )+
    expression = term {( "-" | "+") term }
    term = unary {( "/" | "+" ) term }
    unary = [ "+" | "-" ] primary
    primary = number | ident
    nl = '\n'+
    
## Structure

Structure of the compiler is fairly straight forward.

The Lexer class is in charge of generating tokens from the input. 
Tokens are essentially key-value objects that store the text of the token and what kind of token it is.
The value of the Token is an enumeration key-value object that further stores details of the token.

After the input has gone through the lexer, we begin parsing the code in accordance to our above grammar. 
