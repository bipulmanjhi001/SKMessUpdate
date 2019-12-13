package skmess.com.main.ui.home;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import skmess.com.R;
import skmess.com.adapter.MealAdpater;
import skmess.com.adapter.MealModel;
import skmess.com.api.URLs;
import skmess.com.model.VolleySingleton;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private TextView months,Year,grand_total,grand_symbol;
    private ListView meal_daily_list;
    Button add_meal;
    String selectDate,cat_ids;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Dialog myDialog;
    private static final String SHARED_PREF_NAME = "Messpref";
    private String token,usernames;
    ProgressBar meal_progress;
    MealAdpater mealAdpater;
    String d="",bf="",l="";
    ArrayList<MealModel> mealModels;
    LinearLayout offers_layout;
    TextView offers;

    public static final String mypreference = "cat_shared";
    public static final String cat_id = "cat_id";
    SharedPreferences sharedpreferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        usernames = prefs.getString("username", null);
        token = prefs.getString("token", null);

        sharedpreferences = getActivity().getSharedPreferences(mypreference, MODE_PRIVATE);
        cat_ids = sharedpreferences.getString(cat_id, null);

        Year=(TextView)root.findViewById(R.id.Year);
        months=(TextView)root.findViewById(R.id.month);
        meal_daily_list=(ListView)root.findViewById(R.id.meal_daily_list);

        myDialog=new Dialog(getActivity());
        mealModels=new ArrayList<MealModel>();
        grand_symbol=(TextView)root.findViewById(R.id.grand_symbol);
        offers_layout=(LinearLayout)root.findViewById(R.id.offers_layout);
        offers=(TextView)root.findViewById(R.id.offers);

        meal_progress=(ProgressBar)root.findViewById(R.id.meal_progress);
        add_meal=(Button)root.findViewById(R.id.add_meal);
        add_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                selectDate=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                Toast.makeText(getActivity(),selectDate,Toast.LENGTH_SHORT).show();
                                ShowPopup(view);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        grand_total=(TextView)root.findViewById(R.id.grand_total);
        Attendancelist();

        return root;
    }

    public void ShowPopup(View v) {
        TextView txtclose,select_date;
        final EditText add_d,add_l,add_bf;
        Button btnFollow;
        myDialog.setContentView(R.layout.custom_add_meal);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        select_date =(TextView) myDialog.findViewById(R.id.select_date);
        select_date.setText(selectDate);
        add_d =(EditText) myDialog.findViewById(R.id.add_d);
        add_l =(EditText) myDialog.findViewById(R.id.add_l);
        add_bf =(EditText) myDialog.findViewById(R.id.add_bf);

        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 d=add_d.getText().toString();
                 l=add_l.getText().toString();
                 bf=add_bf.getText().toString();
                 AddExtra();
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

    public void Attendancelist() {
        meal_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_attendancelist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d("res",response);
                            if (obj.getBoolean("status")) {

                                JSONObject userJson = obj.getJSONObject("attendancelist");
                                String year = userJson.getString("year");
                                Year.setText(year);
                                String month = userJson.getString("month");
                                months.setText(month);

                                JSONArray meal= userJson.getJSONArray("meal");
                                for(int i=0; i<meal.length(); i++){
                                    JSONObject object=meal.getJSONObject(i);

                                    String date = object.getString("date");
                                    String breakfast = object.getString("breakfast");
                                    String lunch = object.getString("lunch");
                                    String dinner = object.getString("dinner");
                                    String price = object.getString("price");

                                    MealModel mealModel=new MealModel();
                                    mealModel.setDate(date);
                                    mealModel.setBreakfast(breakfast);
                                    mealModel.setLunch(lunch);
                                    mealModel.setDinner(dinner);
                                    mealModel.setPrice(price);
                                    mealModels.add(mealModel);
                                    try {
                                        Double checkvalue=Double.parseDouble(price);
                                        if(checkvalue < 200) {
                                            grand_total.setText(price);
                                            grand_total.setTextColor(Color.parseColor("#D80909"));
                                            grand_symbol.setTextColor(Color.parseColor("#D80909"));
                                        }else {
                                            grand_total.setText(price);
                                            grand_total.setTextColor(Color.parseColor("#ffffff"));
                                            grand_symbol.setTextColor(Color.parseColor("#ffffff"));
                                        }
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                }

                                meal_progress.setVisibility(View.GONE);
                                LayoutInflater myinflater = getLayoutInflater();
                                ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.meal_header, meal_daily_list, false);
                                meal_daily_list.addHeaderView(myHeader, null, false);
                                LayoutInflater myinflater2 = getLayoutInflater();
                                ViewGroup myHeader2 = (ViewGroup)myinflater2.inflate(R.layout.meal_footer, meal_daily_list, false);
                                meal_daily_list.addFooterView(myHeader2, null, false);
                                mealAdpater=new MealAdpater(mealModels,getActivity());
                                meal_daily_list.setAdapter(mealAdpater);
                                mealAdpater.notifyDataSetChanged();

                            } else if (!obj.getBoolean("status")) {

                                Toast.makeText(getActivity(), "Refresh again", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), "Connection error..", Toast.LENGTH_SHORT).show();
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
                params.put("category_id", cat_ids);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        Offers();
    }

    public void AddExtra() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_addextra,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }else {
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
                params.put("date",selectDate);
                params.put("l", l);
                params.put("br", bf);
                params.put("d",d);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void Offers(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_getoffers,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("message");

                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String offer_text = itemslist.getString("message");
                                    offers_layout.setVisibility(View.VISIBLE);
                                    offers.setText(offer_text);
                                    offers.setSelected(true);

                                }
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
                params.put("token", token);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}