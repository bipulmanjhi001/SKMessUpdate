package skmess.com.main.ui.complain;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
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
import skmess.com.model.VolleySingleton;
import static android.content.Context.MODE_PRIVATE;

public class ComplainFragment extends Fragment {

    Button Submit_Complain_form;
    Dialog myDialog;
    String phone_pay ="com.phonepe.app",google="com.google.android.apps.nbu.paisa.user",bhim="in.org.npci.upiapp",paytm="net.one97.paytm";
    TextView phone,Google,Paytm,Bhim;
    private static final String SHARED_PREF_NAME = "Messpref";
    private String token,usernames;
    private String d,l;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_share, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        usernames = prefs.getString("username", null);
        token = prefs.getString("token", null);

        Submit_Complain_form=(Button)root.findViewById(R.id.Submit_Complain_form);
        Submit_Complain_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup(view);
            }
        });

        phone=(TextView)root.findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp(getActivity(), phone_pay);
            }
        });

        Google=(TextView)root.findViewById(R.id.Google);
        Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp(getActivity(), google);
            }
        });

        Paytm=(TextView)root.findViewById(R.id.Paytm);
        Paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp(getActivity(), paytm);
            }
        });

        Bhim=(TextView)root.findViewById(R.id.Bhim);
        Bhim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp(getActivity(), bhim);
            }
        });

        myDialog=new Dialog(getActivity());

        return root;
    }

    public void ShowPopup(View v) {
        TextView txtclose;
        final EditText add_ttile,editText_newprom_description;
        Button btnFollow;
        myDialog.setContentView(R.layout.complain_meal);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        add_ttile =(EditText) myDialog.findViewById(R.id.add_ttile);
        editText_newprom_description =(EditText) myDialog.findViewById(R.id.editText_newprom_description);

        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d=add_ttile.getText().toString();
                l=editText_newprom_description.getText().toString();
                Complain();
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

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
                //throw new ActivityNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public void Complain() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_complain,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("title", d);
                params.put("description", l);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}