package listeners;

import java.util.ArrayList;
import java.util.HashMap;

import models.DataSource;

/**
 * Created by rahul on 30-01-2016.
 */
public interface NetworkListener {

    public void onDataReceived(DataSource data);

}
