package listeners;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 30-01-2016.
 */
public interface DataListener {

    public void onDataDownloaded(ArrayList<HashMap<String,String>> data);
}
