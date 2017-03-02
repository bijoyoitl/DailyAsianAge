package com.dailyasianage.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class NewVersion extends AppCompatActivity {

    class C05951 implements OnClickListener {
        C05951() {
        }

        public void onClick(View v) {
            NewVersion.this.startActivity(new Intent(NewVersion.this, MainActivity.class));
            NewVersion.this.finish();
        }
    }

    class C05962 implements OnClickListener {
        C05962() {
        }

        public void onClick(View v) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("market://details?id=com.dailyasianage.android"));
            NewVersion.this.startActivity(intent);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.new_version);
        ((RelativeLayout) findViewById(R.id.imageView6)).setOnClickListener(new C05951());
        ((ImageView) findViewById(R.id.imageView1)).setOnClickListener(new C05962());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        finish();
        return true;
    }
}
