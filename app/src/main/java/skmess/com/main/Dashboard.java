package skmess.com.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import skmess.com.R;
import skmess.com.api.URLs;
import skmess.com.main.ui.select.SelectFragment;
import skmess.com.main.ui.home.HomeFragment;
import skmess.com.main.ui.expenses.ExpensesFragment;
import skmess.com.main.ui.complain.ComplainFragment;
import skmess.com.main.ui.slideshow.SlideshowFragment;
import skmess.com.model.VolleySingleton;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button logout;
    private static final String SHARED_PREF_NAME = "Messpref";
    private View navHeader;
    TextView username;
    String usernames,token;
    ImageView check_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        username=(TextView)navHeader.findViewById(R.id.username);
        check_status=(ImageView)findViewById(R.id.check_status);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        usernames = prefs.getString("username", null);
        token = prefs.getString("token", null);

        if(usernames!= null){
            username.setText(usernames);
        }

        logout=(Button)navHeader.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                finish();
                Intent intent = new Intent(Dashboard.this, Login.class);
                startActivity(intent);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
        Dashboard.this.setTitle("Dashboard");

        Checkcancelday();
    }

    @Override
    public void onBackPressed() {
        backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Dashboard.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Dashboard.this.finish();
                    }
                });
        alertDialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
            Dashboard.this.setTitle("Dashboard");

        } else if (id == R.id.nav_meal) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, new SelectFragment()).commit();
            Dashboard.this.setTitle("Select / Change Meal");

        } else if (id == R.id.nav_expenses) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, new ExpensesFragment()).commit();
            Dashboard.this.setTitle("Cancel Meal");

        }
        else if (id == R.id.nav_cancellist) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, new SlideshowFragment()).commit();
            Dashboard.this.setTitle("Report");

        }
        else if(id == R.id.nav_complain){

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, new ComplainFragment()).commit();
            Dashboard.this.setTitle("About Us");

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Checkcancelday() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_checkcancelday,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject c = new JSONObject(response);
                            if(c.getBoolean("status")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    check_status.setImageDrawable(getResources().getDrawable(R.drawable.onmess, getApplicationContext().getTheme()));
                                } else {
                                    check_status.setImageDrawable(getResources().getDrawable(R.drawable.onmess));
                                }
                            }else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    check_status.setImageDrawable(getResources().getDrawable(R.drawable.offmess, getApplicationContext().getTheme()));
                                } else {
                                    check_status.setImageDrawable(getResources().getDrawable(R.drawable.offmess));
                                }
                            }

                        }catch (JSONException e){
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
                params.put("token",token);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
