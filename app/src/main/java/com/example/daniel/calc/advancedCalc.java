package com.example.daniel.calc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class advancedCalc extends AppCompatActivity {

    @BindViews({R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9})
    List<Button> numbers;
    @BindViews({R.id.btnMnoz, R.id.btnDziel, R.id.btnPlus, R.id.btnMinus,R.id.btnXpowY})
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

    @BindViews({R.id.btnSin,R.id.btnCos,R.id.btnTan,R.id.btnLn,R.id.btnLog,R.id.btnSqrt,R.id.btnXpow2})
    List<Button> oneFactorOperators;
    int kropkaCounter=0;
    List<String> opers = new ArrayList<>();
    Pattern p = Pattern.compile("[^0-9 ]", Pattern.CASE_INSENSITIVE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_calc);
        ButterKnife.bind(this);
        init();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        if(!opers.isEmpty()) savedInstanceState.putString("oper", opers.get(0));
        savedInstanceState.putString("fieldContent", field.getText().toString());
        savedInstanceState.putInt("kropkaCounter", kropkaCounter);




        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        String savedOper = savedInstanceState.getString("oper");
        if(opers!=null && savedOper!=null)
        {
            opers.add(savedOper); //operatora do listy operatorow
        }
        field.setText(savedInstanceState.getString("fieldContent"));
        kropkaCounter = savedInstanceState.getInt("kropkaCounter");
    }


    void init() {


        for (final Button b : operators) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (field.getText().length() >= 25) //jesli za duzo znakow to ine pozwol wiecej wpisac
                    {
                        toastNotification("Za malo miejsca");
                        return;
                    }
                    else if(field.getText().toString().isEmpty()) //jesli pusty
                    {
                        if(!b.getText().equals("-"))
                        {
                            toastNotification("Nie mozna wstawic takiego operatora jako pierwszy znak");
                            return;
                        }
                        else
                        {
                            field.append("-");
                        }

                    }
                    else //jesli nie jest pusto
                    {
                        if(p.matcher(getLastCharacter()).find()) //sprawdz czy ostatni wpisany znak nie jest operatorem, jesli tak to nie pozwol wstawic kolejnego operatora chyba ze minus to sprawdz inne warunki
                        {
                            toastNotification("Dwa sprzeczne operatory obok siebie");
                            return;
                        }




                        if(opers.isEmpty())
                        {
                            opers.add(b.getText().toString()); //operatora do listy operatorow
                            field.append(b.getText());
                        }
                        else
                        {
                            if(field.getText().toString().charAt(0)=='-')
                            {
                                if(field.getText().toString().substring(1).split("[^0-9.]").length==2)
                                {
                                    System.out.println(Arrays.toString(field.getText().toString().split("[^0-9.]")));
                                    toastNotification("Oblicz poprzednie dzialanie");
                                    return;
                                }
                                field.append(b.getText().toString());
                            }
                            else
                            {
                                if(field.getText().toString().split("[^0-9.]").length==2)
                                {
                                    System.out.println(Arrays.toString(field.getText().toString().split("[^0-9.]")));
                                    toastNotification("Oblicz poprzednie dzialanie");
                                    return;
                                }
                                field.append(b.getText().toString());
                            }

                            opers.set(0,b.getText().toString());
                        }

                        System.out.println("ostatni znak "+getLastCharacter());
                        System.out.println("Operator " + opers.get(0));
                        System.out.println("Ostatni operator w liscie " + opers.get(opers.size() - 1) + " wielkosc listy operatorow " + opers.size());
                        System.out.println("Operatory w liscie "+opers.toString());

                    }


                }
            });
        }
        for (final Button b : numbers) {

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (field.getText().length() >= 25)
                    {
                        toastNotification("Za malo miejsca");
                        return;
                    }
                    if(field.getText().toString().contains("NaN") || field.getText().toString().contains("Infinity"))
                    {
                        toastNotification("Niedozwolone dzialanie Infinity / Nan");
                        return;
                    }
                    if(opers.size()>2)
                    {
                        toastNotification("Oblicz poprzednie dzialanie");
                    }
                    else
                    {
                        field.append(b.getText());
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
                kropkaCounter=0;

            }
        });
        btnZnak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(field.getText().toString().equalsIgnoreCase(""))
                {
                    field.setText("-");
                }
                else if(field.getText().toString().substring(0,1).equalsIgnoreCase("-"))
                {
                    field.setText(field.getText().toString().replaceFirst("-",""));
                }
                else
                {
                    field.setText("-"+field.getText().toString());
                }


            }
        });
        final Pattern x = Pattern.compile("[^0-9.]", Pattern.CASE_INSENSITIVE);
        btnBksp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (field.getText().length() < 1) return;
                if(getLastCharacter().equalsIgnoreCase("."))
                {
                    kropkaCounter--;
                }

                if(x.matcher(getLastCharacter()).find())
                {
                    opers.clear();
                    System.out.println("wyczyszczono opers = "+opers.toString()+opers.isEmpty());
                }
                if(field.getText().toString().contains("NaN") || field.getText().toString().contains("Infinity"))
                {
                    clear.callOnClick();
                    return;
                }
                field.setText(field.getText().toString().substring(0, field.getText().length() - 1)); //usun ostatni znak
            }
        });

        btnKropka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                if(field.getText().toString().isEmpty())
                {
                    kropkaCounter++;
                    field.append("0.");
                    System.out.println("1");
                }
                else if(kropkaCounter==2 || getLastCharacter().equalsIgnoreCase(".") || (opers.isEmpty() && kropkaCounter==1)) //countOccurances(field.getText().toString(),".")==2
                {
                    toastNotification("Blad formatu liczby");
                    System.out.println("2");
                    return;
                }
                else if(x.matcher(getLastCharacter()).find())
                {
                    field.append("0.");
                    kropkaCounter++;
                    System.out.println("3");
                }
                else
                {
                    //Double.parseDouble(countOccurances(field.getText().toString().substring(field.getText().toString().lastIndexOf(opers.get(0))),"."))
                    if(!opers.isEmpty())
                    {
                        try
                        {
                            Double.parseDouble(field.getText().toString().substring(field.getText().toString().lastIndexOf(opers.get(0))+1)+".");
                            field.append(".");
                            kropkaCounter++;
                            return;
                        }
                        catch(NullPointerException e)
                        {
                            toastNotification("Brak drugiej liczby");
                            return;
                        }
                        catch(NumberFormatException e)
                        {
                            toastNotification("Bledny format liczby");
                            return;
                        }
                        catch(IndexOutOfBoundsException e)
                        {
                            toastNotification("IndexBoundException");
                            return;
                        }
                    }
                    field.append(".");
                    kropkaCounter++;
                    System.out.println("Kropek "+kropkaCounter+" operatory "+opers);
                    System.out.println("4");

                }


            }
        });

        for(final Button b : oneFactorOperators)
        {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(!opers.isEmpty() || field.getText().toString().matches("[^0-9\\.]")) return;
                    if(field.getText().toString().isEmpty())
                    {
                        toastNotification("Blad dzialania");
                        return;
                    }
                    opers.clear();
                    double wynik=0;
                    if(b.getText().equals("sin"))
                    {
                        wynik=Math.sin(Double.valueOf(field.getText().toString()));
                        if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                        {
                            toastNotification("Wynik NaN lub Infinite");
                            return;
                        }
                        field.setText(String.valueOf(wynik));
                    }
                    if(b.getText().equals("cos"))
                    {
                        wynik=Math.cos(Double.valueOf(field.getText().toString()));
                        if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                        {
                            toastNotification("Wynik NaN lub Infinite");
                            return;
                        }
                        field.setText(String.valueOf(wynik));
                    }
                    if(b.getText().equals("tan"))
                    {
                        wynik=Math.tan(Double.valueOf(field.getText().toString()));
                        if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                        {
                            toastNotification("Wynik NaN lub Infinite");
                            return;
                        }
                        field.setText(String.valueOf(wynik));
                    }
                    if(b.getText().equals("ln"))
                    {
                        wynik=Math.log1p(Double.valueOf(field.getText().toString()));
                        if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                        {
                            toastNotification("Wynik NaN lub Infinite");
                            return;
                        }
                        field.setText(String.valueOf(wynik));
                    }
                    if(b.getText().equals("log"))
                    {
                        wynik=Math.log10(Double.valueOf(field.getText().toString()));
                        if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                        {
                            toastNotification("Wynik NaN lub Infinite");
                            return;
                        }
                        field.setText(String.valueOf(wynik));
                    }
                    if(b.getText().equals("sqrt"))
                    {
                        wynik=Math.sqrt(Double.valueOf(field.getText().toString()));
                        if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                        {
                            toastNotification("Wynik NaN lub Infinite");
                            return;
                        }
                        field.setText(String.valueOf(wynik));
                    }
                    if(b.getText().equals("x^2"))
                    {
                        wynik=Math.pow(Double.valueOf(field.getText().toString()),2);
                        if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                        {
                            toastNotification("Wynik NaN lub Infinite");
                            return;
                        }
                        field.setText(String.valueOf(wynik));
                    }

                    opers.clear();
                    kropkaCounter=1;
                }
            });
        }

    }

    public void toastNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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
                    if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                    {
                        toastNotification("Wynik NaN lub Infinite");
                        return;
                    }
                    field.setText(wynik.toString());
                    break;
                case "-":
                    wynik = Double.valueOf("-"+numbs[0]) - Double.valueOf(numbs[1]);
                    if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                    {
                        toastNotification("Wynik NaN lub Infinite");
                        return;
                    }
                    field.setText(wynik.toString());
                    break;
                case "*":
                    wynik = Double.valueOf("-"+numbs[0]) * Double.valueOf(numbs[1]);
                    if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                    {
                        toastNotification("Wynik NaN lub Infinite");
                        return;
                    }
                    field.setText(wynik.toString());
                    break;
                case "/":
                    wynik = Double.valueOf("-"+numbs[0]) / Double.valueOf(numbs[1]);
                    if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                    {
                        toastNotification("Wynik NaN lub Infinite");
                        return;
                    }
                    field.setText(wynik.toString());
                    break;
                case "^":
                    numbs = field.getText().toString().split("[//^]");
                    wynik=Math.pow(Double.valueOf(numbs[0]),Double.valueOf(numbs[1]));
                    field.setText(String.valueOf(wynik));
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
                    if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                    {
                        toastNotification("Wynik NaN lub Infinite");
                        return;
                    }
                    field.setText(wynik.toString());
                    break;
                case "-":
                    wynik = Double.valueOf(numbs[0])-Double.valueOf(numbs[1]);
                    if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                    {
                        toastNotification("Wynik NaN lub Infinite");
                        return;
                    }
                    field.setText(wynik.toString());
                    break;
                case "*":
                    wynik = Double.valueOf(numbs[0])*Double.valueOf(numbs[1]);
                    if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                    {
                        toastNotification("Wynik NaN lub Infinite");
                        return;
                    }
                    field.setText(wynik.toString());
                    break;
                case "/":
                    wynik = Double.valueOf(numbs[0])/Double.valueOf(numbs[1]);
                    if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                    {
                        toastNotification("Wynik NaN lub Infinite");
                        return;
                    }
                    field.setText(wynik.toString());
                    break;
                case "^":
                    wynik=Math.pow(Double.valueOf(numbs[0]),Double.valueOf(numbs[1]));
                    if(Double.isInfinite(Double.valueOf(wynik)) || Double.isNaN(Double.valueOf(wynik)))
                    {
                        toastNotification("Wynik NaN lub Infinite");
                        return;
                    }
                    field.setText(String.valueOf(wynik));
                    break;
                default:
                    toastNotification("Blad");
            }
        }


        opers.clear();
        kropkaCounter=1; //bo zawsze wyswietla wynik typu Double z przecinkiem

    }
    @Deprecated
    public void appendIfPossible(String s)
    {
        if(s.length()+field.getText().length() > 26)
        {
            toastNotification("Limit znakow");
            return;
        }
        else if(s.equalsIgnoreCase("-"))
        {
            if(countOccurances(field.toString(),"-")==3)
            {
                toastNotification("Za duzo operatorow");
                return;
            }
            else if(countOccurances(field.toString(),"-")==2)
            {

            }
        }
    }

    public static int countOccurances(String s, String charToCount)
    {
        return s.length()-s.replace(charToCount,"").length();
    }



}
