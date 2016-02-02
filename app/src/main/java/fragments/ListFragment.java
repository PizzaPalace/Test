package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.testapp.assignment.R;

import java.util.ArrayList;
import java.util.List;

import models.DataSource;
import adapters.CustomListAdapter;
import constants.Constants;
import models.Details;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {


    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance() {

        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        this.mListView = (ListView)view.findViewById(android.R.id.list);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_to_refresh);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    /*
     * Required to retrieve Fragment's state when device is rotated. State is persisted
     * in onSaveInstanceState()
     *
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){

            List<Details> data = (List<Details>)savedInstanceState.getSerializable(Constants.DATA_STORE_KEY);
            setAdapter(data);
        }
    }

    /**
     * Save data associated with the Fragment's adapter in a Bundle, required to persist the Fragment's state
     * @param bundle Bundle to store an Arr
     */
    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        DataSource source = new DataSource();
        bundle.putSerializable(Constants.DATA_STORE_KEY,(ArrayList<Details>)source.getDetails());
        source = null;
    }

    /**
     * Listener implementation to pass data to Activity is performed here.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     *
     * @param data Data to bind with the Fragment's adapter
     */
    public void setAdapter(List<Details> data){

        CustomListAdapter adapter = new CustomListAdapter(getActivity(),data);
        mListView.setAdapter(adapter);
    }

    /**
     * Called when a user swipes across the top of the page. The listener is implemented
     * in the Activity that then fetches data from the network.
     */
    @Override
    public void onRefresh() {

        mListView.setAdapter(null);
        mListener.onSwipeInteraction();
    }

    /**
     * Clears the swipe to refresh widget.
     */
    public void dismissRefresh(){
        if(mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        // Associated with the SwipeToRefresh widget
        void onSwipeInteraction();
    }
}
