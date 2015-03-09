package com.cuc.miti.phone.xmc.xmpp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/** 
 * Activity for displaying the message details view.
 *
 * @author SongQing
 */
public class MessageDetailsActivity extends Activity {

    private static final String LOGTAG = LogUtil.makeLogTag(MessageDetailsActivity.class);

    private String callbackActivityPackageName;

    private String callbackActivityClassName;

    public MessageDetailsActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = this.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        callbackActivityPackageName = sharedPrefs.getString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, "");
        callbackActivityClassName = sharedPrefs.getString(Constants.CALLBACK_ACTIVITY_CLASS_NAME, "");

        Intent intent = getIntent();
        String messageId = intent.getStringExtra(Constants.MESSAGE_ID);
        String messageType = intent.getStringExtra(Constants.MESSAGE_TYPE);
        String messageSender = intent.getStringExtra(Constants.MESSAGE_SENDER);
        String messageReceiver = intent.getStringExtra(Constants.MESSAGE_RECEIVER);
        String messageContent = intent.getStringExtra(Constants.MESSAGE_CONTENT);

        Log.d(LOGTAG, "messageId=" + messageId);
        Log.d(LOGTAG, "messageType=" + messageType);
        Log.d(LOGTAG, "messageSender=" + messageSender);
        Log.d(LOGTAG, "messageReceiver=" + messageReceiver);
        Log.d(LOGTAG, "messageContent=" + messageContent);

        View rootView = createView(messageSender, messageContent, messageReceiver);
        setContentView(rootView);
    }

    private View createView(final String title, final String message,final String uri) {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(0xffeeeeee);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(5, 5, 5, 5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        linearLayout.setLayoutParams(layoutParams);

        TextView textTitle = new TextView(this);
        textTitle.setText(title);
        textTitle.setTextSize(18);
        textTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textTitle.setTextColor(0xff000000);
        textTitle.setGravity(Gravity.CENTER);

        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 30, 30, 0);
        textTitle.setLayoutParams(layoutParams);
        linearLayout.addView(textTitle);

        TextView textDetails = new TextView(this);
        textDetails.setText(message);
        textDetails.setTextSize(14);
        textDetails.setTextColor(0xff333333);
        textDetails.setGravity(Gravity.CENTER);

        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 10, 30, 20);
        textDetails.setLayoutParams(layoutParams);
        linearLayout.addView(textDetails);

        Button okButton = new Button(this);
        okButton.setText("Ok");
        okButton.setWidth(100);

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent;
                if (uri != null
                        && uri.length() > 0
                        && (uri.startsWith("http:") || uri.startsWith("https:")
                                || uri.startsWith("tel:") || uri
                                .startsWith("geo:"))) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                } else {
                    intent = new Intent().setClassName(callbackActivityPackageName,callbackActivityClassName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }

                MessageDetailsActivity.this.startActivity(intent);
                MessageDetailsActivity.this.finish();
            }
        });

        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setGravity(Gravity.CENTER);
        innerLayout.addView(okButton);

        linearLayout.addView(innerLayout);

        return linearLayout;
    }

}
