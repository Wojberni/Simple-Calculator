package com.example.simplecalculator;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mariuszgromada.math.mxparser.Expression;

public class AdvancedActivity extends AppCompatActivity {

    private EditText display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.advanced_calc);

        display = findViewById(R.id.display);
        display.setShowSoftInputOnFocus(false);

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getString(R.string.displayMsg).equals(display.getText().toString()) ||
                        getString(R.string.invalidOp).equals(display.getText().toString())){
                    display.setText("");
                }
            }
        });
    }


    private boolean validationDisplay(String string){
        if("".equals(string))
            return true;
        int cursorPosition = display.getSelectionStart();
        if(cursorPosition == 0)
            cursorPosition += 1;
        if(Character.isDigit(string.charAt(0)))
            return true;
        else if(string.charAt(0) == '(' || string.charAt(0) == ')')
            return true;
        else if(!Character.isDigit(string.charAt(0)) &&
                !Character.isAlphabetic(string.charAt(0))){
            if (Character.isDigit(display.getText().toString().charAt(cursorPosition-1))
                    || display.getText().toString().charAt(cursorPosition-1) == '('
                    || display.getText().toString().charAt(cursorPosition-1) == ')')
                return true;
            if(cursorPosition < display.getText().toString().length()){
                if(Character.isAlphabetic(display.getText().toString().charAt(cursorPosition)))
                    return true;
            }
        }
        else if(Character.isAlphabetic(string.charAt(0))){
            if(display.getText().toString().charAt(cursorPosition-1) == '.')
                return false;
            else if(!Character.isDigit(display.getText().toString().charAt(cursorPosition-1)))
                return true;
            else
                return false;
        }
        return false;
    }

    private void updateText(String string){
        String oldString = display.getText().toString();
        int cursorPosition = display.getSelectionStart();
        String leftString = oldString.substring(0, cursorPosition);
        String rightString = oldString.substring(cursorPosition);
        if(getString(R.string.displayMsg).equals(display.getText().toString()) ||
            "".equals(display.getText().toString()) ||
                getString(R.string.invalidOp).equals(display.getText().toString())){
            display.setText(string);
            display.setSelection(string.length());
        }
        else{
            if(validationDisplay(string)){
                display.setText(String.format("%s%s%s", leftString, string, rightString));
                display.setSelection(cursorPosition + string.length());
            }
        }
    }

    public void zero(View view){
        updateText("0");
    }

    public void one(View view){
        updateText("1");
    }

    public void two(View view){
        updateText("2");
    }

    public void three(View view){
        updateText("3");
    }

    public void four(View view){
        updateText("4");
    }

    public void five(View view){
        updateText("5");
    }

    public void six(View view){
        updateText("6");
    }

    public void seven(View view){
        updateText("7");
    }

    public void eight(View view){
        updateText("8");
    }

    public void nine(View view){
        updateText("9");
    }

    public void plus(View view){
        updateText("+");
    }

    public void minus(View view){
        updateText("-");
    }

    public void multiply(View view){
        updateText("*");
    }

    public void divide(View view){
        updateText("/");
    }

    public void percent(View view){
        updateText("%");
    }

    public void sinus(View view) { updateText("sin("); }

    public void cosinus(View view) { updateText("cos("); }

    public void tangens(View view) { updateText("tg("); }

    public void power(View view) { updateText("^"); }

    public void natural_log(View view) { updateText("ln("); }

    public void common_log(View view){ updateText("log10("); }

    public void root(View view) { updateText("sqrt("); }

    public void backspace(View view){
        int cursorPosition = display.getSelectionStart();
        int textLength = display.getText().length();

        if(cursorPosition != 0 && textLength != 0){
            SpannableStringBuilder text = (SpannableStringBuilder) display.getText();
            text.replace(cursorPosition - 1, cursorPosition, "");
            display.setText(text);
            display.setSelection(cursorPosition - 1);
        }
    }

    public void clear(View view){
        display.setText("");
    }

    public void equals(View view){
        String text = display.getText().toString();

        Expression expression = new Expression(text);
        if(!expression.checkSyntax()){
            Toast.makeText(
                    AdvancedActivity.this, "Syntax Error!", Toast.LENGTH_LONG).show();
        }
        else{
            String result = String.valueOf(expression.calculate());

            if("NaN".equals(result)){
                result = getString(R.string.invalidOp);
            }

            display.setText(result);
            display.setSelection(result.length());
        }

    }

    public void comma(View view){
        int i = display.getSelectionStart()-1;
        if(i < 0)
            i = 0;

        for(; i > 0; i--){
            if(display.getText().toString().charAt(i) == '.')
                return;
            if(!Character.isDigit(display.getText().toString().charAt(i))){
                break;
            }
        }

        updateText(".");
    }

    public void parenthesis(View view){
        int cursorPosition = display.getSelectionStart();
        int openParent = 0;
        int closedParent = 0;
        int textLen = display.getText().length();

        for(int i = 0; i < cursorPosition; i++){
            if(display.getText().toString().charAt(i) == '('){
                openParent += 1;
            }
            if(display.getText().toString().charAt(i) == ')'){
                closedParent += 1;
            }

        }

        if(openParent == closedParent || display.getText().toString().charAt(textLen - 1) == '('){
            updateText("(");
        }
        else if(closedParent < openParent && display.getText().toString().charAt(textLen - 1) != '('){
            updateText(")");
        }
        display.setSelection(cursorPosition + 1);

    }

    public enum dataType {NUMBER, EXPRESSION;}

    public void sign(View view){
        if("".equals(display.getText().toString()) ||
                getString(R.string.displayMsg).equals(display.getText().toString()) ||
                getString(R.string.invalidOp).equals(display.getText().toString()))
            return;

        int i = display.getSelectionStart()-1;
        if(i < 0)
            i = 0;

        dataType dataChange;
        if(Character.isAlphabetic(display.getText().toString().charAt(i)))
            dataChange = dataType.EXPRESSION;
        else
            dataChange = dataType.NUMBER;

        if(dataChange == dataType.NUMBER){
            for(; i > 0; i--){
                if(!Character.isDigit(display.getText().toString().charAt(i)) &&
                        display.getText().toString().charAt(i) != '.'){
                    break;
                }
            }
        }
        else{
            for(; i > 0; i--){
                if(!Character.isAlphabetic(display.getText().toString().charAt(i)) &&
                        display.getText().toString().charAt(i) != '.'){
                    break;
                }
            }
        }

        display.setSelection(i);
        if(i == 0){
            if(display.getText().toString().charAt(i) == '-'){
                String displayString = display.getText().toString();
                String rightString = displayString.substring(i+1);
                display.setText(String.format("%s",rightString));
                i--;
            }
            else{
                updateText("-");
            }
        }
        else{
            if(display.getText().toString().charAt(i) == '-'){
                String displayString = display.getText().toString();
                String leftString = displayString.substring(0, i);
                String rightString = displayString.substring(i+1);
                display.setText(String.format("%s%s%s", leftString, "+", rightString));
            }
            else if(display.getText().toString().charAt(i) == '+'){
                String displayString = display.getText().toString();
                String leftString = displayString.substring(0, i);
                String rightString = displayString.substring(i+1);
                display.setText(String.format("%s%s%s", leftString, "-", rightString));
            }
            else if(display.getText().toString().charAt(i) == '('){
                display.setSelection(++i);
                updateText("-");
            }
        }
        i++;

        if(dataChange == dataType.NUMBER){
            for(; i < display.getText().toString().length(); i++){
                if(!Character.isDigit(display.getText().toString().charAt(i)) &&
                        display.getText().toString().charAt(i) != '.')
                    break;
            }
        }
        else{
            for(; i < display.getText().toString().length(); i++){
                if(!Character.isAlphabetic(display.getText().toString().charAt(i)) &&
                        display.getText().toString().charAt(i) != '.')
                    break;
            }
        }
        display.setSelection(i);
    }


}