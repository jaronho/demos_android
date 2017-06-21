package com.example.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.classes.ProductDetailBean;
import com.example.nyapp.R;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 产品详情
 * 参数
 */
public class ParameterFragment extends Fragment {
    @BindView(R.id.text_chengfeng)
    TextView mTextChengfeng;
    @BindView(R.id.text_hanliang)
    TextView mTextHanliang;
    @BindView(R.id.text_manfName)
    TextView mTextManfName;
    @BindView(R.id.text_dengji)
    TextView mTextDengji;
    @BindView(R.id.text_pizhun)
    TextView mTextPizhun;
    @BindView(R.id.text_biaozhun)
    TextView mTextBiaozhun;
    @BindView(R.id.text_jixing)
    TextView mTextJixing;
    @BindView(R.id.text_duxing)
    TextView mTextDuxing;
    private ProductDetailBean.DataBean mDataBean;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment5, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String result = bundle.getString("ProDetailData");
            mDataBean = new Gson().fromJson(result, ProductDetailBean.DataBean.class);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mTextDuxing != null) {
            mTextDengji.setText(mDataBean.getStandardNumber().trim());
            mTextPizhun.setText(mDataBean.getApprovalNumber().trim());
            mTextBiaozhun.setText(mDataBean.getRegistrationNumber().trim());
            mTextManfName.setText(mDataBean.getProductInfo().getManuf_Name().trim());
            mTextJixing.setText(mDataBean.getProductInfo().getDosageform().trim());
            mTextDuxing.setText(mDataBean.getToxicityInstruction().trim());
            mTextChengfeng.setText(mDataBean.getProductInfo().getPercentage().split(":")[0].trim());
            mTextHanliang.setText(mDataBean.getProductInfo().getPercentage().split(":")[1].trim());
        }
    }
}
