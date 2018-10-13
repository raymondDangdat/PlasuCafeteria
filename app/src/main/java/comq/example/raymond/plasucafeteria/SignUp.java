package comq.example.raymond.plasucafeteria;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import comq.example.raymond.plasucafeteria.model.User;

public class SignUp extends AppCompatActivity {
    private MaterialEditText name, phone, password, cPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize
        name = findViewById(R.id.edtName);
        phone = findViewById(R.id.edtPhone);
        password = findViewById(R.id.edtPassword);
        cPassword = findViewById(R.id.edtCPassword);
        btnSignUp = findViewById(R.id.btnSignUp);



        //initialize firebase
       final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");


        //setOnCLickLister
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Uphone = phone.getText().toString().trim();
                String Uname = name.getText().toString().trim();
                String Upassword = password.getText().toString().trim();
                String uCpassword = cPassword.getText().toString().trim();

                //check if the user did not submit an empty field
                if (!TextUtils.isEmpty(Uphone) && !TextUtils.isEmpty(Uname) && !TextUtils.isEmpty(Upassword)
                        && !TextUtils.isEmpty(uCpassword)){
                    if (Upassword.equals(uCpassword)){


                        final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                        mDialog.setMessage("Signing up...");
                        mDialog.show();

                        //add valueEventListener
                        table_user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //check if the phone number already exist
                                if (dataSnapshot.child(phone.getText().toString().trim()).exists()){
                                    mDialog.dismiss();
                                    Toast.makeText(SignUp.this, "Number already in use by another customer", Toast.LENGTH_SHORT).show();
                                }else{
                                    mDialog.dismiss();
                                    User user = new User(name.getText().toString(), password.getText().toString());
                                    table_user.child(phone.getText().toString().trim()).setValue(user);
                                    Toast.makeText(SignUp.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else {
                        //passwords mismatch
                        Toast.makeText(SignUp.this, "Password doesn't match comfirm password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //user submitted empty fields
                    Toast.makeText(SignUp.this, "All fields must be completed please", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
