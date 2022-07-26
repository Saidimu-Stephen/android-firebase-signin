package compess.shopping.onlineshopping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText editTextEmail, editTextPassword;
   private ProgressBar progressBar;
   private FirebaseAuth authProfile;
   private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).setTitle("login");
        editTextEmail=findViewById(R.id.loginEmailAddress);
        editTextPassword=findViewById(R.id.lg_password);
        progressBar=findViewById(R.id.progress_bar);
        authProfile=FirebaseAuth.getInstance();
        imageView=findViewById(R.id.show_hidePassword);
        imageView.setImageResource(R.drawable.hide_password);
        imageView.setOnClickListener(view -> {
            if (editTextPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                //if password is visible then hide it
                editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageView.setImageResource(R.drawable.hide_password);
            }
            else{
                editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageView.setImageResource(R.drawable.show_password);
            }
        });

        //defining ad declaring login button
        Button loginButton;
        loginButton=findViewById(R.id.btn_Login);
        loginButton.setOnClickListener(view -> {
            String txtEmail=editTextEmail.getText().toString().trim();
            String txtPassword=editTextPassword.getText().toString().trim();


            if(TextUtils.isEmpty(txtEmail)){
                Toast.makeText(LoginActivity.this, "Please enter email address", Toast.LENGTH_LONG).show();
                editTextEmail.setError("Enter email address");
                editTextEmail.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(txtPassword)){
                Toast.makeText(LoginActivity.this, "Password is required", Toast.LENGTH_LONG).show();
                editTextPassword.setError("Enter password");
                editTextPassword.requestFocus();
            }
            else{
                progressBar.setVisibility(View.VISIBLE);
                loginUser(txtEmail,txtPassword);
            }
        });
    }

    private void loginUser(String Email, String Password) {
        authProfile.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                //Get the instance of the current user who is trying to login
                FirebaseUser firebaseUser=authProfile.getCurrentUser();

                //check weather the user is verified before user can access their profile
                assert firebaseUser != null;
                if(firebaseUser.isEmailVerified()){
                    Toast.makeText(LoginActivity.this, "You are now logged in", Toast.LENGTH_LONG).show();
                    //open user profile

                    startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
                    finish(); //close login activity
                }
                else{
                   // firebaseUser.sendEmailVerification();
                    authProfile.signOut(); // signout user
                    showAlertDialog();
                }
            }
            else{
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                try {
                    throw Objects.requireNonNull(task.getException());
                }
                catch (FirebaseAuthInvalidCredentialsException e){
                    editTextEmail.setError("Invalid login credentials. Kindly check and try again");
                    editTextEmail.requestFocus();
                }
                catch(FirebaseAuthInvalidUserException e){
                    editTextEmail.setError("User does not exits or no longer exist. Please register again!!");
                    editTextEmail.requestFocus();
                }
                catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void showAlertDialog() {
        //setup alert filter
        AlertDialog.Builder builder=new AlertDialog.Builder( LoginActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your now. You cannot loggin without email verification.");
        //Open Email Appp if user click enter/continue
        builder.setPositiveButton("Continue", (dialogInterface, i) -> {
            Intent intent=new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            // to open email app in anew window and not within the app
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        });
        // create the alertDialog
        AlertDialog alertDialog = builder.create();
        //show alertDialog
        alertDialog.show();
    }
    //Check if the user is already logged in. in this case straight take the user to the profile activity
    @Override
    public void onStart(){
        super.onStart(); if(authProfile.getCurrentUser()!= null){
            Toast.makeText(LoginActivity.this, "You are already logged in", Toast.LENGTH_SHORT).show();
            //start the user profile activity
            startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
            finish(); //close login activity

        }
        else{
            Toast.makeText(LoginActivity.this, "You can log in now", Toast.LENGTH_SHORT).show();
        }
    }
}