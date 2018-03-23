package com.example.daniel.calc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main extends AppCompatActivity {
    Button aboutBtn;
    Button exitBtn;
    @BindView(R.id.advCalcBtn) Button advCalc;
    @BindView(R.id.basicCalcBtn) Button basCalc;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

    }
    public void init()
    {
        aboutBtn = findViewById(R.id.aboutMeBtn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main.this,aboutMe.class));
            }
        });
        exitBtn = findViewById(R.id.exitBtn);
         exitBtn.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View view) {
                 finish();
             }
         });
         basCalc.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(Main.this,basicCalc.class));
             }
         });

         advCalc.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(Main.this,advancedCalc.class));
             }
         });




    }


}
