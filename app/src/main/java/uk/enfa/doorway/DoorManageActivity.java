package uk.enfa.doorway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import uk.enfa.doorway.databinding.ActivityDoorManageBinding;

public class DoorManageActivity extends AppCompatActivity {
    ActivityDoorManageBinding binding;
    RequestConstructor requestConstructor;
    Timer reloadTimer;
    private long doorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_door_manage);
        binding = ActivityDoorManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()
                .setHomeButtonEnabled(true);
        Intent intent = getIntent();
        doorId = intent.getLongExtra("id", -1);
        getSupportActionBar().setTitle("Door ID: " + doorId);
        requestConstructor = new RequestConstructor();
        reloadTimer = new Timer();
        reloadTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchDoorSafe();
            }
        }, 2000, 2000);
        fetchDoor();
    }

    private void fetchDoor(){
        JsonObjectRequest req = requestConstructor.createGetDoorRequest(doorId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    binding.card.doorButtons.setVisibility(View.INVISIBLE);
                    binding.card.doorTextViewTitle.setText(response.getString("title"));
                    binding.card.doorTextViewId.setText("#" + response.getLong("id"));
                    binding.switchLock.setChecked(response.getBoolean("locked"));
                    binding.textViewActivities.setText(response.getString("activities"));
                }catch (Exception e){

                }
            }
        }, new UniversalErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    private void fetchDoorSafe(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fetchDoor();
            }
        });
    }
}
