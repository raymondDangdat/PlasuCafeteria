package comq.example.raymond.plasucafeteria;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btnSignIn, btnSignUp;
    private TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtSlogan = findViewById(R.id.txtSlogan);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Lobster.otf");
        txtSlogan.setTypeface(typeface);




        //set onclickListeners
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));


           }
        });


     btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
               startActivity(new Intent(MainActivity.this, SignIn.class));

            }
      });

    }
}
