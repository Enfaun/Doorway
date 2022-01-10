package uk.enfa.doorway;

import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class UniversalErrorListener implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(AppController.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
    }
}
