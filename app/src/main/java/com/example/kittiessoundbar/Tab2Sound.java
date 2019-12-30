package com.example.kittiessoundbar;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;


public class Tab2Sound extends Fragment {


    private ListView listView;
    private String[] activities;
    int[] items = {R.raw.problem, R.raw.ok, R.raw.twoonezero, R.raw.step};

    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;

    CallbackManager callbackManager;
    private View mMessengerButton;
    private boolean mPicking;

    int customNumber;

    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2sound, container, false);
        mContext = container.getContext();

        listView = (ListView) rootView.findViewById(R.id.listView);

        Log.d("Widok", listView.toString());

        initResources();
        initActivitiesListView();

        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(mContext);


        return rootView;
    }


    private void initResources() {
        Log.d("initResources", "initResources");

        Resources resources = getResources();
        activities = resources.getStringArray(R.array.activities);
    }


    private void initActivitiesListView() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                mContext, android.R.layout.simple_list_item_1, activities) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView) view.findViewById(android.R.id.text1);

            /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(getResources().getColor(R.color.customWhite));
                textView.setBackgroundResource(R.drawable.customshape);
                return view;
            }
        };


        listView.setAdapter(adapter);
//        listView.setBackgroundColor(getResources().getColor(R.color.customPrimary));
        // background listView
//        listView.setBackgroundResource(R.drawable.);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MediaPlayer mediaPlayer = MediaPlayer.create(mContext, items[position]);
                mediaPlayer.start(); // no need to call prepare(); create() does that for you


                Toast.makeText(mContext, id + " Short click", Toast.LENGTH_SHORT).show();
            }


        });


        listView .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                click(position);

                return true;
            }
        });

    }


    public void dialog(int number) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                .setTitle("My title")
//                .setMessage("Enter password");
        final FrameLayout frameView = new FrameLayout(mContext);
        builder.setView(frameView);

        final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog, frameView);


        this.customNumber = number;

        alertDialog.show();

        Toast.makeText(mContext, number + " ", Toast.LENGTH_SHORT).show();

        mMessengerButton = dialoglayout.findViewById(R.id.messenger_send_button);
        mMessengerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onMessengerButton(customNumber);
                alertDialog.hide();
            }

        });




    }


    private void onMessengerButton(int number) {

        Toast.makeText(mContext, number + " ", Toast.LENGTH_SHORT).show();

        // The URI can reference a file://, content://, or android.resource. Here we use
        // android.resource for sample purposes.


        Uri uri = Uri.parse("android.resource://com.example.kittiessoundbar/" + items[number]);


        // Create the parameters for what we want to send to Messenger.
        ShareToMessengerParams shareToMessengerParams =
                ShareToMessengerParams.newBuilder(uri, "audio/mpeg")
                        .setMetaData("{ \"mp3\" : \"sound\" }")
                        .build();

        if (mPicking) {
            // If we were launched from Messenger, we call MessengerUtils.finishShareToMessenger to return
            // the content to Messenger.


            MessengerUtils.finishShareToMessenger(this.getActivity(), shareToMessengerParams);

        } else {
            // Otherwise, we were launched directly (for example, user clicked the launcher icon). We
            // initiate the broadcast flow in Messenger. If Messenger is not installed or Messenger needs
            // to be upgraded, this will direct the user to the play store.
            MessengerUtils.shareToMessenger(
                    this.getActivity(),
                    REQUEST_CODE_SHARE_TO_MESSENGER,
                    shareToMessengerParams);
        }
    }


    public void click(int pos) {

//        Intent intent;


        switch (pos) {

            case 0:
//                intent = new Intent(MainActivity.this, SoundActivity.class);
                dialog(0);
                break;
            case 1:
//                intent = new Intent(MainActivity.this, SoundActivity.class);
                dialog(1);
                break;
        }
    }


}

