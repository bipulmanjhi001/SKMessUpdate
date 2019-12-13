package skmess.com.main.ui.slideshow;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import skmess.com.adapter.ExtraMealAdapter;
import skmess.com.adapter.ExtraMealModel;
import skmess.com.api.URLs;
import skmess.com.model.VolleySingleton;
import static android.content.Context.MODE_PRIVATE;

public class SlideshowFragment extends Fragment {

    private ListView meal_daily_list2;
    private ListView meal_daily_list3;
    private static final String SHARED_PREF_NAME = "Messpref";
    private String token;
    ProgressBar meal_progress2;
    ProgressBar meal_progress3;
    ArrayList<ExtraMealModel> extraMealModel;
    ExtraMealAdapter extraMealAdapter;
    ArrayList listItem=new ArrayList();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = prefs.getString("token", null);

        extraMealModel=new ArrayList<ExtraMealModel>();
        meal_daily_list2=(ListView)root.findViewById(R.id.meal_daily_list2);
        meal_progress2=(ProgressBar)root.findViewById(R.id.meal_progress2);

        meal_daily_list3=(ListView)root.findViewById(R.id.meal_daily_list3);
        meal_progress3=(ProgressBar)root.findViewById(R.id.meal_progress3);
        ExtraMeallist();

        return root;
    }

    public void ExtraMeallist() {
        meal_progress2.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_extrameallist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONArray meal= obj.getJSONArray("message");
                                for(int i=0; i<meal.length(); i++){
                                    JSONObject object=meal.getJSONObject(i);

                                    String date = object.getString("date");
                                    String breakfast = object.getString("breakfast");
                                    String lunch = object.getString("lunch");
                                    String dinner = object.getString("dinner");
                                    String price = object.getString("price");

                                    ExtraMealModel mealModel=new ExtraMealModel();
                                    mealModel.setDate(date);
                                    mealModel.setBreakfast(breakfast);
                                    mealModel.setLunch(lunch);
                                    mealModel.setDinner(dinner);
                                    mealModel.setPrice(price);
                                    extraMealModel.add(mealModel);
                                }
                                meal_progress2.setVisibility(View.GONE);
                                LayoutInflater myinflater = getLayoutInflater();
                                ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.meal_header, meal_daily_list2, false);
                                meal_daily_list2.addHeaderView(myHeader, null, false);
                                LayoutInflater myinflater2 = getLayoutInflater();
                                ViewGroup myHeader2 = (ViewGroup)myinflater2.inflate(R.layout.meal_footer, meal_daily_list2, false);
                                meal_daily_list2.addFooterView(myHeader2, null, false);
                                extraMealAdapter=new ExtraMealAdapter(extraMealModel,getActivity());
                                meal_daily_list2.setAdapter(extraMealAdapter);
                                extraMealAdapter.notifyDataSetChanged();

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
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        CancelsList();
    }

    public void CancelsList(){
        meal_progress3.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_cancelorderlist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("message");

                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String duration = itemslist.getString("duration");
                                    listItem.add(duration);
                                }
                            }
                            meal_progress3.setVisibility(View.GONE);
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
                            meal_daily_list3.setAdapter(adapter);

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