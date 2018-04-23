package com.example.dell.sendbird.groupcchannel;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.sendbird.R;
import com.example.dell.sendbird.main.ConnectionManager;
import com.example.dell.sendbird.utils.PreferenceUtils;
import com.example.dell.sendbird.utils.PushUtils;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class GroupChannelActivity extends AppCompatActivity implements SelectDistinctFragment.DistinctSelectedListener{
    private boolean mIsDistinct = true;
    private List<String> mSelectedIds;
    public static final String EXTRA_NEW_CHANNEL_URL = "EXTRA_NEW_CHANNEL_URL";
    static final int STATE_SELECT_USERS = 0;
    String userId,userNickname;
    static final int STATE_SELECT_DISTINCT = 1;private int mCurrentState;
    int randomNumber,length=1,range=9;
    private UserListQuery mUserListQuery;
    private SelectableUserListAdapter mListAdapter;
    private List<String> mSelectedUserIds;

    private EditText edittext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_channel);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            userId = bundle.getString("userId");
            userNickname = bundle.getString("nickname");
        }
//        Typeface typeface  = Typeface.createFromAsset(getAssets(), "myfont.ttf");
//        myTextView.setTypeface(typeface);
        //  edittext=findViewById(R.id.edittext_group_chat_message);
        mSelectedIds = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_group_channel);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24_dp);
            getSupportActionBar().setTitle("");
        }

        mSelectedUserIds = new ArrayList<>();

        if (savedInstanceState == null) {
            // Load list of Group Channels
            mIsDistinct = PreferenceUtils.getGroupChannelDistinct();

            Log.e("userId "+userId,"nickname "+userNickname);
           // userNickname="itscomplicatedapp";
            PreferenceUtils.setUserId(userId);
            PreferenceUtils.setNickname(userNickname);

            connectToSendBird(userId, userNickname);

            //the user I begin the chat with
            mSelectedIds.add("admin123");

        }
        mListAdapter = new SelectableUserListAdapter(this, false, true);
        mListAdapter.setItemCheckedChangeListener(new SelectableUserListAdapter.OnItemCheckedChangeListener() {
            @Override
            public void OnItemChecked(User user, boolean checked) {
                if (checked) {
                    mSelectedUserIds.add((user.getUserId()));
                    Log.e("user list",""+user.getUserId());
                } else {
                    mSelectedUserIds.remove(user.getUserId());
                }

                // If no users are selected, disable the invite button.
                if (mSelectedUserIds.size() > 0) {
                    //   mInviteButton.setEnabled(true);
                } else {
                    //   mInviteButton.setEnabled(false);
                }
            }
        });

        String channelUrl = getIntent().getStringExtra("groupChannelUrl");
        //   Log.e("channelurl",""+channelUrl);
        if(channelUrl != null) {
            // If started from notification
            Fragment fragment = GroupChatFragment.newInstance(channelUrl);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    interface onBackPressedListener {
        boolean onBack();
    }
    private onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            //  getSupportActionBar().setTitle(title);
        }
    }
    @Override
    public void onDistinctSelected(boolean distinct) {
        mIsDistinct = distinct;
    }

    void setState(int state) {
        if (state == STATE_SELECT_USERS) {
            mCurrentState = STATE_SELECT_USERS;
            //    mCreateButton.setVisibility(View.VISIBLE);
            //  mNextButton.setVisibility(View.GONE);
//            mCreateButton.setVisibility(View.GONE);
//            mNextButton.setVisibility(View.VISIBLE);
        } else if (state == STATE_SELECT_DISTINCT){
            mCurrentState = STATE_SELECT_DISTINCT;
            //    mCreateButton.setVisibility(View.VISIBLE);
            //  mNextButton.setVisibility(View.GONE);
        }
    }


    /**
     * Creates a new Group Channel.
     *
     * Note that if you have not included empty channels in your GroupChannelListQuery,
     * the channel will not be shown in the user's channel list until at least one message
     * has been sent inside.
     *
     * @param userIds   The users to be members of the new channel.
     * @param distinct  Whether the channel is unique for the selected members.
     *                  If you attempt to create another Distinct channel with the same members,
     *                  the existing channel instance will be returned.
     */
    private void createGroupChannel(List<String> userIds, boolean distinct) {
        Log.e("gropu chat channel",""+userIds);
        Log.e("gropu chat channel",""+distinct);
        try {
//        if(PreferenceUtils.getConnected()) {
//            Log.e("value"," "+PreferenceUtils.getUserId()+ PreferenceUtils.getNickname());
//            connectToSendBird(PreferenceUtils.getUserId(), PreferenceUtils.getNickname());
//        }
            GroupChannel.createChannelWithUserIds(userIds, distinct, new GroupChannel.GroupChannelCreateHandler() {
                @Override
                public void onResult(GroupChannel groupChannel, SendBirdException e) {
                    Log.e("gropu old channel", "" + groupChannel);
                    Log.e("gropu channe", "" + groupChannel.getUrl());
                    if (e != null) {
                        // Error!
                        Log.e("gropu old error", "" + e);
                        return;
                    }

                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_NEW_CHANNEL_URL, groupChannel.getUrl());
                    setResult(RESULT_OK, intent);
                    // finish();

                    String url = groupChannel.getUrl();

                    Fragment fragment = GroupChatFragment.newInstance(url);

                    FragmentManager manager = getSupportFragmentManager();
                    manager.popBackStack();

                    manager.beginTransaction()
                            .replace(R.id.container_group_channel, fragment)
                            .commit();

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Attempts to connect a user to SendBird.
     * @param userId    The unique ID of the user.
     * @param userNickname  The user's nickname, which will be displayed in chats.
     */
    private void connectToSendBird(final String userId, final String userNickname) {
        // Show the loading indicator
        //  showProgressBar(true);
        //  mConnectButton.setEnabled(false);
        //   Log.e("here ","failed ");
        ConnectionManager.login(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                // Callback received; hide the progress bar.
                //  showProgressBar(false);

                if (e != null) {
                    // Error!
                    Toast.makeText(
                            GroupChannelActivity.this, "" + e.getCode() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show login failure snackbar
                    Log.e("Login to SendBird ","failed " +e);
                    // mConnectButton.setEnabled(true);
                    PreferenceUtils.setConnected(false);
                    return;
                }

                PreferenceUtils.setNickname(user.getNickname());
                PreferenceUtils.setProfileUrl(user.getProfileUrl());
                PreferenceUtils.setConnected(true);
                Log.e("Logging","okay "+e);
                // Update the user's nickname

                updateCurrentUserInfo(userNickname);
                updateCurrentUserPushToken();

                createGroupChannel(mSelectedIds, mIsDistinct);

                // Proceed to MainActivity
//                Intent intent = new Intent(GroupChannelActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }

    /**
     * Update the user's push token.
     */
    private void updateCurrentUserPushToken() {
      //  PushUtils.registerPushTokenForCurrentUser(GroupChannelActivity.this, null);
    }

    /**
     * Updates the user's nickname.
     * @param userNickname  The new nickname of the user.
     */
    private void updateCurrentUserInfo(final String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            GroupChannelActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show update failed snackbar
                    Log.e("Update user"," nickname failed");

                    return;
                }

                PreferenceUtils.setNickname(userNickname);
            }
        });
    }

    private void loadInitialUserList(int size) {
        mUserListQuery = SendBird.createUserListQuery();

        mUserListQuery.setLimit(size);
        mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(List<User> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                Log.e("user list",""+list);
                Log.e("user list",""+list.get(0));
                String first= String.valueOf(list.get(0));
                mListAdapter.setUserList(list);
                Log.e("user list",""+mListAdapter);
            }
        });
    }



}
