package com.example.andrey.workpls1;

import android.icu.math.BigDecimal;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;



public class CalculatorActivity extends AppCompatActivity {


    TextView result;
    HorizontalScrollView scroll;

    int[] numBtnIds = {R.id.d0, R.id.d1, R.id.d2, R.id.d3, R.id.d4, R.id.d5, R.id.d6, R.id.d7,
            R.id.d8, R.id.d9};

    int[] binOperIds = {R.id.add, R.id.sub, R.id.mul, R.id.div};


    boolean dotted = false;
    boolean ready = false;
    boolean operation = false;
    char operator = ' ';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        result = (TextView) findViewById(R.id.result);
        scroll = (HorizontalScrollView)findViewById(R.id.scroll);
//       / scroll.set
        if (savedInstanceState != null) {
            dotted = savedInstanceState.getBoolean("dotted");
            ready = savedInstanceState.getBoolean("ready");
            operation = savedInstanceState.getBoolean("operation");
            operator = savedInstanceState.getChar("operator");
            result.setText(savedInstanceState.getString("result"));
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("dotted", dotted);
        outState.putBoolean("ready", ready);
        outState.putBoolean("operation", operation);
        outState.putChar("operator", operator);
        outState.putString("result", result.getText().toString());

    }


    public void onClickListener(View v) {

        int id = v.getId();
        Button x = (Button) findViewById(id);

        for (int i = 0; i < numBtnIds.length; i++) {
            if (id == numBtnIds[i]) {
                result.append(x.getText());
                scrollRight();
                operation = false;
                ready = true;
                return;
            }
        }

        for (int i = 0; i < binOperIds.length; i++) {
            if (id == binOperIds[i]) {
                if (ready) {
                    if (!operation) {
                        operation = true;
                        result.setText(compute());
                    } else {
                        result.setText(result.getText().subSequence(0, result.length() - 1));
                    }
                    ready = false;
                    dotted = false;
                    result.append(x.getText());
                    scrollRight();
                    operator = x.getText().charAt(0);
                }
                return;

            }
        }

        switch (id) {
            case R.id.dot: {
                if (!dotted && ready) {
                    result.append(".");
                    scrollRight();
                    dotted = true;
                    ready = false;
                }
            }
            break;
            case R.id.eqv: {
                if (ready) {
                    result.setText(compute());
                    scrollRight();
                }
            }
            break;
            case R.id.clear: {
                result.setText("");
                dotted = false;
                ready = false;
                operation = false;
                operator = ' ';
            }
            break;
        }

    }

    private void scrollRight(){
        scroll.post(new Runnable() {
            public void run() {
                scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
    }

    private String compute() {
        String input = result.getText().toString();
        BigDecimal ans = new BigDecimal("0");
        int index = input.lastIndexOf(operator);

        if (index == -1) {
            ans = new BigDecimal(input);
        } else {
            switch (operator) {
                case '+':
                    ans = new BigDecimal(input.substring(0, index)).add(
                            new BigDecimal(input.substring(index + 1))
                    );
                    break;
                case '-':
                    ans = new BigDecimal(input.substring(0, index)).subtract(
                            new BigDecimal(input.substring(index + 1))
                    );
                    break;
                case '*':
                    ans = new BigDecimal(input.substring(0, index)).multiply(
                            new BigDecimal(input.substring(index + 1))
                    );
                    break;
                case '/':
                    try {
                        ans = new BigDecimal(input.substring(0, index)).divide(
                                new BigDecimal(input.substring(index + 1))
                        );
                    } catch (Exception e) {
                        Toast.makeText(this, "Division by zero", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
            operator = ' ';
        }

        return ans.toString();
    }

}
