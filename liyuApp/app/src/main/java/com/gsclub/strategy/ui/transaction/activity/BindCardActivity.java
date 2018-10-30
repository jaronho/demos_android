package com.gsclub.strategy.ui.transaction.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.Constants;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.contract.transaction.BindCardContract;
import com.gsclub.strategy.contract.transaction.SetTradingPasswordContract;
import com.gsclub.strategy.model.bean.BankBean;
import com.gsclub.strategy.model.bean.UserBankBean;
import com.gsclub.strategy.presenter.transaction.BindCardPresenter;
import com.gsclub.strategy.presenter.transaction.SetTradingPasswordPresenter;
import com.gsclub.strategy.ui.view.MyEditText;
import com.gsclub.strategy.util.BankCardTextWatcher;
import com.gsclub.strategy.util.IDTextWatcher;
import com.gsclub.strategy.util.SimpleTextWatcher;
import com.gsclub.strategy.util.ToastUtil;

import javax.xml.transform.Result;

import butterknife.BindView;
import butterknife.OnClick;

public class BindCardActivity extends BaseActivity<BindCardPresenter> implements BindCardContract.View {
    @BindView(R.id.et_name)
    MyEditText etName;
    @BindView(R.id.et_id)
    MyEditText etID;
    @BindView(R.id.et_bank_card)
    EditText etBankCard;
    @BindView(R.id.et_reserved_phone)
    EditText etReservedPhone;
    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    private String bankId;

    public static void start(Context context) {
        context.startActivity(new Intent(context, BindCardActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bind_card;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.bind_card);
        etID.addTextChangedListener(new IDTextWatcher(etID));
        etBankCard.addTextChangedListener(new BankCardTextWatcher(etBankCard));
        TextWatcher watcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                setBtnEnabled();
            }
        };
        etName.addTextChangedListener(watcher);
        etID.addTextChangedListener(watcher);
        etBankCard.addTextChangedListener(watcher);
        etReservedPhone.addTextChangedListener(watcher);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getBankInfo();
    }

    @OnClick({R.id.tv_sure, R.id.layout_bank})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
//                if(TextUtils.isEmpty(bankId)) {
//                    ToastUtil.showMsg("请选择银行");
//                    return;
//                }
                String real_name = etName.getText().toString().replace(" ", "");
                String id_num = etID.getText().toString().replace(" ", "");
                if(id_num.contains("X")) {
                    id_num.replace("X", "x");
                }
                String bank_no = etBankCard.getText().toString().replace(" ", "");
                String binding_tel = etReservedPhone.getText().toString().replace(" ", "");
                mPresenter.bindCard(real_name, id_num, bank_no, bankId, binding_tel);
                break;
            case R.id.layout_bank:
                BankListActivity.start(this);
                break;
        }
    }

    @Override
    public void loadData(UserBankBean data) {
        if(data == null) return;
        String name = data.getRealName();
        if(!TextUtils.isEmpty(name)) {
            etName.setText(name);
            etName.setEnabled(false);
            etName.setClearIconVisible(false);
        }
        String id = data.getCardId();
        if(!TextUtils.isEmpty(id)) {
            etID.setText(id);
            etID.setEnabled(false);
            etID.setClearIconVisible(false);
        }
    }

    private void setBtnEnabled() {
        if (TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etID.getText()) || TextUtils.isEmpty(etBankCard.getText()) || TextUtils.isEmpty(etReservedPhone.getText()) ) {
            tvSure.setEnabled(false);
            return;
        }
        tvSure.setEnabled(true);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == Constants.REQUEST_BANK_LIST) {
//            if(resultCode == RESULT_OK) {
//                if(data == null) return;
//                BankBean item = (BankBean) data.getSerializableExtra(Constants.KEY_BANK_INFO);
//                if(item == null) return;
//                tvBank.setText(item.bank_name);
//                bankId = item.id;
//            }
//        }
//    }
}
