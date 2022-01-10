package uk.enfa.doorway;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class DoorwayCardAdapter extends BaseAdapter {
    private Context context;
    private JSONArray doors;
    public DoorwayCardAdapter(Context context, JSONArray doors){
        this.context = context;
        this.doors = doors;

    }

    @Override
    public int getCount() {
        return doors.length();
    }

    @Override
    public Object getItem(int position) {
        try{
            return doors.get(position);
        }catch (Exception e){
            return -1;
        }
    }

    @Override
    public long getItemId(int position) {
        try{
            return doors.getJSONObject(position).getLong("id");
        }catch (Exception e){
            return -1;
        }
    }

    @Override
    public View getView(int position, View card, ViewGroup parent){
        if (card == null){
            card = LayoutInflater.from(context).inflate(R.layout.card_doorway, null);
        }
        try{
            JSONObject door = doors.getJSONObject(position);
            Boolean isLocked = door.getBoolean("locked");
            TextView title = (TextView)card.findViewById(R.id.door_textViewTitle);
            TextView doorId = (TextView)card.findViewById(R.id.door_textViewId);
            Button locked = (Button)card.findViewById(R.id.door_buttonLocked);
            Button unlocked = (Button)card.findViewById(R.id.door_buttonUnlocked);
            //ImageView image = (ImageView)card.findViewById(R.id.door_imageView);

            title.setText(door.getString("title"));
            doorId.setText("#" + door.getLong("id"));
            if(isLocked){
                locked.setVisibility(View.INVISIBLE);
                unlocked.setVisibility(View.VISIBLE);
            }else{ // unlocked
                locked.setVisibility(View.VISIBLE);
                unlocked.setVisibility(View.INVISIBLE);
            }


        }catch (Exception e){

        }

        return card;
    }
}
