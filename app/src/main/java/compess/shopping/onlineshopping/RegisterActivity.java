package compess.shopping.onlineshopping;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
   private EditText editTextFirtsName, editTextSecondName, editTextEmail,editTextPhoneNumber, editTextDob, editTextPassword, editTextConfirmPassword;
    private ProgressBar progressBar;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonRegisteredSelectedGender;
    private static final String TAG="RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set title
        Objects.requireNonNull(getSupportActionBar()).setTitle("Registration");

        Toast.makeText(RegisterActivity.this, "You can now register in the app", Toast.LENGTH_LONG).show();
        editTextFirtsName=findViewById(R.id.firstName);
        editTextSecondName=findViewById(R.id.secondName);
        editTextDob=findViewById(R.id.dob);
        editTextEmail=findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        editTextConfirmPassword=findViewById(R.id.c_password);
        editTextPhoneNumber=findViewById(R.id.phone);
        progressBar=findViewById(R.id.progress_bar);

        //radio button for gender
        radioGroupGender=findViewById(R.id.rGregister_gender);
        radioGroupGender.clearCheck();

        //setting up date picker
        editTextDob.setOnClickListener(this::onClick);


        Button buttonRegistration=findViewById(R.id.btn_Registration);
        buttonRegistration.setOnClickListener(view -> {
            int selectedGenderId=radioGroupGender.getCheckedRadioButtonId();
            radioButtonRegisteredSelectedGender=findViewById(selectedGenderId);



            //obtain data that was enter by the user
            String txtFirtsName=editTextFirtsName.getText().toString().trim();
            String txtSecondName=editTextSecondName.getText().toString().trim();
            String txtEmail=editTextEmail.getText().toString().trim();
            String txtPhoneNo=editTextPhoneNumber.getText().toString().trim();
            String txtPassword=editTextPassword.getText().toString().trim();
            String txtConfirmPassword=editTextConfirmPassword.getText().toString().trim();
            String txtDOB=editTextDob.getText().toString().trim();
            String txtGender;

            // validate mobile number
            String mobileNumberRegex="[0][0-9]{9}";//the first digit should be 0, the second digit can be any number between 0 and 9
            Matcher mobileMatcher;
            Pattern mobilePattern=Pattern.compile(mobileNumberRegex);
            mobileMatcher=mobilePattern.matcher(txtPhoneNo);

            if(TextUtils.isEmpty(txtFirtsName))
            {
                Toast.makeText(RegisterActivity.this, "Please Enter first name", Toast.LENGTH_LONG).show();
                editTextFirtsName.setError("First NAme is required");
                editTextFirtsName.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(txtSecondName))
            {
                Toast.makeText(RegisterActivity.this, "Please Enter Second name", Toast.LENGTH_LONG).show();
                editTextSecondName.setError("Second Name is required");
                editTextSecondName.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(txtEmail))
            {
                Toast.makeText(RegisterActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                editTextEmail.setError("Email Address is required");
                editTextEmail.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches())
            {
                Toast.makeText(RegisterActivity.this, "Please EnterValid email ", Toast.LENGTH_LONG).show();
                editTextEmail.setError("Invalid email Address");
                editTextEmail.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(txtPhoneNo))
            {
                Toast.makeText(RegisterActivity.this, "Please Enter Phone Number ", Toast.LENGTH_LONG).show();
                editTextPhoneNumber.setError("Phone Number is required");
                editTextPhoneNumber.requestFocus();
                return;
            }
            if(txtPhoneNo.length()!=10)
            {
                Toast.makeText(RegisterActivity.this, "Please Re-enter mobile number", Toast.LENGTH_LONG).show();
                editTextPhoneNumber.setError("Mobile number should be equal to 10digits");
                editTextPhoneNumber.requestFocus();
                return;
            }
            if(!mobileMatcher.find())
            {

                Toast.makeText(RegisterActivity.this, "Please Re-enter mobile number", Toast.LENGTH_LONG).show();
                editTextPhoneNumber.setError("Mobile number is not valid");
                editTextPhoneNumber.requestFocus();
                return;
            }

            if(TextUtils.isEmpty(txtDOB))
            {
                Toast.makeText(RegisterActivity.this, "Please Enter Date of Birth", Toast.LENGTH_LONG).show();
                editTextDob.setError("Date of Birth is required");
                editTextDob.requestFocus();
                return;
            }
            if(radioGroupGender.getCheckedRadioButtonId()==-1)
            {
                Toast.makeText(RegisterActivity.this, "Please select Gender", Toast.LENGTH_LONG).show();
                radioGroupGender.requestFocus();
                return;
            }

            if(TextUtils.isEmpty(txtPassword))
            {
                Toast.makeText(RegisterActivity.this, "Please Enter password", Toast.LENGTH_LONG).show();
                editTextPassword.setError("Password is required");
                editTextPassword.requestFocus();
                return;
            }

            if(TextUtils.isEmpty(txtConfirmPassword))
            {
                Toast.makeText(RegisterActivity.this, "Please Confirm password ", Toast.LENGTH_LONG).show();
                editTextConfirmPassword.setError("Password confirmation is required");
                editTextConfirmPassword.requestFocus();
                return;
            }if(txtPassword.length() <6)
            {
                Toast.makeText(RegisterActivity.this, "Please length should be atlest 6 characters in legth", Toast.LENGTH_LONG).show();
                editTextPassword.setError("Password too weak");
                editTextPassword.requestFocus();
                return;
            }
            if(!txtPassword.equals(txtConfirmPassword))
            {
                Toast.makeText(RegisterActivity.this, "please enter same password ", Toast.LENGTH_LONG).show();
                editTextConfirmPassword.setError("Password confirmation is required");
                editTextConfirmPassword.requestFocus();

                //clearing the enter password
                editTextPassword.clearComposingText();
                editTextConfirmPassword.clearComposingText();


            }
            else {
                txtGender = radioButtonRegisteredSelectedGender.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                registerUser(txtFirtsName,txtSecondName,txtEmail,txtPhoneNo,txtDOB,txtGender,txtPassword);

            }



        });

    }

    private void registerUser(String txtFirtsName, String txtSecondName,
                              String txtEmail, String txtPhoneNo, String txtDOB, String txtGender, String txtPassword) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        // creates user profile
        auth.createUserWithEmailAndPassword(txtEmail,txtPassword).addOnCompleteListener(RegisterActivity.this, task -> {
            if(task.isSuccessful()){

                FirebaseUser firebaseuser=auth.getCurrentUser();

                String fullName=txtFirtsName+txtSecondName;

                //Update Display name of the user
                UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder().setDisplayName(fullName).build();
                assert firebaseuser != null;
                firebaseuser.updateProfile(profileChangeRequest);

                //Enter user data into the Real time database
                ReadWriteUserDetails writeUserDetails=new ReadWriteUserDetails(txtPhoneNo,txtDOB,txtGender);

                // Extracting user reference from database for "Registered users"
                DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Register Users");
                referenceProfile.child(firebaseuser.getUid()).setValue(writeUserDetails).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){

                    //send verification email
                    firebaseuser.sendEmailVerification();
                    Toast.makeText(RegisterActivity.this, "User  successfully registered. Please very your email address",
                            Toast.LENGTH_LONG).show();
                    //open user profile after successful registration
                    Intent intent=new Intent(RegisterActivity.this, UserProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                   // progressBar.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "User Registration failed. Please try again",
                                Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });

            }
            else{
                try {
                    throw Objects.requireNonNull(task.getException());
                }
                catch (FirebaseAuthWeakPasswordException e){
                    editTextPassword.setError("Your password is too weak!! Kindly use a combination of alphabet, numbers and charcters");
                    editTextPassword.requestFocus();
                }
                catch (FirebaseAuthInvalidCredentialsException e){
                    editTextEmail.setError("Your email is invalid or already in use. kindly renter");
                    editTextEmail.requestFocus();
                }
                catch(FirebaseAuthUserCollisionException e){
                    editTextEmail.setError("User is already registered. Use another email address.");
                    editTextEmail.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }
                catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);

                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void onClick(View view) {
        final Calendar calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int month = calender.get(Calendar.MONTH);
        int year = calender.get((Calendar.YEAR));


        //date picker dialog
        DatePickerDialog picker;
        picker = new DatePickerDialog(RegisterActivity.this,
                (datePicker, i, i1, i2) -> editTextDob.setText(i + "/" + (i1 + 1) + "/" + i2),
                year,
                month,
                day);
        picker.show();
    }
}