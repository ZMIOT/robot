package ldu.guofeng.imdemo.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.bean.TrainInfo;


/**
 * Created by Administrator on 2019/4/2.
 */

public class TrainInfoAdapter extends ArrayAdapter<TrainInfo> {
    private int resourceId;
    public TrainInfoAdapter(@NonNull Context context, @LayoutRes int resource,List<TrainInfo> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TrainInfo trainInfo=getItem(position);
        View view1=(CardView)view.findViewById(R.id.cardView);
        TextView deptTime=(TextView)view1.findViewById(R.id.deptTime);
        deptTime.setText(trainInfo.getDeptTime());
        TextView deptStation=(TextView)view1.findViewById(R.id.deptStationName);
        deptStation.setText(trainInfo.getDeptStationName());
        TextView runTime=(TextView)view1.findViewById(R.id.runTime);
        runTime.setText(trainInfo.getRunTime());
        TextView trainCode=(TextView)view1.findViewById(R.id.trainCode);
        trainCode.setText(trainInfo.getTrainCode());
        TextView arrTime=(TextView)view1.findViewById(R.id.arrTime);
        arrTime.setText(trainInfo.getArrTime());
        TextView arrStation=(TextView)view1.findViewById(R.id.arrStationName);
        arrStation.setText(trainInfo.getArrStationName());
        TextView seatPrice=(TextView)view1.findViewById(R.id.seatPrice);
        seatPrice.setText(trainInfo.getTrainTickets().get(0).getPrice());
        return view;
    }
}
