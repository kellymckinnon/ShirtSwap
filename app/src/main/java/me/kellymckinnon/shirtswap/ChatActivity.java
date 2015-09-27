package me.kellymckinnon.shirtswap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import me.kellymckinnon.shirtswap.R;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, me.kellymckinnon.shirtswap.MessageDataSource.MessagesCallbacks {

    public static final String USER_EXTRA = "USER";

    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    private me.kellymckinnon.shirtswap.User mRecipient;
    private ListView mListView;
    private Date mLastMessageDate = new Date();
    private String mConvoId;
    private me.kellymckinnon.shirtswap.MessageDataSource.MessagesListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mRecipient = (me.kellymckinnon.shirtswap.User) getIntent().getSerializableExtra(USER_EXTRA);

        mListView = (ListView) findViewById(R.id.messages_list);

        mMessages = new ArrayList<>();
        mAdapter = new MessagesAdapter(mMessages);

        mListView.setAdapter(mAdapter);

        setTitle(mRecipient.getFirstName());
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button sendButton = (Button) findViewById(R.id.send_message);
        sendButton.setOnClickListener(this);

        String[] ids = {mRecipient.getId(), me.kellymckinnon.shirtswap.UserDataSource.getCurrentUser().getId()};
        Arrays.sort(ids);
        mConvoId = ids[0] + ids[1];

        mListener = me.kellymckinnon.shirtswap.MessageDataSource.addMessageListener(mConvoId, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        me.kellymckinnon.shirtswap.MessageDataSource.stop(mListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        EditText newMessageView = (EditText) findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
        newMessageView.setText("");

        Message msg = new Message();
        msg.setDate(new Date());
        msg.setText(newMessage);
        msg.setSender(me.kellymckinnon.shirtswap.UserDataSource.getCurrentUser().getId());

        me.kellymckinnon.shirtswap.MessageDataSource.saveMessage(msg, mConvoId);
    }

    @Override
    public void onMessageAdded(Message message) {
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
    }

    private class MessagesAdapter extends ArrayAdapter<Message> {

        MessagesAdapter(ArrayList<Message> messages) {
            super(ChatActivity.this, R.layout.message, R.id.message, messages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            Message message = getItem(position);

            TextView nameView = (TextView) convertView.findViewById(R.id.message);
            nameView.setText(message.getText());

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) nameView.getLayoutParams();

            if(message.getSender().equals(UserDataSource.getCurrentUser().getId())) {
                nameView.setBackground(getDrawable(R.drawable.bubble_right_green));
                layoutParams.gravity = Gravity.END;
            } else {
                nameView.setBackground(getDrawable(R.drawable.bubble_left_gray));
                layoutParams.gravity = Gravity.LEFT;
            }
            nameView.setLayoutParams(layoutParams);

            return convertView;
        }
    }
}
