package uk.enfa.doorway;

import org.json.JSONObject;

public interface DoorClickListener {
    void onLockedClicked(long id);
    void onUnlockedClicked(long id);
    void onInfoClicked(long id);
}
