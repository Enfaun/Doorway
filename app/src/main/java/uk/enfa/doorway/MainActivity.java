package uk.enfa.doorway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import uk.enfa.doorway.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    RequestConstructor requestConstructor;
    JSONObject doors;
    JSONArray doorsArr;
    DoorwayCardRecyclerAdapter doorwayCardRecyclerAdapter;
    Timer lazyLoadTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);
        requestConstructor = new RequestConstructor();
        checkLogin(false);
        lazyLoadTimer = new Timer();
        lazyLoadTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkLoginSafe(true);
            }
        }, 10000, 10000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin(true);
    }

    private  void checkLoginSafe(Boolean lazyload){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkLogin(lazyload);
            }
        });
    }

    private void checkLogin(Boolean lazyload){
        if(!lazyload) Toast.makeText(getApplicationContext(), R.string.toast_authenticating, Toast.LENGTH_SHORT).show();
        String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("token", null);

        JsonObjectRequest req =  requestConstructor.createTokenCheckRequest(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(!response.getBoolean("ok")){
                        if(!lazyload) Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(intent);
                        return;
                    }
                    Toast.makeText(getApplicationContext(), R.string.toast_authed, Toast.LENGTH_SHORT).show();
                    if(!lazyload || (doors==null)) {
                        loadAllDoors();
                    }else{
                        updateDoors();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new UniversalErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    private void loadAllDoors(){
        JsonObjectRequest req = requestConstructor.createListDoorRequest(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                doors = response;
                try{
                    if(!doors.getBoolean("ok"))
                        Toast.makeText(getApplicationContext(), doors.toString(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                populateDoors();
            }
        }, new UniversalErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    private void populateDoors(){
        try {
            doorsArr = doors.getJSONArray("doors");
            doorwayCardRecyclerAdapter = new DoorwayCardRecyclerAdapter(doorsArr);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            binding.doors.setLayoutManager(linearLayoutManager);
            binding.doors.setAdapter(doorwayCardRecyclerAdapter);

        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDoors(){
        JsonObjectRequest req = requestConstructor.createGetDoorLazyRequest(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                doors = response;
                try{
                    if(!doors.getBoolean("ok"))
                        Toast.makeText(getApplicationContext(), doors.toString(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                refreshRecycler();
            }
        }, new UniversalErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    private void refreshRecycler(){
        try {
            JSONArray lazyDoorsArr = doors.getJSONArray("doors");
            for(int i=0; i<doorsArr.length(); i++){
                JSONObject oldDoor = doorsArr.getJSONObject(i);
                for(int j=0; j<lazyDoorsArr.length(); j++){
                    JSONObject newDoor = lazyDoorsArr.getJSONObject(j);
                    if(oldDoor.getLong("id") == newDoor.getLong("id")){
                        doorsArr.put(i, newDoor);
                    }
                }
            }
            doorwayCardRecyclerAdapter.notifyDataSetChanged();
        }catch(Exception e){}
    }

}