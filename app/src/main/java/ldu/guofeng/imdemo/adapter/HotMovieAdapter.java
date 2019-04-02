package ldu.guofeng.imdemo.adapter;

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

import com.bumptech.glide.Glide;

import java.util.List;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.bean.HotMovie;

/**
 * Created by Administrator on 2019/3/30.
 */

public class HotMovieAdapter extends ArrayAdapter<HotMovie> {
    int resourceId;
    public HotMovieAdapter(@NonNull Context context, @LayoutRes int resource, List<HotMovie> objects) {
        super(context, resource,objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HotMovie hotMovie=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView bookImg=(ImageView)view.findViewById(R.id.hotMovieImg);
        Glide.with(getContext()).load(hotMovie.getImgurl()).into(bookImg);
        TextView title=(TextView)view.findViewById(R.id.hotMovieTitle);
        title.setText(hotMovie.getTitle());
        TextView score=(TextView)view.findViewById(R.id.hotMovieScore);
        score.setText(hotMovie.getScore());
        return view;
    }
}
