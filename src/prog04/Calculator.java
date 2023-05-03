package prog04;

import java.util.Stack;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import prog02.UserInterface;
import prog02.GUI;
import prog02.ConsoleUI;

public class Calculator {
  static final String OPERATORS = "()+-*/u^";
  static final int[] PRECEDENCE = { -1, -1, 1, 1, 2, 2, 3, 4 };
  Stack<Character> operatorStack = new Stack<Character>();
  Stack<Double> numberStack = new Stack<Double>();
  boolean previousTokenWasNumberOrRightParenthesis=false;
  UserInterface ui = new GUI("Calculator");

  Calculator (UserInterface ui) { this.ui = ui; }

  int precedence(char op)
  {
    return PRECEDENCE[OPERATORS.indexOf(op)];
  }

  void emptyStacks ()
  {
    while (!numberStack.empty())
      numberStack.pop();
    while (!operatorStack.empty())
      operatorStack.pop();
  }

  String numberStackToString () {
    String s = "numberStack: ";
    Stack<Double> helperStack = new Stack<Double>();
    // EXERCISE
    // Put every element of numberStack into helperStack
    // You will need to use a loop.  What kind?
    // What condition? When can you stop moving elements out of numberStack?
    // What method do you use to take an element out of numberStack?
    // What method do you use to put that element into helperStack?

    while(!numberStack.empty()) {
      helperStack.push(numberStack.pop());
    }


    // Now put everything back, but also add each one to s:
    // s = s + " " + number;
    while(!helperStack.empty()) {
      s = s + " " +  helperStack.peek();
      numberStack.push(helperStack.pop());

    }


    return s;
  }

  String operatorStackToString () {
    String s = "operatorStack: ";

    // EXERCISE
    Stack<Character> helperStack = new Stack<Character>();
    // EXERCISE
    // Put every element of numberStack into helperStack
    // You will need to use a loop.  What kind?
    // What condition? When can you stop moving elements out of numberStack?
    // What method do you use to take an element out of numberStack?
    // What method do you use to put that element into helperStack?

    while(!operatorStack.empty()) {
      helperStack.push(operatorStack.pop());
    }


    // Now put everything back, but also add each one to s:
    // s = s + " " + number;
    while(!helperStack.empty()) {
      s = s + " " +  helperStack.peek();
      operatorStack.push(helperStack.pop());

    }

    return s;
  }

  void displayStacks () {
    ui.sendMessage(numberStackToString() + "\n" +
            operatorStackToString());
  }

  void doNumber (double x) {
    numberStack.push(x);
    previousTokenWasNumberOrRightParenthesis=true;
    displayStacks();
  }

  void doOperator (char op) {

    if(op ==')')
    {
      previousTokenWasNumberOrRightParenthesis=true;
    }
    if(op =='-' && numberStack.empty()){
      previousTokenWasNumberOrRightParenthesis=false;
    }
     if(op=='-'&& !previousTokenWasNumberOrRightParenthesis){
      processOperator('u');

    }
   /* else if(op=='-'){
      if(!previousTokenWasNumberOrRightParenthesis){
        processOperator('u');
      }
      else{
        processOperator('-');
      }
      previousTokenWasNumberOrRightParenthesis=false;
    }*/

    else
    {
      processOperator(op);
      previousTokenWasNumberOrRightParenthesis=false;
    }

    displayStacks();
  }

  double doEquals () {
    while (!operatorStack.empty())
      evaluateTopOperator();

    return numberStack.pop();
  }

  double evaluateOperator (double a, char op, double b) {
    switch (op) {
      case '+':
        return a + b;
      case '-':
        return b-a;
      case '*':
        return a*b;
      case '/':
        return b/a;
      case '^':
        return Math.pow(b,a);
      // EXERCISE
    }
    System.out.println("Unknown operator " + op);
    return 0;
  }

  void evaluateTopOperator () {
    char op = operatorStack.pop();

    if(op=='u')
    {
     // double temp = -numberStack.pop();
      numberStack.push(-numberStack.pop());
    }
    else
    {
      double num1 = numberStack.pop();
      double num2 = numberStack.pop();
      numberStack.push(evaluateOperator(num1, op, num2));
    }
    displayStacks();
  }

  void processOperator (char op)
  {
    if(op=='(')
    {
      operatorStack.push(op);
    }
    else if(op == ')')
    {
      while(operatorStack.peek()!='(')
      {
        evaluateTopOperator();
      }
      operatorStack.pop();
    }
   else if(op=='u')
   {
     operatorStack.push(op);
     //previousTokenWasNumberOrRightParenthesis=false;
   }
    else
    {
      while (!operatorStack.empty() && precedence(operatorStack.peek()) >= precedence(op))
      {
        evaluateTopOperator();
      }
      operatorStack.push(op);
    }
  }

  static boolean checkTokens (UserInterface ui, Object[] tokens) {
    for (Object token : tokens)
      if (token instanceof Character &&
              OPERATORS.indexOf((Character) token) == -1) {
        ui.sendMessage(token + " is not a valid operator.");
        return false;
      }
    return true;
  }

  static void processExpressions (UserInterface ui, Calculator calculator) {
    while (true) {
      String line = ui.getInfo("Enter arithmetic expression or cancel.");
      if (line == null)
        return;
      Object[] tokens = Tokenizer.tokenize(line);
      if (!checkTokens(ui, tokens))
        continue;
      try {
        for (Object token : tokens)
          if (token instanceof Double)
            calculator.doNumber((Double) token);
          else
            calculator.doOperator((Character) token);
        double result = calculator.doEquals();
        ui.sendMessage(line + " = " + result);
      } catch (Exception e) {
        ui.sendMessage("Bad expression.");
        calculator.emptyStacks();
      }
    }
  }

  public static void main (String[] args) {
    UserInterface ui = new ConsoleUI();
    Calculator calculator = new Calculator(ui);
    processExpressions(ui, calculator);
  }
}
