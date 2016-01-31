package listeners;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 30-01-2016.
 */
public interface NetworkListener {

    public void onDataReceived(ArrayList<HashMap<String,String>> data);
    public void onTitleReceived(String title);
    // passes both data and title in the same method.
    public void onInformationReceived(ArrayList<HashMap<String,String>> data,String title);

}
