package constants;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by rahul on 30-01-2016.
 */
public class Constants {

    // Key corresponding to JSON feed.
    // Each JSONObject contains three elements with keys title,description and imageHref
    // The TITLE key corresponds to the title to be displayed in the Activity
    public static final String TITLE = "title";

    // URL to parse data from server
    public static final String URL = "https://dl.dropboxusercontent.com/u/746330/facts.json";

    // Keys for local use
    public static final String DATA_STORE_KEY = "DATA_STORE_KEY";
    public static final String REQUEST_QUEUE_TAG = "REQUEST_QUEUE_TAG";
}
