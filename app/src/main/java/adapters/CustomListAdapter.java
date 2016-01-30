package adapters;

import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
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
 */

public class CustomListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<HashMap<String,String>> mData;
    LayoutInflater mInflater;

    public CustomListAdapter(Context context,
                             ArrayList<HashMap<String, String>> data){

        this.mContext = context;
        this.mData = data;
        if (mContext instanceof MainActivity)
            this.mInflater = ((MainActivity) mContext).getLayoutInflater();

    }

    public static class ViewHolder {

        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private ImageView mImageView;
    }

    @Override
    public int getCount() {
        return mData.size();
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

        ViewHolder viewHolder;
        String title, description, imageURL;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mTitleTextView = (TextView)convertView.findViewById(R.id.title_text_view);
            viewHolder.mDescriptionTextView = (TextView) convertView.findViewById(R.id.description_text_view);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image_view);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(mData != null) {
            HashMap<String,String> map = mData.get(position);

            title = map.get(Constants.TITLE);
            description = map.get(Constants.DESCRIPTION);
            imageURL = map.get(Constants.IMAGE_URL);

            viewHolder.mTitleTextView.setText(title);
            viewHolder.mDescriptionTextView.setText(description);
            if(imageURL != null) {
                Picasso.with(mContext)
                        .load(imageURL)
                        .placeholder(R.drawable.notification_template_icon_bg)
                        .error(R.drawable.notification_template_icon_bg)
                        .into(viewHolder.mImageView);
            }

        }


        return convertView;
    }
}