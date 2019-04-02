package ldu.guofeng.imdemo.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.bean.Book;



/**
 * Created by Administrator on 2019/3/30.
 */

public class BookInfoAdapter extends ArrayAdapter<Book>{
    private int resourceId;
    public BookInfoAdapter(@NonNull Context context, @LayoutRes int resource, List<Book> objects) {
        super(context, resource,objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Book book=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView bookImg=(ImageView)view.findViewById(R.id.bookimg);
        Glide.with(getContext()).load(book.getImgurl()).into(bookImg);
        TextView title=(TextView)view.findViewById(R.id.title);
        title.setText(book.getBookname());
        TextView score=(TextView)view.findViewById(R.id.score);
        score.setText(book.getScore());
        TextView author=(TextView)view.findViewById(R.id.author);
        author.setText(book.getAuthor());
        return view;
    }
}
