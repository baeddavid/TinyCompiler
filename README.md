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

## Instructions 

 If for some odd reason you want to try and use this compiler set up is easy.
 Clone the repo into your desired location. 
 Run `javac TinyCompiler.java` inside the repo directory. 
 Run `java TinyCompiler "path to your tiny file"`
 There should be a new file in the repo directory called `out.c` that contains the compiled code.


## Images
Input

[![input.png](https://i.postimg.cc/26kq4KNH/input.png)](https://postimg.cc/XZP7WxJF)

Output

[![output.png](https://i.postimg.cc/hGqQGGyR/output.png)](https://postimg.cc/yDv8Q7CL)
