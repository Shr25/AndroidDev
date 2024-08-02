package com.example.calc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private TextView history;
    private Button buttonBackspace;
    private StringBuilder currentInput = new StringBuilder();
    private double lastResult = Double.NaN;
    private char currentOperation = ' ';
    private boolean operationInProgress = false;
    private List<String> pastCalculations = new ArrayList<>();
    private boolean hasDecimalPoint = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        history = findViewById(R.id.history);
        pastCalculations = new ArrayList<>();




        Button.OnClickListener listener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String buttonText = button.getText().toString();

                switch (buttonText) {
                    case "C":
                        clearAll();
                        break;
                    case "=":
                        evaluateExpression();
                        break;
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                        handleOperation(buttonText.charAt(0));
                        break;
                    case "%":
                        handlePercentage();
                        break;
                    case "CH":
                        clearHistory();
                        break;
                    case ".":
                        handleDecimalPoint();
                        break;
                    default:
                        handleNumberInput(buttonText);
                        break;
                }
            }
        };

        attachListeners(listener);
    }

    private void clearAll() {
        currentInput.setLength(0);
        lastResult = Double.NaN;
        currentOperation = ' ';
        display.setText("0");
        operationInProgress = false;
        hasDecimalPoint = false;
    }

    private void evaluateExpression() {
        if (currentInput.length() == 0 || operationInProgress) {
            return;
        }

        if (!Double.isNaN(lastResult)) {
            String input = currentInput.toString();
            double currentNumber = parseNumber(input);
            double result = calculate(lastResult, currentNumber, currentOperation);
            String expression = formatExpression(lastResult, currentNumber, currentOperation, result);
            addCalculationToHistory(expression);
            display.setText(formatResult(result));
            currentInput.setLength(0);
            currentInput.append(result);
            lastResult = result;
            operationInProgress = true;
            hasDecimalPoint = false;
        }
    }

    private void handleOperation(char operation) {
        if (currentInput.length() > 0) {
            if (!Double.isNaN(lastResult)) {
                evaluateExpression();
            } else {
                lastResult = parseNumber(currentInput.toString());
            }
            currentOperation = operation;
            currentInput.append(operation);
            display.setText(currentInput.toString());
            operationInProgress = true;
            hasDecimalPoint = false;
        }
    }
    private void handlePercentage() {
        if (currentInput.length() > 0) {
            double value = parseNumber(currentInput.toString());
            value /= 100;
            currentInput.setLength(0);
            currentInput.append(value);
            display.setText(currentInput.toString());
        }
    }
    private void handleDecimalPoint() {
        // If an operation is in progress, it means a new number is being started
        if (operationInProgress) {
            // Clear the current input to start fresh for the new number
            currentInput.setLength(0);
            operationInProgress = false;
        }

        // If the current input does not already contain a decimal point
        if (!hasDecimalPoint) {
            // If the current input is empty, append "0" before the decimal point
            if (currentInput.length() == 0) {
                currentInput.append("0");
            }
            // Append the decimal point to the current input
            currentInput.append(".");
            // Update the display with the new input
            display.setText(currentInput.toString());
            // Set the flag to indicate that a decimal point is now present
            hasDecimalPoint = true;
        }
    }

    private void handleNumberInput(String input) {
        if (operationInProgress) {
            currentInput.setLength(0);
            operationInProgress = false;
            hasDecimalPoint = false;
        }
        currentInput.append(input);
        display.setText(currentInput.toString());
    }

    private double calculate(double operand1, double operand2, char operation) {
        switch (operation) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 != 0) return operand1 / operand2;
                else return Double.NaN; // handle division by zero
            case '%':
                   return (operand1 / operand2)*100;
            default:
                return Double.NaN;
        }
    }

    private double parseNumber(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.format("%s", result);
        }
    }
    private void addCalculationToHistory(String calculation) {
        pastCalculations.add(calculation);
        StringBuilder historyText = new StringBuilder();
        for (String calc : pastCalculations) {
            historyText.append(calc).append("\n");
        }
        history.setText(historyText.toString());
    }
    private void updateHistoryDisplay() {
        StringBuilder historyText = new StringBuilder();
        for (String calc : pastCalculations) {
            historyText.append(calc).append("\n");
        }
        history.setText(historyText.toString());
    }

    private String formatExpression(double operand1, double operand2, char operation, double result) {
        return operand1 + " " + operation + " " + operand2 + " = " + result;
    }


    private void attachListeners(Button.OnClickListener listener) {
        findViewById(R.id.button9).setOnClickListener(listener);
        findViewById(R.id.button8).setOnClickListener(listener);
        findViewById(R.id.button7).setOnClickListener(listener);
        findViewById(R.id.button6).setOnClickListener(listener);
        findViewById(R.id.button5).setOnClickListener(listener);
        findViewById(R.id.button4).setOnClickListener(listener);
        findViewById(R.id.button3).setOnClickListener(listener);
        findViewById(R.id.button2).setOnClickListener(listener);
        findViewById(R.id.button1).setOnClickListener(listener);
        findViewById(R.id.button0).setOnClickListener(listener);
        findViewById(R.id.buttonDiv).setOnClickListener(listener);
        findViewById(R.id.buttonMult).setOnClickListener(listener);
        findViewById(R.id.buttonSub).setOnClickListener(listener);
        findViewById(R.id.buttonAdd).setOnClickListener(listener);
        findViewById(R.id.buttonPercent).setOnClickListener(listener);
        findViewById(R.id.buttonEqual).setOnClickListener(listener);
        findViewById(R.id.buttonClear).setOnClickListener(listener);
        findViewById(R.id.buttonClearHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistory();
            }
        });
        findViewById(R.id.buttonDecimal).setOnClickListener(listener);
    }
    private void clearHistory() {
        pastCalculations.clear();
        history.setText("");
    }

}
