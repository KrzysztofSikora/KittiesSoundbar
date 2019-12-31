package pl.krzysztofsikora.kittiessoundbar;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.kittiessoundbar.R;
import com.facebook.FacebookSdk;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;

import java.util.Objects;


public class TabSoundFragment extends Fragment {
    private ListView listView;
    private String[] activities;
    private Context mContext;
    private int[] items = {
            R.raw.catmeow2,
            R.raw.tomcatmrsmith,
            R.raw.catmeowsmrsmith,
            R.raw.catmeowing2mrsmith,
            R.raw.lionsoundbiblecom,
            R.raw.catmeowingsoundbiblecom,
            R.raw.catmeowmikekoening,
            R.raw.kittenmeow,
            R.raw.catmeow9
    };
    private int customNumber;
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    private boolean mPicking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_sound, container, false);
        mContext = container.getContext();
        listView = rootView.findViewById(R.id.listView);
        Log.d("View: ", listView.toString());
        initResources();
        initActivitiesListView();
        FacebookSdk.sdkInitialize(mContext);

        return rootView;
    }

    private void initResources() {
        Log.d("InitResources: ", "initResources");
        Resources resources = getResources();
        activities = resources.getStringArray(R.array.activities);
    }

    private void initActivitiesListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                mContext, android.R.layout.simple_list_item_1, activities) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);

                /* YOUR CHOICE OF COLOR */
                textView.setTextColor(getResources().getColor(R.color.customWhite));
                textView.setBackgroundResource(R.drawable.customshape);
                return view;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaPlayer mediaPlayer = MediaPlayer.create(mContext, items[position]);
                mediaPlayer.start(); // no need to call prepare(); create() does that for you
//                Toast.makeText(mContext, id + " Short click", Toast.LENGTH_SHORT).show();
            }


        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialog(position);
                return true;
            }
        });
    }

    private void dialog(int number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final FrameLayout frameView = new FrameLayout(mContext);
        builder.setView(frameView);

        final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog, frameView);

        this.customNumber = number;
        alertDialog.show();
        Toast.makeText(mContext, number + " ", Toast.LENGTH_SHORT).show();

        View mMessengerButton = dialoglayout.findViewById(R.id.messenger_send_button);
        mMessengerButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                onMessengerButton(customNumber);
                alertDialog.hide();
            }

        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void onMessengerButton(int number) {
        Toast.makeText(mContext, number + " ", Toast.LENGTH_SHORT).show();

        // The URI can reference a file://, content://, or android.resource. Here we use

        Uri uri = Uri.parse("android.resource://com.example.kittiessoundbar/" + items[number]);

        // Create the parameters for what we want to send to Messenger.
        ShareToMessengerParams shareToMessengerParams =
                ShareToMessengerParams.newBuilder(uri, "audio/mpeg")
                        .setMetaData("{ \"mp3\" : \"sound\" }")
                        .build();
        if (mPicking) {
            // If we were launched from Messenger, we call MessengerUtils.finishShareToMessenger to return
            // the content to Messenger.
            MessengerUtils.finishShareToMessenger(Objects.requireNonNull(this.getActivity()), shareToMessengerParams);
        } else {
            // Otherwise, we were launched directly (for example, user clicked the launcher icon). We
            // initiate the broadcast flow in Messenger. If Messenger is not installed or Messenger needs
            // to be upgraded, this will direct the user to the play store.
            MessengerUtils.shareToMessenger(
                    Objects.requireNonNull(this.getActivity()),
                    REQUEST_CODE_SHARE_TO_MESSENGER,
                    shareToMessengerParams);
        }
    }
}

