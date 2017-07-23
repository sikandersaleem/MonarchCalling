package com.example.notificationtesting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String uemail, uid, deviceid;
    String token;
    TelephonyManager mngr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mProgress =new ProgressDialog(this);
        mProgress.setMessage("Signing in...");
        mProgress.setCancelable(false);
        mngr  =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        mAuth = FirebaseAuth.getInstance();

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                mProgress.show();
                signIn(email,password);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myintent= new Intent(LoginActivity.this, MainActivity.class);
                    //   myintent.putExtra("msg",carnum);
                    startActivity(myintent);

                } else {

                }
                // ...
            }
        };


    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void dataupload(){
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        //deviceid = mngr.getDeviceId().toString();
        token = FirebaseInstanceId.getInstance().getToken();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref_data = database.getReference("users");
        //deviceregistrationid d_r_id=new deviceregistrationid(token);
        //userdevices u_devices=new userdevices(deviceid, d_r_id);

        //userid u_id=new userid(uemail,u_devices);


        //List<String> deviceid=new ArrayList<String>();
        //deviceid.add(token);
        //HashMap<Objects> devices=new HashMap();


        //List<String> devices1= new ArrayList<String>();
        //HashMap<String, HashMap<String, Objects>> userr=new HashMap();
        //userr.put(uemail,devices);
        // devices1.add(token);
        //HashMap<String,Object> uid= new HashMap<String, Object>();

        //user user1=new user(uid,u_id);
        //ref_data.child("user").setValue(uid);
        ref_data.child(uid).child("email").setValue(uemail);
        ref_data.child(uid).child("Devices").child(deviceid).setValue(token);

        Toast.makeText(getApplicationContext()," Users data Updated on firebase",Toast.LENGTH_SHORT).show();
    }
    private void signIn(final String email, String password) {
        //Log.d(TAG, "signIn:" + email);
      /*  if (!validateForm()) {
            return;
        }*/

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {
                      //  Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                         //   Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Failed...",
                                    Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                            //mEmailView.setText("");
                            mPasswordView.setText("");
                        }
                        else
                        {
                            dataupload();
                            finish();
                            mProgress.dismiss();
                            Intent myintent= new Intent(LoginActivity.this, MainActivity.class);
                         //   myintent.putExtra("msg",carnum);
                            Toast.makeText(getApplicationContext(),"Loging in...",Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                            startActivity(myintent);
                        }
                    }
                });
    }
}

