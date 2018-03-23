package com.example.daniel.calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class basicCalc extends AppCompatActivity {

    @BindViews({R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9})
    List<Button> numbers;
    @BindViews({R.id.btnMnoz, R.id.btnDziel, R.id.btnPlus, R.id.btnMinus})
    List<Button> operators;
    @BindView(R.id.field)
    TextView field;
    @BindView(R.id.c)
    Button clear;
    @BindView(R.id.btnRowna)
    Button btnRowna;
    @BindView(R.id.btnZnak)
    Button btnZnak;
    @BindView(R.id.btnKropka)
    Button btnKropka;
    @BindView(R.id.btnBksp)
    Button btnBksp;

    String fieldContent = "";
    boolean negative = false;
    List<String> opers = new ArrayList<>();
    List<Double> numbersToCalc = new ArrayList<>();

    Pattern p = Pattern.compile("[^0-9 ]", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_calc);
        ButterKnife.bind(this);
        init();
    }

    void refreshFieldContent()
    {
        fieldContent = field.getText().toString();
    }

    void init() {
        for (final Button b : numbers) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    field.setText(field.getText().toString() + b.getText().toString());
                }
            });
        }
        for (final Button b : operators) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (field.getText().length() >= 15)
                    {
                        toastNotification("Za malo miejsca");
                        return;
                    }

                    if(field.getText().toString().isEmpty())
                    {
                        toastNotification("Nie mozna wstawic takiego operatora jako pierwszy znak");
                        return;
                    }
                    else if(!field.getText().toString().isEmpty())
                    {
                        if(p.matcher(getLastCharacter()).find())
                        {
                            toastNotification("Dwa operatory obok siebie");
                            return;
                        }
                        field.setText(field.getText().toString() + b.getText().toString());
                        System.out.println("ostatni znak "+getLastCharacter());
                        if(opers.isEmpty())
                        {
                            opers.add(b.getText().toString()); //operatora do listy operatorow
                        }
                        System.out.println("Operator " + opers.get(0));
                        System.out.println("Ostatni operator w liscie " + opers.get(opers.size() - 1) + " wielkosc listy operatorow " + opers.size());
                    }


                }
            });
        }
        for (final Button b : numbers) {

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (field.getText().length() >= 15)
                    {
                        toastNotification("Za malo miejsca");
                        return;
                    }
                    if(opers.size()>2)
                    {
                        toastNotification("Oblicz poprzednie dzialanie");
                    }
                    else
                    {
                        field.setText(field.getText().toString() + b.getText().toString());
                    }

                }
            });
        }
        btnRowna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if (p.matcher(getLastCharacter()).find() || field.getText().length()<1) //jesli ostatni znak jest operatorem
                    {
                        toastNotification("Bledne dzialanie");
                    }
                    else
                    {
                        calculate();
                    }
                }
                catch(StringIndexOutOfBoundsException e)
                {
                    System.out.println("Blad przy =");
                }

            }

        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                field.setText("");
                opers.clear();
                numbersToCalc.clear();
            }
        });

        btnZnak.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {

            if(field.getText().length()<1)
            {
                field.setText("-"+field.getText());
            }
            else if(field.getText().subSequence(0,1).equals("-"))
               {
                   field.setText(field.getText().toString().replace("-",""));
               }
               else
               {
                   field.setText("-"+field.getText());
               }

           }
       });

        btnBksp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (field.getText().length() < 1) return;
                field.setText(field.getText().toString().substring(0, field.getText().length() - 1)); //usun ostatni znak
            }
        });

        btnKropka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(field.getText().length()<1)
                {
                    field.append("0.");
                }
                else if(!p.matcher(getLastCharacter()).find())
                {
                    field.append(".");
                }

            }
        });

    }

    public void toastNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void deleteLastCharacter()
    {
        btnBksp.callOnClick();
    }
    public String getLastCharacter()
    {
        return field.getText().toString().substring(field.getText().length()-1);
    }
    public void calculate()
    {
        if(opers.isEmpty())
        {
            toastNotification("Brak operatora");
            return;
        }
        fieldContent = field.getText().toString();
       String[] numbs;
       if(field.getText().toString().startsWith("-")) {
           numbs = field.getText().toString().substring(1).split("[^0-9.]");
           if (numbs.length != 2) {
               toastNotification("Zla ilosc parametrow");
               return;
           }
           System.out.println("Liczby to: " + Arrays.toString(numbs));
           String operator = opers.get(0);
           Double wynik = new Double(0);
           switch (operator) {
               case "+":
                   wynik = Double.valueOf("-"+numbs[0]) + Double.valueOf(numbs[1]);
                   field.setText(wynik.toString());
                   break;
               case "-":
                   wynik = Double.valueOf("-"+numbs[0]) - Double.valueOf(numbs[1]);
                   field.setText(wynik.toString());
                   break;
               case "*":
                   wynik = Double.valueOf("-"+numbs[0]) * Double.valueOf(numbs[1]);
                   field.setText(wynik.toString());
                   break;
               case "/":
                   wynik = Double.valueOf("-"+numbs[0]) / Double.valueOf(numbs[1]);
                   field.setText(wynik.toString());
                   break;
               default:
                   toastNotification("Blad");
           }
       }else
       {
           numbs = field.getText().toString().split("[^0-9.]");
           if(numbs.length!=2)
           {
               toastNotification("Zla ilosc parametrow");
               return;
           }
           System.out.println("Liczby to: "+ Arrays.toString(numbs));
           String operator = opers.get(0);
           Double wynik = new Double(0);
           switch(operator)
           {
               case "+":
                   wynik = Double.valueOf(numbs[0])+Double.valueOf(numbs[1]);
                   field.setText(wynik.toString());
                   break;
               case "-":
                   wynik = Double.valueOf(numbs[0])-Double.valueOf(numbs[1]);
                   field.setText(wynik.toString());
                   break;
               case "*":
                   wynik = Double.valueOf(numbs[0])*Double.valueOf(numbs[1]);
                   field.setText(wynik.toString());
                   break;
               case "/":
                   wynik = Double.valueOf(numbs[0])/Double.valueOf(numbs[1]);
                   field.setText(wynik.toString());
                   break;
               default:
                   toastNotification("Blad");
           }
       }


        opers.clear();
    }


}
