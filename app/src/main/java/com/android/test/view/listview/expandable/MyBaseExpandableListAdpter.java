package com.android.test.view.listview.expandable;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.test.R;


public class MyBaseExpandableListAdpter extends BaseExpandableListAdapter {
    public final static String TAG = "MyAdpter";

    //数据
    public List<String> fatherDataList;
    public List<List<String>> childDataList;
    private Context context;

    /** 构造函数 **/
    public MyBaseExpandableListAdpter(List<String> faList, List<List<String>> chList, Context context) {
        this.fatherDataList = faList;
        this.childDataList = chList;
        this.context = context;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childDataList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View childView = LayoutInflater.from(context).inflate(R.layout.expandable_child_item, null);
        TextView textView = (TextView) childView.findViewById(R.id.all_list_text_item_id);
        textView.setText(childDataList.get(groupPosition).get(childPosition));
        return childView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition<childDataList.size()&&childDataList.get(groupPosition)!=null){
            return childDataList.get(groupPosition).size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return fatherDataList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return fatherDataList.size();  ////父类item总数
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;   //父类位置
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.expandable_father_item, null);
        TextView textView = (TextView) view.findViewById(R.id.all_list_text_id);
        textView.setText(fatherDataList.get(groupPosition));
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {  //点击子类触发事件
        Log.e(TAG,"第" + groupPosition + "大项，第" + childPosition + "小项被点击了");
        return true;

    }

}