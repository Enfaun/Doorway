package uk.enfa.doorway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

public class DoorwayCardRecyclerAdapter extends RecyclerView.Adapter<DoorwayCardRecyclerAdapter.ViewHolder> {

    private JSONArray doors;

    public DoorwayCardRecyclerAdapter(JSONArray doors){
        this.doors = doors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_doorway, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DoorwayCardRecyclerAdapter.ViewHolder holder, int position) {
        try{
            JSONObject door = doors.getJSONObject(position);
            Boolean isLocked = door.getBoolean("locked");
            holder.getTitle().setText(door.getString("title"));
            holder.getDoorId().setText("#" + door.getLong("id"));
            if(isLocked){
                holder.getLocked().setVisibility(View.INVISIBLE);
                holder.getUnlocked().setVisibility(View.VISIBLE);
            }else{ // unlocked
                holder.getLocked().setVisibility(View.VISIBLE);
                holder.getUnlocked().setVisibility(View.INVISIBLE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return doors.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView doorId;
        private final Button locked;
        private final Button unlocked;
        public ViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.door_textViewTitle);
            doorId = (TextView)view.findViewById(R.id.door_textViewId);
            locked = (Button)view.findViewById(R.id.door_buttonLocked);
            unlocked = (Button)view.findViewById(R.id.door_buttonUnlocked);
        }
        public TextView getTitle() {
            return title;
        }
        public TextView getDoorId() {
            return doorId;
        }
        public Button getLocked() {
            return locked;
        }
        public Button getUnlocked() {
            return unlocked;
        }
    }
}
