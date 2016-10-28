package com.android.test.view.listview.expandable;


import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.android.test.R;

public class TestExpandableListViewActivity  extends Activity {


    private MyBaseExpandableListAdpter adpter;  //绑定数据的adpter
    private ExpandableListView listView; //控件
    private ArrayList<String> fatherList;   //放置父类数据
    private List<List<String>> childList;  //子类数据
    private List<String> list;   //中间数据保存量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_expandable_list_view);
        listView = (ExpandableListView) findViewById(R.id.all);  //获取控件对象
        initDate(); //初始化数据
        setAdpter();  //绑定数据
    }
    private void initDate() {
        fatherList=new ArrayList<String>();
        childList=new ArrayList<List<String>>();
        /**
         * 初始化父listview
         */
        fatherList.add("搞笑");
        fatherList.add("原创");
        fatherList.add("娱乐");
        fatherList.add("资讯");
        fatherList.add("汽车");
        fatherList.add("体育");
        /**
         * 初始化子listview
         */
        for (int i = 0; i < fatherList.size(); i++) {
            initChild(i);  //为每一个父类增加子类数据
        }
    }
    private void initChild(int id) {
        list=new ArrayList<String>();
        switch (id) {
            case 0:
                list.add("疯狂搞笑");
                list.add("搞笑综艺");
                list.add("原创搞笑");
                list.add("经典搞笑");
                childList.add(list);
                break;
            case 1:
                list.add("原创影视");
                list.add("音乐动画");
                list.add("火星搞笑");
                list.add("网络红人");
                childList.add(list);
                break;
            case 2:
                list.add("明星八卦");
                list.add("影视资讯");
                childList.add(list);
                break;
            case 3:
                list.add("社会");
                list.add("国内");
                childList.add(list);
            default:
                break;
        }
    }

    private void setAdpter()  //绑定数据
    {
        if (adpter==null) {
            adpter = new MyBaseExpandableListAdpter(fatherList, childList, this);
            listView.setAdapter(adpter);
        }


    }

}