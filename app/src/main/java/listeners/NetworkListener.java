package listeners;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 30-01-2016.
 */
public interface NetworkListener {

    public void onDataReceived(ArrayList<HashMap<String,String>> data);
    public void onTitleReceived(String title);
}
