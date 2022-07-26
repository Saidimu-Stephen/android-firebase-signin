package compess.shopping.onlineshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set title
        Objects.requireNonNull(getSupportActionBar()).setTitle("Compess Mall");

        //open login activity
        Button buttonLogin=findViewById(R.id.btn_Login);
        buttonLogin.setOnClickListener(view -> {
            Intent i=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        });


        // open register activity
        Button buttonRegister = findViewById(R.id.btn_Register);
        buttonRegister.setOnClickListener(view -> {
            Intent re=new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(re);
        });

    }
}