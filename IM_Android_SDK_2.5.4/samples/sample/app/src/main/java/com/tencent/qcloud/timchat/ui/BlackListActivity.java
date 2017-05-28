package com.tencent.qcloud.timchat.ui;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.adapters.ProfileSummaryAdapter;
import com.tencent.qcloud.timchat.model.FriendProfile;
import com.tencent.qcloud.timchat.model.ProfileSummary;

import java.util.ArrayList;
import java.util.List;

public class BlackListActivity extends Activity {

    private final String TAG = "BlackListActivity";

    ProfileSummaryAdapter adapter;
    List<ProfileSummary> list = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        listView = (ListView) findViewById(R.id.list);
        adapter = new ProfileSummaryAdapter(this, R.layout.item_profile_summary, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.get(position).onClick(BlackListActivity.this);
            }
        });
        TIMFriendshipManager.getInstance().getBlackList(new TIMValueCallBack<List<String>>() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "get black list error code : " + i);
            }

            @Override
            public void onSuccess(List<String> ids) {
                TIMFriendshipManager.getInstance().getFriendsProfile(ids, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int i, String s) {
                        Log.e(TAG, "get profile error code : " + i);
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        for (TIMUserProfile item : timUserProfiles){
                            FriendProfile profile = new FriendProfile(item);
                            list.add(profile);
                        }
                        adapter.notifyDataSetChanged();

                    }
                });

            }
        });
    }
}
