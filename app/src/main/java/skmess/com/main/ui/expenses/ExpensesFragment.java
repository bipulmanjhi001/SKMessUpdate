package skmess.com.main.ui.expenses;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import skmess.com.api.URLs;
import skmess.com.model.VolleySingleton;
import skmess.com.rangedatepicker.CalendarPickerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import skmess.com.R;
import skmess.com.rangedatepicker.SubTitle;
import static android.content.Context.MODE_PRIVATE;

public class ExpensesFragment extends Fragment {

    CalendarPickerView calendar;
    Button button;
    String remove = "00:00:00 GMT+05:30 ";
    ArrayList<String> arr =new ArrayList<String>();
    private static final String SHARED_PREF_NAME = "Messpref";
    private String token,usernames,data="";
    Dialog myDialog;
    String msg;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        usernames = prefs.getString("username", null);
        token = prefs.getString("token", null);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 3);
        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.DATE, 0);
        calendar =root.findViewById(R.id.calendar_view);
        button = root.findViewById(R.id.get_selected_dates);
        myDialog=new Dialog(getActivity());
        calendar.init(lastYear.getTime(), nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(new Date());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data=removeWords(calendar.getSelectedDates().toString(),remove);
                CancelMeal();
            }
        });

        return root;
    }

    private ArrayList<SubTitle> getSubTitles() {
        final ArrayList<SubTitle> subTitles = new ArrayList<SubTitle>();
        final Calendar tmrw = Calendar.getInstance();
        tmrw.add(Calendar.DAY_OF_MONTH, 1);
        return subTitles;
    }

    public static String removeWords(String word ,String remove) {
        return word.replace(remove,"");
    }

    public void CancelMeal(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_cancelorder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                msg= obj.getString("message");
                                msg=data+"\n\n"+msg;
                                ShowPopup();
                            }
                            else {
                                msg= obj.getString("message");
                                ShowPopup();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Check again...",Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",token);
                params.put("duration",data);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void ShowPopup() {
        TextView txtclose;
        final TextView add_d;
        Button btnFollow;
        myDialog.setContentView(R.layout.show_msg);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        add_d =(TextView) myDialog.findViewById(R.id.add_user);
        add_d.setText(msg);
        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}