package com.fasgmail.bilal.top10downloader;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bilal S on 30-May-17.
 */

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater; //final can;t be changed
    private List<FeedEntry> applications;
//    Bitmap bitmap = BitmapFactory.decodeFile(get);


    public FeedAdapter(@NonNull Context context, @LayoutRes int resource, List<FeedEntry> applications) { //context of movie screening, needs to know room, price etc.
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context); //gives custom views for XML objects
        this.applications = applications;

    }

    @Override
    public int getCount() { // needs to be public because itll be called by listview which is in seperate package
        return applications.size();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //when calling this method, listvew tells it the posistion
        // of the item it needs to dsplay, so we retrieve object at this position from applications list

//        View view = layoutInflater.inflate(layoutResource, parent, false); //creating view by inflating layout resource, parent,,, android is sending us back parent
//        TextView tvName=(TextView) convertView.findViewById(tvName); //finds 3 widgets by findviwbyid..but here we're using View.find...
//        TextView tvArtist=(TextView) convertView.findViewById(R.id.tvArtist); //we put view. because we're inflating the view
//        TextView tvSummary=(TextView) convertView.findViewById(R.id.tvSummary); //we want you to find the id thats part of this View

        FeedEntry currentApp = applications.get(position);

        viewHolder.tvName.setText(currentApp.getName());
        viewHolder.tvArtist.setText(currentApp.getArtist());
        viewHolder.tvSummary.setText(currentApp.getSummary());


        return convertView;
    }

    private class ViewHolder {
        final TextView tvName;
        final TextView tvArtist;
        final TextView tvSummary;
        final ImageView imageView;

        ViewHolder(View v) {
            this.tvName = (TextView) v.findViewById(R.id.tvName); //finds 3 widgets by findviwbyid..but here we're using View.find...
            this.tvArtist = (TextView) v.findViewById(R.id.tvArtist); //we put view. because we're inflating the view
            this.tvSummary = (TextView) v.findViewById(R.id.tvSummary);
            this.imageView = (ImageView) v.findViewById(R.id.imageView);
        }
    }
}
