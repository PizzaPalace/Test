package adapters;

import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.testapp.assignment.R;
import com.testapp.assignment.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

import constants.Constants;

/**
 * Created by rahul on 30-01-2016.
 *  Adapter class for the Fragment's ListView. Has a ViewHolder to hold
 *  references to views in each listitem. Each listitem comprises of 2 TextViews and
 *  an ImageView. The first TextView is a title for a listitem. The second
 *  TextView displays a description if present along an ImageView in a 2:1 ratio
 *  along the same row (refer list_element.xml).
 *
 *  Note - This class can be replaced by the more recent RecylcerView class.
 */

public class CustomListAdapter extends BaseAdapter{

    // An Activity context to obtain reference to LayoutInflater
    Context mContext;
    // Indexed data structure to use in getView(..) method.
    ArrayList<HashMap<String,String>> mData;
    // LayoutInflater variable to inflate views.
    LayoutInflater mInflater;

    public CustomListAdapter(Context context,
                             ArrayList<HashMap<String, String>> data){

        this.mContext = context;
        this.mData = data;
        if (mContext instanceof MainActivity)
            this.mInflater = ((MainActivity) mContext).getLayoutInflater();

    }

    /**
     * View Holder class required to maintain references to
     * already inflated views. This is an optimization technique
     * to avoid expensive re-inflations as the listview scrolls.
     */
    public static class ViewHolder {

        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private ImageView mImageView;
    }

    @Override
    public int getCount() {
        // ensure that mData is not null and set count equal to the arraylist's size.
        // return 0 otherwise.
        if(mData != null)
            return mData.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // declare a viewHolder variable.
        ViewHolder viewHolder;
        // declare String variables for the title, desciption and imageURL.
        String title, description, imageURL;

        // check if convertView has been inflated, if not do this here.
        if(convertView == null) {

            // inflate convertView.
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            // create ViewHolder instance.
            viewHolder = new ViewHolder();
            // obtain references to corresponding views.
            viewHolder.mTitleTextView = (TextView)convertView.findViewById(R.id.title_text_view);
            viewHolder.mDescriptionTextView = (TextView) convertView.findViewById(R.id.description_text_view);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image_view);
            // set viewHolder as tag for convertView to retrieve if it has already been inflated.
            convertView.setTag(viewHolder);
        }
        else{
            // if convertView is already inflated, obtain reference to viewHolder from
            // convertView's tag
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // check if data obtained from JSONParsers is not null.
        if(mData != null) {
            // get map corresponding to each position in arraylist.
            HashMap<String,String> map = mData.get(position);

            // get title, description and imageURL reference.
            title = map.get(Constants.TITLE);
            description = map.get(Constants.DESCRIPTION);
            imageURL = map.get(Constants.IMAGE_URL);

            // apply data to each view.
            viewHolder.mTitleTextView.setText(title);

            // leaves out descriptions containing null in the listview.
            if(description != null && !description.equals("null")) {
                viewHolder.mDescriptionTextView.setText(description);
            }
            else{
                viewHolder.mDescriptionTextView.setText("");
            }
            // Uses the Picasso library to render images if present in each listitem
            // if images are absent, a placeholder is displayed. Is cached by default.
            // Cache size can be adjusted using other Picasso APIs if required.
            Picasso.with(mContext)
                    .load(imageURL)
                    .placeholder(android.R.drawable.stat_notify_error)
                    .error(android.R.drawable.stat_notify_error)
                    .into(viewHolder.mImageView);

        }

        return convertView;
    }
}
