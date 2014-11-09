Scenario: Test parsing lambda expression with no parameters

Given a Statement
When is the String "() -> println(this)" is parsed by the JavaParser using parseStatement
Then the Statement is parsed correctly