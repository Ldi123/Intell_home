package edu.jlu.intell_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jay on 2015/9/6 0006.
 */
public class MyAdapter extends BaseAdapter{

    private List<RoomBean> mData;
    private Context mContext;

    public MyAdapter(List<RoomBean> Rooms, Context mContext) {
        this.mData = Rooms;
        this.mContext = mContext;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.txt_item_title = convertView.findViewById(R.id.txt_item_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_item_title.setText(mData.get(position).getRoom_name());
        return convertView;
    }

    private class ViewHolder{
        TextView txt_item_title;
    }

}
