package com.arbind.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText fn, ln, phn, eml, pwd, cpwd;
    TextView warn;
    boolean  isEmailValid=false, isPwdvalid = false;
    public static final String jsonPreference = "JPref";
    public static final String loggedIn = "loggedIn";
    public static final String uName = "userName";
    public static final String uMobile = "userMobile";
    public static final String uemail = "userEmail";
    SharedPreferences jsp;
    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        jsp = this.getSharedPreferences(jsonPreference, Context.MODE_PRIVATE);
        isLoggedIn = jsp.getBoolean(loggedIn,false);
        if(isLoggedIn){
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            finish();
        }


    }
    public void sign_up(View v){
        fn=findViewById(R.id.editTextTextPersonName);
        phn=findViewById(R.id.editTextPhone);
        eml=findViewById(R.id.editTextTextEmailAddress);
        warn=findViewById(R.id.textView4);

        String phn_no = phn.getText().toString();

        String email= eml.getText().toString();
        int at_pos = email.indexOf("@");
        int dot_pos = email.lastIndexOf(".");

        if(at_pos>4&&dot_pos-at_pos>2&&email.length()-dot_pos>2){
            isEmailValid = true;
        }
        else{
            isEmailValid=false;
        }




        String first_name = fn.getText().toString();
        String full_name = first_name;

        if(first_name.length()>0&&phn_no.length()==10&&(isEmailValid||email.equals(""))){
            SharedPreferences.Editor editor = jsp.edit();
            editor.putBoolean(loggedIn, true);
            editor.putString(uName, full_name);
            editor.putString(uemail, email);
            editor.putString(uMobile, phn_no);
            editor.commit();

            Intent i = new Intent(Login.this, MainActivity.class);
            i.putExtra("my_key", full_name);
            startActivity(i);
            finish();
        }
        else{
            if(first_name.length()==0){
                fn.requestFocus();
                fn.setError(getString(R.string.provide_fname));
                //warn.setText(getString(R.string.provide_fname));
            }

            else if(phn_no.length()==0){
                phn.requestFocus();
                phn.setError(getString(R.string.provide_mobile_no));
                //warn.setText(getString(R.string.provide_mobile_no));
            }
            else if(phn_no.length()!=10){
                phn.requestFocus();
                phn.setError(getString(R.string.not_ten_digit));
                //warn.setText(getString(R.string.not_ten_digit));
            }
            else if(email!=""){
                eml.requestFocus();
                eml.setError(getString(R.string.invalid_email));
                //warn.setText(getString(R.string.invalid_email));
            }

        }
    }
}

