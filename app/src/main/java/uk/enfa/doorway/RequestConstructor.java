package uk.enfa.doorway;

import android.content.res.Resources;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestConstructor {

    private Resources getResources(){
        return AppController.iGetResources();
    }

    private String getToken(){
        return PreferenceManager.getDefaultSharedPreferences(AppController.getContext()).getString("token", "");
    }

    JsonObjectRequest createLoginRequest(
             String username,
             String password,
             Response.Listener<JSONObject> listener,
             @Nullable Response.ErrorListener errorListener
    ) {
        String url = getResources().getString(R.string.api) + getResources().getString(R.string.api_login);

        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
        } catch (Exception e) {
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, json, listener, errorListener);

        return req;
    }

    AuthJsonObjectRequest createTokenCheckRequest(
            Response.Listener<JSONObject> listener,
            @Nullable Response.ErrorListener errorListener
    ) {
        String url = getResources().getString(R.string.api) + getResources().getString(R.string.api_checktoken);

        AuthJsonObjectRequest req = new AuthJsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);

        return req;
    }

    AuthJsonObjectRequest createListDoorRequest(
            Response.Listener<JSONObject> listener,
            @Nullable Response.ErrorListener errorListener
    ) {
        String url = getResources().getString(R.string.api) + getResources().getString(R.string.api_list);
        AuthJsonObjectRequest req = new AuthJsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        return req;
    }

    AuthJsonObjectRequest createGetDoorRequest(
            Response.Listener<JSONObject> listener,
            @Nullable Response.ErrorListener errorListener
    ) {
        String url = getResources().getString(R.string.api) + getResources().getString(R.string.api_door);
        AuthJsonObjectRequest req = new AuthJsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        return req;
    }

    AuthJsonObjectRequest createGetDoorLazyRequest(
            Response.Listener<JSONObject> listener,
            @Nullable Response.ErrorListener errorListener
    ) {
        String url = getResources().getString(R.string.api) + getResources().getString(R.string.api_door_lazyupdate);
        AuthJsonObjectRequest req = new AuthJsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        return req;
    }

    AuthJsonObjectRequest createSetDoorRequest(
            JSONObject options,
            Response.Listener<JSONObject> listener,
            @Nullable Response.ErrorListener errorListener
    ) {
        String url = getResources().getString(R.string.api) + getResources().getString(R.string.api_door);
        AuthJsonObjectRequest req = new AuthJsonObjectRequest(Request.Method.POST, url, options, listener, errorListener);
        return req;
    }
}

class AuthJsonObjectRequest extends JsonObjectRequest {

    public AuthJsonObjectRequest(int method,
                                 String url,
                                 @Nullable JSONObject json,
                                 Response.Listener<JSONObject> listener,
                                 @Nullable Response.ErrorListener errorListener) {
        super(method, url, json, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", AppController.getToken());
        return headers;
    }
}
