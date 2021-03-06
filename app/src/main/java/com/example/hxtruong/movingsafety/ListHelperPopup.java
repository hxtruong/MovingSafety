package com.example.hxtruong.movingsafety;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListHelperPopup extends Activity {
    public static final String EXTRA_POPUP_MESSAGE = "com.example.hxtruong.ListHelperPopup";
    private ListView listView;
    protected View view;
    private Helper[] helperArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_helper_pop_up);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 0.8f), (int) (height * 0.8f));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

        ArrayList<Helper> objs = getIntent().getParcelableArrayListExtra(EXTRA_POPUP_MESSAGE);
        helperArray = objs.toArray(new Helper[objs.size()]);
        displayListHelpers();
    }

    private void displayListHelpers() {
        listView = (ListView) findViewById(R.id.listView);
        ListHelperAdapter listHelperAdapter = new ListHelperAdapter();
        listView.setAdapter(listHelperAdapter);
    }


    class ListHelperAdapter extends BaseAdapter {
        public ListHelperAdapter() {
            view = getLayoutInflater().inflate(R.layout.list_helper, null);
        }

        @Override
        public int getCount() {
            return helperArray.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.list_helper, null);
            displayInfoHelper(view, helperArray[position]);
            // send data to detail info
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DetailInfo.class);
                    intent.putExtra(DetailInfo.EXTRA_DETAIL_MESSAGE, helperArray[position]);
                    startActivity(intent);
                }
            });

            // call intent
            ImageButton callBtn = view.findViewById(R.id.popupCallBtn);
            callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + helperArray[position].getPhoneNumber()));
                    startActivity(intent);
                }
            });
            return view;
        }

        private void displayInfoHelper(View view, Helper helper) {
            ImageView panel_IMG_back = view.findViewById(R.id.helperAvatar);
            Context context = panel_IMG_back.getContext();
            int bgId = context.getResources().getIdentifier(helper.getAvatarLink(), "drawable", context.getPackageName());
            Glide.with(getApplicationContext()).load(bgId).into(panel_IMG_back);

            TextView name = view.findViewById(R.id.helperName);
            name.setText(helper.getName());

            TextView distance = view.findViewById(R.id.helperDistance);
            String distanceText = Integer.toString((int) helper.getDistanceToCurrenLocation() / 100);
            distance.setText(distanceText + " km");
        }

    }
}
