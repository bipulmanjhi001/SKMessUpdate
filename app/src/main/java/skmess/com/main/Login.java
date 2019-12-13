package skmess.com.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import skmess.com.R;
import skmess.com.api.URLs;
import skmess.com.model.ConnectivityReceiver;
import skmess.com.model.VolleySingleton;
import skmess.com.pref.SharedPrefManager;
import skmess.com.pref.User;

public class Login extends AppCompatActivity {

    TextView sign_in_Register;
    String username, password;
    String userName, passwordsss;
    private EditText UserView;
    ProgressBar login_progress;
    private EditText mPasswordView;
    CheckBox remember;
    private static final String PREFS_NAME = "preferenceName";
    private static final String SHARED_PREF_NAME = "Messpref";
    String tokens,usernames,d,otp,pass;
    TextView forgot_password;
    Dialog myDialog,myDialog2,myDialog3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        usernames = prefs.getString("username", null);
        tokens = prefs.getString("token", null);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            try {
                finish();
                startActivity(new Intent(this, Dashboard.class));
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        UserView = findViewById(R.id.user_id);
        mPasswordView = findViewById(R.id.password);

        SharedPreferences prefs3 = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userName = prefs3.getString("username","");
        passwordsss = prefs3.getString("password","");

        forgot_password=(TextView)findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });

        sign_in_Register = (TextView) findViewById(R.id.sign_in_Register);
        sign_in_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        myDialog=new Dialog(Login.this);
        myDialog2=new Dialog(Login.this);
        myDialog3=new Dialog(Login.this);

        mPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT);
                }else {
                    mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });

        login_progress = (ProgressBar) findViewById(R.id.login_progress);
        Button SignInButton = findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });

        remember = (CheckBox) findViewById(R.id.remember);
        if (!TextUtils.isEmpty(userName)) {
            remember.setChecked(true);
            UserView.setText(userName);
            mPasswordView.setText(passwordsss);
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (TextUtils.isEmpty(userName)) {
                        username = UserView.getText().toString();
                        password = mPasswordView.getText().toString();
                        save(remember.isChecked(), username, password);

                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        remember.setChecked(true);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            attemptLogin();
        } else {
            message = "connect your internet.";
            color = Color.RED;
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }

    private void attemptLogin() {
        UserView.setError(null);
        mPasswordView.setError(null);
        username = UserView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            UserView.setError(getString(R.string.error_field_required));
            focusView = UserView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();

        } else {
            Authenticate();
        }
    }

    public void Authenticate() {
        login_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONObject userJson = obj.getJSONObject("user");
                                String token = userJson.getString("token");
                                String username = userJson.getString("mobile");
                                User user = new User(userJson.getString("token"), userJson.getString("mobile"));

                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                finish();

                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                intent.putExtra("name", username);
                                intent.putExtra("token", token);
                                startActivity(intent);
                                finish();

                            } else if (!obj.getBoolean("status")) {

                                String error = obj.getString("error");
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Connection error..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", username);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void save(final boolean isChecked, String key, String key2) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", key);
        editor.putString("password", key2);
        editor.putBoolean(key, isChecked);
        editor.putBoolean(key2, isChecked);
        editor.apply();
    }

    public void ShowPopup(View v) {
        TextView txtclose;
        final EditText add_d;
        Button btnFollow;
        myDialog.setContentView(R.layout.forgot_password);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        add_d =(EditText) myDialog.findViewById(R.id.add_user);
        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        myDialog.setCancelable(false);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d=add_d.getText().toString();
                ForgotPassword();
                myDialog.dismiss();
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void ForgotPassword(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_forgot,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d("res",response);
                            if(obj.getBoolean("status")) {
                                ShowPopup2();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", d);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void ShowPopup2() {
        TextView txtclose;
        EditText etDigit1,etDigit2,etDigit3,etDigit4,etDigit5,etDigit6;
        AppCompatButton btnContinue;
        myDialog2.setContentView(R.layout.forgot_password2);
        myDialog2.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.setCancelable(false);
        txtclose =(TextView) myDialog2.findViewById(R.id.txtclose);

        etDigit1=(EditText)myDialog2.findViewById(R.id.etDigit1);
        etDigit2=(EditText)myDialog2.findViewById(R.id.etDigit2);
        etDigit3=(EditText)myDialog2.findViewById(R.id.etDigit3);
        etDigit4=(EditText)myDialog2.findViewById(R.id.etDigit4);
        etDigit5=(EditText)myDialog2.findViewById(R.id.etDigit5);
        etDigit6=(EditText)myDialog2.findViewById(R.id.etDigit6);

        btnContinue=(AppCompatButton)myDialog2.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp=etDigit1.getText().toString()+etDigit2.getText().toString()+etDigit3.getText().toString()+etDigit4.getText().toString()+etDigit5.getText().toString()+etDigit6.getText().toString();
                if(otp.length() == 6) {
                    OTP();
                }
                myDialog2.dismiss();
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.dismiss();
            }
        });
        myDialog2.show();
    }

    public void OTP(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                ShowPopup3();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", d);
                params.put("otp", otp);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void ShowPopup3() {
        TextView txtclose;
        final EditText add_d,add_password;
        Button btnFollow;
        myDialog3.setContentView(R.layout.forgot_password3);
        myDialog3.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txtclose =(TextView) myDialog3.findViewById(R.id.txtclose);
        add_d =(EditText) myDialog3.findViewById(R.id.New_Password);
        myDialog3.setCancelable(false);
        add_password=(EditText) myDialog3.findViewById(R.id.Confirm_Password);
        btnFollow = (Button) myDialog3.findViewById(R.id.btnfollow);
        myDialog3.setCancelable(false);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = add_password.getText().toString();
                if(add_d.getText().toString().equals(pass)) {
                    SetPassword();
                }else {
                    Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
                }
                myDialog3.dismiss();
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog3.dismiss();
            }
        });
        myDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog3.show();
    }

    public void SetPassword(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_setpassword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", d);
                params.put("password", pass);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
