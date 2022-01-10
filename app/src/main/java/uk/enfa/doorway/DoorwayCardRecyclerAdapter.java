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

    private final JSONArray doors;
    private final DoorClickListener listener;

    public DoorwayCardRecyclerAdapter(JSONArray doors, DoorClickListener listener){
        this.doors = doors;
        this.listener = listener;
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
            long id = door.getLong("id");
            Boolean isLocked = door.getBoolean("locked");
            holder.getTitle().setText(door.getString("title"));
            holder.getDoorId().setText("#" + door.getLong("id"));
            if(isLocked){
                holder.getLocked().setVisibility(View.VISIBLE);
                holder.getUnlocked().setVisibility(View.INVISIBLE);
            }else{ // unlocked
                holder.getLocked().setVisibility(View.INVISIBLE);
                holder.getUnlocked().setVisibility(View.VISIBLE);
            }
            holder.getLocked().setOnClickListener( v -> {
                listener.onLockedClicked(id);
            });
            holder.getUnlocked().setOnClickListener( v -> {
                listener.onUnlockedClicked(id);
            });
            holder.getInfo().setOnClickListener( v -> {
                listener.onInfoClicked(id);
            });

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
        private final Button info;
        public ViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.door_textViewTitle);
            doorId = (TextView)view.findViewById(R.id.door_textViewId);
            locked = (Button)view.findViewById(R.id.door_buttonLocked);
            unlocked = (Button)view.findViewById(R.id.door_buttonUnlocked);
            info = (Button)view.findViewById(R.id.door_buttonInfo);
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
        public Button getInfo() {
            return info;
        }
    }
}
