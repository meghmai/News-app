package com.example.android.news;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    private Context context;
    private List<News> objects;
    private static final String middle = "T";
    private static final String end = "Z";

    public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
        this.objects = objects;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        News currentnews = getItem(position);
        TextView section = (TextView) listItemView.findViewById(R.id.section);
        section.setText(currentnews.getMsection());
        TextView title = (TextView) listItemView.findViewById(R.id.title);
        title.setText(currentnews.getMtitle());
        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(currentnews.getMauthor());
        String totaldate = currentnews.getMdate();
        String date, time;
        String parts[] = totaldate.split(middle);
        date = parts[0];
        String part[] = parts[1].split(end);
        time = part[0];
        TextView Date = (TextView) listItemView.findViewById(R.id.date);
        Date.setText(date);
        TextView Time = (TextView) listItemView.findViewById(R.id.time);
        Time.setText(time);
        try {
            ImageView img = (ImageView) listItemView.findViewById(R.id.image);
            img.setImageResource(R.drawable.imagebefore);
            Picasso.with(context)
                    .load(currentnews.getMimg())
                    .placeholder(R.drawable.imagebefore)
                    .error(R.drawable.imagebefore)
                    .resize(100, 100)
                    .into(img);
        } catch (Exception e) {
        }
        return listItemView;
    }

}



