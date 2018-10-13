package comq.example.raymond.plasucafeteria;

import android.app.ProgressDialog;
import android.content.Intent;
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

import comq.example.raymond.plasucafeteria.Common.Common;
import comq.example.raymond.plasucafeteria.model.User;
import info.hoang8f.widget.FButton;

public class SignIn extends AppCompatActivity {
    private MaterialEditText editTextPhone, editTextPassword;
    private Button buttonSigIn;
//    private FButton fButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextPhone = findViewById(R.id.edtPhone);
        editTextPassword = findViewById(R.id.edtPassword);
        buttonSigIn = findViewById(R.id.btnSignIn);
//        fButton = findViewById(R.id.btnSignIn);

        //initialize firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");


        buttonSigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone = editTextPhone.getText().toString().trim();
                String userPassword = editTextPassword.getText().toString().trim();

                //make sure user doesn't submit an empty field
                if (!TextUtils.isEmpty(userPassword) && !TextUtils.isEmpty(userPhone)){


                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Logging In...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //check if user exist
                            if (dataSnapshot.child(editTextPhone.getText().toString()).exists()){



                                mDialog.dismiss();
                                //get User information
                                User user = dataSnapshot.child(editTextPhone.getText().toString()).getValue(User.class);
                                user.setPhone(editTextPhone.getText().toString());//set phone number
                                if (user.getPassword().equals(editTextPassword.getText().toString())){
                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                }else{
                                    Toast.makeText(SignIn.this, "Sign In Failed; wrong phone number or password ", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else {
                    Toast.makeText(SignIn.this, "Sorry you cannot login with an empty field(s)", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }


}
