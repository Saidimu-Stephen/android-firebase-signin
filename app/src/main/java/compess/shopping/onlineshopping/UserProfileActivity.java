package compess.shopping.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {
   private TextView textViewWelcome, textViewFullName,textViewEmail, textViewDob, textViewGender, textViewMobile;
   private ProgressBar progressBar;
    private String fullName, email, dob, gender, mobile;
   // private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        textViewWelcome=findViewById(R.id.tvShowWelcome);
        textViewFullName=findViewById(R.id.tv_show_full_name);
        textViewDob=findViewById(R.id.tv_show_dob);
        textViewGender=findViewById(R.id.tv_show_gender);
        textViewMobile=findViewById(R.id.tv_show_phone);
        textViewEmail=findViewById(R.id.tv_show_email);
        progressBar=findViewById(R.id.progress_bar);

        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= authProfile.getCurrentUser();
        if(firebaseUser == null){
            Toast.makeText(UserProfileActivity.this, "Something went wrong wrong! User details" +
                    " are nota available at the moment", Toast.LENGTH_LONG ).show();

        }
        else{
          progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID= firebaseUser.getUid();
        //Extracting User Reference from the databse from the Registered Users
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Register User");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails!=null){
                    fullName=firebaseUser.getDisplayName();
                    email=firebaseUser.getEmail();
                    dob=readUserDetails.dateOfBirth;
                    gender=readUserDetails.gender;
                    mobile=readUserDetails.phoneNumber;

                    textViewWelcome.setText("Welcome" + fullName+"!");
                    textViewFullName.setText(fullName);
                    textViewDob.setText(dob);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);
                    textViewEmail.setText(email);

                  progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong wrong", Toast.LENGTH_LONG ).show();
              progressBar.setVisibility(View.GONE);
            }
        });

    }
}