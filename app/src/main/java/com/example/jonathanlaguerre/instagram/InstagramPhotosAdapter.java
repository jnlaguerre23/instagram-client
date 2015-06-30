package com.example.jonathanlaguerre.instagram;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.w3c.dom.Text;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by jonathanlaguerre on 6/22/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto>{
    // What data do we need from the activity
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);

    }

    //Create a viewHolder object
    private static class ViewHolder {
        TextView tvCaption;
        ImageView ivPhoto;
        TextView tvLikeCount;
        ImageView ivUserProfile;
        TextView tvUserName;
        TextView timestamp;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get Data

        String colorP = "#FF0000";
        ViewHolder viewHolder;
        InstagramPhoto photo = getItem(position);

        //Check if are using a recycled view
        if(convertView == null) {
            viewHolder = new ViewHolder();
            //Create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            viewHolder.tvLikeCount = (TextView)convertView.findViewById(R.id.tvLikes);
            viewHolder.tvCaption = (TextView)convertView.findViewById(R.id.tvCaption);
            viewHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
            viewHolder.ivUserProfile = (RoundedImageView)convertView.findViewById(R.id.ivUserProfile);
            viewHolder.tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.timestamp = (TextView)convertView.findViewById(R.id.tvTimeStamp);

            convertView.setTag(viewHolder);

        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String html = "<font color='" + "#315E89" + "'>" + photo.username + "</font> " + photo.caption;

        //Set html text to tvCaption
        viewHolder.tvCaption.setText(Html.fromHtml(html));

        // Clear out the imageview
        viewHolder.ivPhoto.setImageResource(0);
        //Insert the image using picasso
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.placeholder_image).into(viewHolder.ivPhoto);

        // username
        viewHolder.tvUserName.setText(photo.username);


        viewHolder.tvLikeCount.setText(photo.likesCount + "likes");

        // change the color of the heart icon
        //viewHolder.tvLikeCount.setCom
        Drawable heart = viewHolder.tvLikeCount.getCompoundDrawables()[0];
        ColorFilter color = new LightingColorFilter(Color.parseColor(colorP), Color.parseColor(colorP));
        heart.setColorFilter(color);


        viewHolder.ivUserProfile.setImageResource(0);
        Picasso.with(getContext()).load(photo.profilePicture).placeholder(R.drawable.placeholder_image).into(viewHolder.ivUserProfile);
        viewHolder.tvUserName.setText(photo.username);

        return convertView;

    }
}
