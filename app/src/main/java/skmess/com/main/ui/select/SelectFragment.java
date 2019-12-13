package skmess.com.main.ui.select;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.Map;
import skmess.com.R;
import skmess.com.api.URLs;
import skmess.com.model.VolleySingleton;
import static android.content.Context.MODE_PRIVATE;

public class SelectFragment extends Fragment {

    private static final String SHARED_PREF_NAME = "Messpref";
    private String token,usernames;
    private Spinner spinner1,spinner2,spinner3,spinner4,spinner5;

    private ArrayList<String> AreaNames=new ArrayList<String>();
    private ArrayList<String> AreaIds=new ArrayList<String>();

    private ArrayList<String> subareaNames=new ArrayList<String>();
    private ArrayList<String> subareaIds=new ArrayList<String>();

    private ArrayList<String> hosteNames=new ArrayList<String>();
    private ArrayList<String> hosteIds=new ArrayList<String>();

    private ArrayList<String> categoryNames=new ArrayList<String>();
    private ArrayList<String> categoryIds=new ArrayList<String>();

    private ArrayList<String> packageNames=new ArrayList<String>();
    private ArrayList<String> packageIds=new ArrayList<String>();

    private String area_ids,subarea_ids,category_ids,hostel_ids,package_id;
    Button Submit_added_form;
    LinearLayout linear,lineartype,lineartype2,select_type,packagelist;
    String valueList1="",valueList2="",valueList3="",data="",monthly_price,half="", valueList4="", valueList5="", valueList6="",valueList7="",valueList8="",valueList9="",checkmeal;
    private RadioGroup radioGroup2,chooseGroup2;
    private CheckBox CHICKEN,EGG,FISH,choose1,choose2;
    private CheckBox URAD,MASUR,ROTI,BREAKFAST,LUNCH,DINNER;
    TextView Monthly_Price;
    public static final String mypreference = "cat_shared";
    public static final String cat_id = "cat_id";
    SharedPreferences sharedpreferences;
    int valueList10=0,valueList20=0,valueList30=0,half10=0,full10=0, valueList40=0, valueList50=0, valueList60=0,valueList70=0,valueList80=0,valueList90=0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        usernames = prefs.getString("username", null);
        token = prefs.getString("token", null);

        spinner1=(Spinner)root.findViewById(R.id.spinner1);
        spinner2=(Spinner)root.findViewById(R.id.spinner2);
        spinner3=(Spinner)root.findViewById(R.id.spinner3);
        spinner4=(Spinner)root.findViewById(R.id.spinner4);
        spinner5=(Spinner)root.findViewById(R.id.spinner5);

        lineartype=(LinearLayout)root.findViewById(R.id.lineartype);
        lineartype2=(LinearLayout)root.findViewById(R.id.lineartype2);
        select_type=(LinearLayout)root.findViewById(R.id.select_type);
        packagelist=(LinearLayout)root.findViewById(R.id.packagelist);

        URAD = (CheckBox)root.findViewById(R.id.URAD);
        URAD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(valueList40 == 0){
                    valueList4="1";
                    ++valueList40;
                    URAD.setChecked(true);
                }
                else if(valueList40 == 1){
                    URAD.setChecked(false);
                    valueList4="0";
                   --valueList40;
            }
            }
        });

        MASUR = (CheckBox)root.findViewById(R.id.MASUR);
        MASUR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(valueList50 == 0){
                    valueList5="1";
                    ++valueList50;
                    MASUR.setChecked(true);
                }
                else if(valueList50 == 1){
                        MASUR.setChecked(false);
                        valueList5="0";
                    --valueList50;

                }
            }
        });

        ROTI = (CheckBox)root.findViewById(R.id.ROTI);
        ROTI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(valueList60 == 0){
                    valueList6="1";
                    ++valueList60;
                    ROTI.setChecked(true);
                }
                else if(valueList60 == 1){
                    ROTI.setChecked(false);
                    valueList6="0";
                    --valueList60;
                }
            }
        });

        BREAKFAST = (CheckBox)root.findViewById(R.id.BREAKFAST);
        BREAKFAST.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(valueList70 == 0){
                    valueList7="1";
                    ++valueList70;
                    BREAKFAST.setChecked(true);
                    Packprice_List();
                }
                else if(valueList70 == 1){
                    BREAKFAST.setChecked(false);
                    valueList7="0";
                    --valueList70;
                }
            }
        });

        LUNCH = (CheckBox)root.findViewById(R.id.LUNCH);
        LUNCH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(valueList80 == 0){
                    valueList8="1";
                    ++valueList80;
                    LUNCH.setChecked(true);
                    Packprice_List();
                }
                else if(valueList80 == 1){
                    LUNCH.setChecked(false);
                    valueList8="0";
                    --valueList80;
                }
            }
        });

        DINNER = (CheckBox)root.findViewById(R.id.DINNER);
        DINNER.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(valueList90 == 0){
                    valueList9="1";
                    ++valueList90;
                    DINNER.setChecked(true);
                    Packprice_List();
                }
                else if(valueList90 == 1){
                    DINNER.setChecked(false);
                    valueList9="0";
                    --valueList90;
                }
            }
        });

        Submit_added_form=(Button)root.findViewById(R.id.Submit_added_form);
        Submit_added_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                        AddMeal2();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });

        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        Monthly_Price=(TextView)root.findViewById(R.id.Monthly_Price);
        linear=(LinearLayout)root.findViewById(R.id.linear);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    area_ids = AreaIds.get(i).toString();
                    SubArea_List();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    subarea_ids=subareaIds.get(i).toString();
                    Hostel_List();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    hostel_ids = hosteIds.get(i).toString();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    category_ids = categoryIds.get(i).toString();
                        Package_List();
                        if(category_ids.equals("2")) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(cat_id, category_ids);
                            editor.apply();
                        }else {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(cat_id, category_ids);
                            editor.apply();
                        }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    package_id = packageIds.get(i).toString();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EGG = (CheckBox)root.findViewById(R.id.EGG);
        EGG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(valueList10 == 0){
                    valueList1="1";
                    ++valueList10;
                    EGG.setChecked(true);
                }
                else if(valueList10 == 1){
                    EGG.setChecked(false);
                    valueList1="0";
                    --valueList10;
                }
            }
        });

        FISH = (CheckBox)root.findViewById(R.id.FISH);
        FISH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(valueList20 == 0){
                    valueList2="1";
                    ++valueList20;
                    FISH.setChecked(true);
                }
                else if(valueList20 == 1){
                    FISH.setChecked(false);
                    valueList2="0";
                    --valueList20;
                }
            }
        });

        CHICKEN = (CheckBox)root.findViewById(R.id.CHICKEN);
        CHICKEN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(valueList30 == 0){
                    valueList3="1";
                    ++valueList30;
                    FISH.setChecked(true);
                }
                else if(valueList30 == 1){
                    FISH.setChecked(false);
                    valueList3="0";
                    --valueList30;
                }
            }
        });


        radioGroup2 = (RadioGroup)root.findViewById(R.id.radioGroup2);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    Toast.makeText(getActivity(), rb.getText(), Toast.LENGTH_SHORT).show();
                    if(rb.getText().equals("HALF")) {
                        half="Half";
                        Package_List();
                    }else {
                        half="Full";
                        Package_List();
                    }
                }
            }
        });
        packagelist.setVisibility(View.VISIBLE);
        select_type.setVisibility(View.VISIBLE);
        chooseGroup2 = (RadioGroup)root.findViewById(R.id.chooseGroup2);
        chooseGroup2.setVisibility(View.GONE);
        Area_List();

        return root;
    }

    public void Area_List() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_arealist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject c = new JSONObject(response);
                            if(c.getBoolean("status")){
                                JSONArray array = c.getJSONArray("arealist");
                                if (!AreaIds.isEmpty()) {
                                    AreaIds.clear();
                                    AreaNames.clear();
                                }
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    AreaNames.add(name);
                                    AreaIds.add(id);
                                }
                            }else {
                                Toast.makeText(getActivity(), c.getString("message"), Toast.LENGTH_SHORT).show();
                                AreaIds.clear();
                                AreaNames.clear();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        try {
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (getActivity(), android.R.layout.simple_spinner_item, AreaNames);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                    .simple_spinner_dropdown_item);
                            spinner1.setAdapter(spinnerArrayAdapter);
                            area_ids = AreaIds.get(0).toString();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Check connection again.", Toast.LENGTH_SHORT).show();
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

    public void  SubArea_List() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_subarealist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("subarealist");
                                if(!subareaNames.isEmpty()){
                                    subareaNames.clear();
                                    subareaIds.clear();
                                }
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    String landmark= itemslist.getString("landmark");
                                    subareaNames.add(name +" "+ landmark);
                                    subareaIds.add(id);
                                }
                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        try {
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subareaNames);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(spinnerArrayAdapter);
                            subarea_ids=subareaIds.get(0).toString();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("area_id",area_ids);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void Hostel_List(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_hostellist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("hostellist");
                                if(!hosteIds.isEmpty()){
                                    hosteIds.clear();
                                    hosteNames.clear();
                                }
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    String landmark = itemslist.getString("landmark");
                                    hosteNames.add(name +" "+ landmark);
                                    hosteIds.add(id);
                                }
                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        try {
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (getActivity(), android.R.layout.simple_spinner_item, hosteNames);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                    .simple_spinner_dropdown_item);
                            spinner3.setAdapter(spinnerArrayAdapter);
                            hostel_ids = hosteIds.get(0).toString();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("subarea_id",subarea_ids);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        Category_List();
    }

    public void Category_List() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_categorylist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject c = new JSONObject(response);
                            if(c.getBoolean("status")){
                                JSONArray array = c.getJSONArray("categorylist");
                                if(!categoryNames.isEmpty()){
                                    categoryNames.clear();
                                    categoryIds.clear();
                                }
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);
                                    String id = object.getString("id");
                                    String name = object.getString("category");
                                    categoryNames.add(name);
                                    categoryIds.add(id);
                                }
                            }else {
                                Toast.makeText(getActivity(), c.getString("message"), Toast.LENGTH_SHORT).show();
                                categoryIds.clear();
                                categoryNames.clear();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        try {
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (getActivity(), android.R.layout.simple_spinner_item, categoryNames);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                    .simple_spinner_dropdown_item);
                            spinner4.setAdapter(spinnerArrayAdapter);
                            category_ids = categoryIds.get(0).toString();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Check connection again.", Toast.LENGTH_SHORT).show();
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

    public void Packprice_List() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_packprice,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject c = new JSONObject(response);
                            if(c.getBoolean("status")) {
                                Monthly_Price.setText("Monthly Price " + c.getString("price"));
                                monthly_price=c.getString("price");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("quantity",half);
                params.put("breakfast",valueList7);
                params.put("lunch",valueList8);
                params.put("dinner",valueList9);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void Package_List() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_packagelist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject c = new JSONObject(response);
                            if(c.getBoolean("status")){
                                JSONArray array = c.getJSONArray("packagelist");
                                if(!packageIds.isEmpty()){
                                    packageIds.clear();
                                    packageNames.clear();
                                }
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String monthly_price= object.getString("monthly_price");
                                    String perday_price= object.getString("perday_price");
                                    packageNames.add(name +" Monthly "+ monthly_price +" Per day "+  perday_price);
                                    packageIds.add(id);
                                }
                            }else {
                                try {
                                    Toast.makeText(getActivity(), c.getString("message"), Toast.LENGTH_SHORT).show();
                                    packageIds.clear();
                                    packageNames.clear();
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        try {
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (getActivity(), android.R.layout.simple_spinner_item, packageNames);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                    .simple_spinner_dropdown_item);
                            spinner5.setAdapter(spinnerArrayAdapter);
                            try {
                                package_id = packageIds.get(0).toString();
                            }catch (IndexOutOfBoundsException e){
                                e.printStackTrace();
                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("quantity",half);
                params.put("category_id", category_ids);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void AddMeal2(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_selectpackage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_SHORT).show();
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
                params.put("token", token);
                params.put("package_id", package_id);
                params.put("area_id", area_ids);
                params.put("landmark_id", subarea_ids);
                params.put("hostel_id", hostel_ids);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}