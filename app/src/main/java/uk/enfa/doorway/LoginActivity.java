package uk.enfa.doorway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import uk.enfa.doorway.databinding.ActivityLoginBinding;
import uk.enfa.doorway.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String savedUsername = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("username", "");
        getSupportActionBar().setTitle("Log in");
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.editTextTextUsername.setText(savedUsername);
        binding.buttonLogin.setOnClickListener(this::onLogin);
    }
    private void onLogin(View v){
        String username = binding.editTextTextUsername.getText().toString();
        String password = binding.editTextTextPassword.getText().toString();

        JsonObjectRequest req =  new RequestConstructor().createLoginRequest(username, password, new LoginListener(), new LoginErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    private class LoginListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            try{
                if(!response.getBoolean("ok")){
                    Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                    return;
                }
                editor.putString("username", LoginActivity.this.binding.editTextTextUsername.getText().toString());
                editor.putString("token", response.getString("token"));
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
            editor.commit();
            LoginActivity.this.finish();
        }
    }

    private class LoginErrorListener implements  Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            LoginActivity.this.binding.editTextTextPassword.setText("");
            LoginActivity.this.binding.editTextTextPassword.requestFocus();
        }
    }
}

