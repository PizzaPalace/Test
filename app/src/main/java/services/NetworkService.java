package services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

import constants.Common;
import listeners.NetworkListener;
import receivers.NetworkReceiver;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NetworkService extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_NETWORK = "services.action.NETWORK";
    public static final String SERVICE_KEY = "com.testapp.assignment.SERVICE_KEY";

    public NetworkService() { super("NetworkService"); }

    public static void readFromNetwork(Context context) {

        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_NETWORK);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if(ACTION_NETWORK.equals(action)){
                Common.fetchDataAndBroadcast(this);
            }
        }
    }
}
