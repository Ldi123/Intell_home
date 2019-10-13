package edu.jlu.intell_home;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 2015/9/6 0006.
 */
@SuppressLint("ValidFragment")
public class NewListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private FragmentManager fManager;
    private List<RoomBean> Rooms;
    private ListView list_news;
    private  List<String> list = new ArrayList<>();
    public NewListFragment(FragmentManager fManager, List<RoomBean> Rooms) {
        this.fManager = fManager;
        this.Rooms = Rooms;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_newlist, container, false);
        list_news = view.findViewById(R.id.list_news);
        MyAdapter myAdapter = new MyAdapter(Rooms, getActivity());
        list_news.setAdapter(myAdapter);
        list_news.setOnItemClickListener(this);
        return view;
    }


    @SuppressLint("ResourceType")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction fTransaction = fManager.beginTransaction();

        for (DeviceBean deviceGet : Rooms.get(position).getList()) {
          list.add(deviceGet.getDevice_name());
        }
        NewContentFragment ncFragment = new NewContentFragment(position);
        Bundle bd = new Bundle();
        bd.putString("content",list.toString());
        ncFragment.setArguments(bd);
        list.clear();
        //获取Activity的控件
        TextView txt_title = getActivity().findViewById(R.id.txt_title);
        txt_title.setText(Rooms.get(position).getRoom_name());
        //加上Fragment替换动画
        fTransaction.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        fTransaction.replace(R.id.fl_content, ncFragment);
        //调用addToBackStack将Fragment添加到栈中
        fTransaction.addToBackStack(null);
        fTransaction.commit();
    }
}
