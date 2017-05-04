package com.example.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.classes.ClassificationBean;
import com.example.nyapp.MainActivity;
import com.example.nyapp.ProductListActivity;
import com.example.nyapp.R;
import com.example.util.GsonUtils;
import com.example.util.MyGlideUtils;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class Fragment_Classification extends Fragment {
    private ListView listView1;
    private ListView listView2;
    private FirstAdapter adapter;
    private FirstAdapter2 adapter2;
    private MainActivity mActivity;
    private List<ClassificationBean.DataBean> mDataBeen;
    private int currentPosition = 0;
    private SwipeToLoadLayout mSwipeToLoadLayout;

    // 分类
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_classification, container,
                false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView1 = (ListView) view.findViewById(R.id.listView1);
        listView2 = (ListView) view.findViewById(R.id.swipe_target);
        mSwipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                adapter2 = new FirstAdapter2(mDataBeen.get(position).getSubCate());

                listView2.setAdapter(adapter2);
                adapter.setSelectItem(position);
                adapter.notifyDataSetInvalidated();
            }
        });

        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeToLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mDataBeen == null) {
                            initData();
                        } else {
                            adapter2= new FirstAdapter2(mDataBeen.get(currentPosition).getSubCate());
                            listView2.setAdapter(adapter2);
                        }
                        mSwipeToLoadLayout.setRefreshing(false);
                    }
                }, 1000);

            }
        });

        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    /**
     * 获取分类列表的数据并放到适配器里面
     */
    private void initData() {
        MyOkHttpUtils.getData(UrlContact.URL_CLASSIFICATION,new HashMap<String, String>())
                .build()
                .connTimeOut(10000)      // 设置当前请求的连接超时时间
                .readTimeOut(10000)      // 设置当前请求的读取超时时间
                .writeTimeOut(10000)     // 设置当前请求的写入超时时间
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToastUtil.showShortMessage("服务器连接失败!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ClassificationBean classificationBean = GsonUtils.getInstance().fromJson(response, ClassificationBean.class);
                        if (classificationBean.isResult() && classificationBean.getData().size() > 0) {
                            mDataBeen = classificationBean.getData();
                            adapter = new FirstAdapter(mDataBeen);
                            listView1.setAdapter(adapter);

                            adapter2 = new FirstAdapter2(mDataBeen.get(0).getSubCate());
                            listView2.setAdapter(adapter2);
                        } else {
                            Toast.makeText(getActivity(), classificationBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private class FirstAdapter extends BaseAdapter {
        private List<ClassificationBean.DataBean> adaptersubCates;

        FirstAdapter(List<ClassificationBean.DataBean> adaptersubCates) {
            this.adaptersubCates = adaptersubCates;
        }

        @Override
        public int getCount() {
            return adaptersubCates.size();
        }

        @Override
        public Object getItem(int arg0) {
            return adaptersubCates.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            ViewHolder1 viewHolder1 = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item1, null);
                viewHolder1 = new ViewHolder1();
                viewHolder1.text_sub = (TextView) convertView.findViewById(R.id.text_sub);
                viewHolder1.view_linear = convertView.findViewById(R.id.view_linear);
                convertView.setTag(viewHolder1);
            } else {
                viewHolder1 = (ViewHolder1) convertView.getTag();
            }
            viewHolder1.text_sub.setText(adaptersubCates.get(position).getName());
            viewHolder1.view_linear.setBackgroundResource(R.color.white);
            if (position == selectItem) {
                convertView.setBackgroundColor(Color.parseColor("#eeeeee"));
                viewHolder1.text_sub.setTextColor(Color.parseColor("#ff4b00"));
                viewHolder1.view_linear.setBackgroundResource(R.color.linear_classification);
            } else {
                convertView.setBackgroundColor(Color.WHITE);
                viewHolder1.text_sub.setTextColor(Color.parseColor("#000000"));
                viewHolder1.view_linear.setBackgroundResource(R.color.white);
            }

            return convertView;
        }

        void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }

        private int selectItem = 0;

        class ViewHolder1 {
            TextView text_sub;
            View view_linear;
        }
    }

    private class FirstAdapter2 extends BaseAdapter {
        private List<ClassificationBean.DataBean.SubCateBeanList> subCate1s;

        FirstAdapter2(List<ClassificationBean.DataBean.SubCateBeanList> subCate1s) {
            this.subCate1s = subCate1s;
        }

        @Override
        public int getCount() {
            return subCate1s.size();
        }

        @Override
        public Object getItem(int arg0) {
            return subCate1s.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item2, null);
                viewHolder.textView = (TextView) convertView
                        .findViewById(R.id.textView2);
                viewHolder.gridView = (GridView) convertView
                        .findViewById(R.id.gridView_listView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(subCate1s.get(position).getName());
            viewHolder.gridView.setAdapter(new FirstAdapter3(subCate1s.get(
                    position).getSubCate()));
            return convertView;
        }

        class ViewHolder {
            TextView textView;
            GridView gridView;
        }
    }

    private class FirstAdapter3 extends BaseAdapter {
        private List<ClassificationBean.DataBean.SubCateBeanList.SubCateBean> subCate2s;

        FirstAdapter3(List<ClassificationBean.DataBean.SubCateBeanList.SubCateBean> subCate2s) {
            this.subCate2s = subCate2s;
        }

        @Override
        public int getCount() {
            return subCate2s.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {
            ViewHolder2 viewHolder2 = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item3, null);
                viewHolder2 = new ViewHolder2();
                viewHolder2.ItemText = (TextView) convertView.findViewById(R.id.ItemText);
                viewHolder2.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                viewHolder2.LinearLayout01 = (LinearLayout) convertView.findViewById(R.id.LinearLayout01);
                convertView.setTag(viewHolder2);
            } else {
                viewHolder2 = (ViewHolder2) convertView.getTag();
            }
            viewHolder2.ItemText.setText(subCate2s.get(position).getName());
            String url = subCate2s.get(position).getCate_Img();
//            String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1482226344281&di=5244fcd91d62b6196c2eb99e9a7907cb&imgtype=0&src=http%3A%2F%2Fimg10.cn.gcimg.net%2Fgcproduct%2Fday_20140729%2F84d12dfe9faca05500d1dd1784987496.jpg-310x310.jpg";
            MyGlideUtils.loadImage(mActivity, url, viewHolder2.imageView);
            viewHolder2.LinearLayout01.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(getActivity(),ProductListActivity.class);
                    intent.putExtra("type", subCate2s.get(position).getName());
                    intent.putExtra("from", "search");
                    startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder2 {
            TextView ItemText;
            ImageView imageView;
            LinearLayout LinearLayout01;
        }
    }

}
