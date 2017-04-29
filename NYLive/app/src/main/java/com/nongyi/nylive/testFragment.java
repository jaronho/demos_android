package com.nongyi.nylive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.jaronho.sdk.utils.adapter.QuickRecyclerViewAdapter.QuickViewHolder;
import com.jaronho.sdk.utils.adapter.TreeRecyclerViewAdapter;
import com.jaronho.sdk.utils.adapter.TreeRecyclerViewAdapter.TreeItem;
import com.jaronho.sdk.utils.view.RefreshView;

public class testFragment extends Fragment {
    private static final String TAG = testFragment.class.getSimpleName();
    private RefreshView mRefresh = null;
    private List<TreeItem> mDatas = new ArrayList<>();

    public testFragment() {
        mDatas.add(new ProvinceItem("安徽省", null));
        mDatas.add(new ProvinceItem("福建省", null));
        mDatas.add(new ProvinceItem("甘肃省", null));
        mDatas.add(new ProvinceItem("广东省", null));
        mDatas.add(new ProvinceItem("广西省", null));
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        mRefresh = (RefreshView) view.findViewById(R.id.refreshview_test);
        mRefresh.setHorizontal(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRefresh.getView().setLayoutManager(linearLayoutManager);
        mRefresh.getView().setHasFixedSize(true);
        mRefresh.getView().setAdapter(new TreeRecyclerViewAdapter(getContext(), mDatas));
        return view;
    }

    private ProvinceItem mGanSu = null;
    private class ProvinceItem extends TreeItem<String> {

        public ProvinceItem(String data, TreeItem parent) {
            super(data, parent);
            if (data.equals("甘肃省")) {
                mGanSu = this;
            }
        }

        @Override
        public int getLayoutId(int position, String data) {
            return R.layout.test_province;
        }

        @Override
        public int getSpanSize() {
            return 0;
        }

        @Override
        public List<TreeItem> onInitChildren(String data) {
            List<TreeItem> children = new ArrayList<>();
            if (data.equals("安徽省")) {
                children.add(new CityItem("合肥市", this));
                children.add(new CityItem("芜湖市", this));
                children.add(new CityItem("安庆市", this));
                children.add(new CityItem("蚌埠市", this));
                children.add(new CityItem("亳州市", this));
                children.add(new CityItem("池州市", this));
                children.add(new CityItem("滁州市", this));
                children.add(new CityItem("阜阳市", this));
                children.add(new CityItem("黄山市", this));
                children.add(new CityItem("淮北市", this));
                children.add(new CityItem("淮南市", this));
                children.add(new CityItem("六安市", this));
                children.add(new CityItem("马鞍山市", this));
                children.add(new CityItem("宿州市", this));
                children.add(new CityItem("铜陵市", this));
                children.add(new CityItem("宣城市", this));
            } else if (data.equals("福建省")) {
                children.add(new CityItem("福州市", this));
                children.add(new CityItem("厦门市", this));
                children.add(new CityItem("泉州市", this));
                children.add(new CityItem("龙岩市", this));
                children.add(new CityItem("宁德市", this));
                children.add(new CityItem("南平市", this));
                children.add(new CityItem("莆田市", this));
                children.add(new CityItem("三明市", this));
                children.add(new CityItem("漳州市", this));
            } else if (data.equals("甘肃省")) {
                children.add(new CityItem("兰州市", this));
                children.add(new CityItem("白银市", this));
                children.add(new CityItem("定西市", this));
                children.add(new CityItem("金昌市", this));
                children.add(new CityItem("酒泉市", this));
                children.add(new CityItem("平凉市", this));
                children.add(new CityItem("庆阳市", this));
                children.add(new CityItem("武威市", this));
                children.add(new CityItem("天水市", this));
                children.add(new CityItem("张掖市", this));
                children.add(new CityItem("甘南藏族自治州", this));
                children.add(new CityItem("嘉峪关市", this));
                children.add(new CityItem("临夏回族自治州", this));
                children.add(new CityItem("陇南市", this));
            } else if (data.equals("广东省")) {
                children.add(new CityItem("广州市", this));
                children.add(new CityItem("深圳市", this));
                children.add(new CityItem("珠海市", this));
                children.add(new CityItem("东莞市", this));
                children.add(new CityItem("佛山市", this));
                children.add(new CityItem("惠州市", this));
                children.add(new CityItem("江门市", this));
                children.add(new CityItem("中山市", this));
                children.add(new CityItem("汕头市", this));
                children.add(new CityItem("湛江市", this));
                children.add(new CityItem("潮州市", this));
                children.add(new CityItem("河源市", this));
                children.add(new CityItem("揭阳市", this));
                children.add(new CityItem("茂名市", this));
                children.add(new CityItem("梅州市", this));
                children.add(new CityItem("清远市", this));
                children.add(new CityItem("韶关市", this));
                children.add(new CityItem("汕尾市", this));
                children.add(new CityItem("阳江市", this));
                children.add(new CityItem("云浮市", this));
                children.add(new CityItem("肇庆市", this));
            } else if (data.equals("广西省")) {
                children.add(new CityItem("南宁市", this));
                children.add(new CityItem("北海市", this));
                children.add(new CityItem("防城港市", this));
                children.add(new CityItem("桂林市", this));
                children.add(new CityItem("柳州市", this));
                children.add(new CityItem("崇左市", this));
                children.add(new CityItem("来宾市", this));
                children.add(new CityItem("梧州市", this));
                children.add(new CityItem("河池市", this));
                children.add(new CityItem("玉林市", this));
                children.add(new CityItem("贵港市", this));
                children.add(new CityItem("贺州市", this));
                children.add(new CityItem("钦州市", this));
                children.add(new CityItem("百色市", this));
            }
            return children;
        }

        @Override
        public void onBindViewHolder(QuickViewHolder holder, String data) {
            ((TextView)holder.getView(R.id.textview_name)).setText(data);
        }

        @Override
        public void onClick(String data) {
            Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
        }
    }

    private class CityItem extends TreeItem<String> {

        public CityItem(String data, TreeItem parent) {
            super(data, parent);
        }

        @Override
        public int getLayoutId(int position, String data) {
            return R.layout.test_city;
        }

        @Override
        public int getSpanSize() {
            return 0;
        }

        @Override
        public List<TreeItem> onInitChildren(String data) {
            List<TreeItem> children = new ArrayList<>();
            if (data.equals("漳州市")) {
                children.add(new CountyItem("龙海市", this));
                children.add(new CountyItem("平和县", this));
                children.add(new CountyItem("漳浦县", this));
                children.add(new CountyItem("南靖县", this));
                children.add(new CountyItem("云霄县", this));
                children.add(new CountyItem("诏安县", this));
                children.add(new CountyItem("东山县", this));
                children.add(new CountyItem("长泰县", this));
                children.add(new CountyItem("华安县", this));
            }
            return children;
        }

        @Override
        public void onBindViewHolder(QuickViewHolder holder, final String data) {
            ((TextView)holder.getView(R.id.textview_name)).setText(data);
            final CityItem self = this;
            holder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    self.setData(data + "_选中");
                    mRefresh.getView().getAdapter().notifyDataSetChanged();
//                    Toast.makeText(getContext(), "删除: " + data, Toast.LENGTH_SHORT).show();
//                    ((TreeRecyclerViewAdapter)mRecylerView.getAdapter()).removeItem(self);
//                    ((TreeRecyclerViewAdapter)mRecylerView.getAdapter()).addItem(self, mGanSu, 0);
                    return false;
                }
            });
        }

        @Override
        public void onClick(String data) {
            Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
        }
    }

    private class CountyItem extends TreeItem<String> {

        public CountyItem(String data, TreeItem parent) {
            super(data, parent);
        }

        @Override
        public int getLayoutId(int position, String data) {
            return R.layout.test_county;
        }

        @Override
        public int getSpanSize() {
            return 0;
        }

        @Override
        public List<TreeItem> onInitChildren(String data) {
            return null;
        }

        @Override
        public void onBindViewHolder(QuickViewHolder holder, String data) {
            ((TextView)holder.getView(R.id.textview_name)).setText(data);
        }

        @Override
        public void onClick(String data) {
            Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
        }
    }
}
