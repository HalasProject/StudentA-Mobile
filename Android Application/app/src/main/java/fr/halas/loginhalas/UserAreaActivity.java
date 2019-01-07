package fr.halas.loginhalas;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.android.volley.VolleyLog.TAG;


public class UserAreaActivity extends BaseActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String EmailVx;
    TextView txtName;
    TextView txtEmail;
    TextView txtEmailV;
    Button emailvalide;
    Button btnAdding;
    Button btnView;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_area);

        Button btnAdding = findViewById(R.id.btnAdding);
        Button btnView = findViewById(R.id.btnView);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button emailvalide = findViewById(R.id.verify_email_button);

        txtName = findViewById(R.id.name);
        txtEmail = findViewById(R.id.email);
        txtEmailV = findViewById(R.id.EmailV);


        // Adding New Affichage
        btnAdding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAreaActivity.this, AddingActivity.class));
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });

        // View Affichage Liste
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAreaActivity.this, ViewActivity.class));
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_left);
            }
        });

        //Send email verification
        emailvalide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
            }
        });

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        if (user == null) {
            startActivity(new Intent(UserAreaActivity.this, LoginActivity.class));
        } else {

            String name = user.getDisplayName();
            //if (name.equals("A334290553654652A"))
            //{txtName.setText("ADMIN");}
            //else
           // {
                txtName.setText(name);
           // }

            txtEmail.setText(user.getEmail());
            if (user.isEmailVerified()) {
                EmailVx = "VERIFIER";
                emailvalide.setVisibility(View.INVISIBLE);
            } else {
                EmailVx = "NON - VERIFIER";
            }
            txtEmailV.setText(EmailVx);
        }

    }

    private void logoutUser() {
        mAuth.signOut();
        startActivity(new Intent(UserAreaActivity.this, LoginActivity.class));
        finish();
    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify_email_button).setEnabled(false);

        showProgressDialog();
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            hideProgressDialog();
                            Toast.makeText(UserAreaActivity.this,
                                    getString(R.string.email_sent)+ " " + user.getEmail(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            hideProgressDialog();
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(UserAreaActivity.this,
                                    R.string.email_sentFail,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}

