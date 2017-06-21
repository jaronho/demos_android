package com.example.nyapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.classes.Products1;

import java.text.DecimalFormat;

public class MydaigouWindousDialogActivity extends Activity {
    private Products1 products;
    private TextView product_name, text_spec, text_price, text_count,
            text_shouyibili, text_daigou_price, text_daigoushouyi;
    DecimalFormat ddf1 = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wordpanel);
        products = (Products1) getIntent().getSerializableExtra("Product");
        product_name = (TextView) findViewById(R.id.text_prodctname);
        text_spec = (TextView) findViewById(R.id.text_spec);
        text_price = (TextView) findViewById(R.id.text_price);
        text_count = (TextView) findViewById(R.id.text_count);
        text_shouyibili = (TextView) findViewById(R.id.text_shouyibili);
        text_daigou_price = (TextView) findViewById(R.id.text_daigou_price);
        text_daigoushouyi = (TextView) findViewById(R.id.text_daigoushouyi);

        product_name.setText(products.getName());

        text_spec.setText(products.getSpec());

        text_shouyibili.setText(products.getScale());


        text_price.setText(products.getPrice());
        text_count.setText(products.getCount());

        text_daigou_price.setText(products.getMoney());
        text_daigoushouyi.setText(products.getLucreMoney());
    }

}
