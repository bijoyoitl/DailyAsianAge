package com.dailyasianage.android;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dailyasianage.android.Database.NewsDatabase;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {
    private Context context;
    private Toolbar toolbar;
    private Button button;
    public int size = 30;
    DetailsActivity activity = null;
    private LinearLayout ratingsLayout;
    private NewsDatabase database;
    private TextView textViewFont;
    public TextView textViewFontSize;

    private LinearLayout fontSizeLayout;

    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        this.context = this;
        database = new NewsDatabase(context);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ratingsLayout = (LinearLayout) findViewById(R.id.ratingsLayout);
        textViewFontSize = (TextView) findViewById(R.id.textViewFontSize);
        fontSizeLayout = (LinearLayout) findViewById(R.id.fontSizeLayout);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                AlertDialogRadio alert = new AlertDialogRadio();
                alert.show(manager, "alert_dialog_radio");
            }
        };

        fontSizeLayout.setOnClickListener(listener);
        String fontLevel = database.getFontLevel();
        textViewFontSize.setText(fontLevel);

        ratingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.dailyasianage.android")));
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class AlertDialogRadio extends DialogFragment {
        public TextView textViewFontSize;
        final String[] items = {"Small", "Normal", "Large"};
        NewsDatabase newsDatabase;
        Integer selection = 3;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            newsDatabase = new NewsDatabase(getActivity());
            textViewFontSize = (TextView) getActivity().findViewById(R.id.textViewFontSize);
            /** Creating a builder for the alert dialog window */
            android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(getActivity());

            /** Setting a title for the window */
            b.setTitle("News Font Size");

            String selector = newsDatabase.getFontSelector();
            int selectorPosition = Integer.parseInt(selector);

            /** Setting items to the alert dialog */
            b.setSingleChoiceItems(items, selectorPosition, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selection = which;
                    switch (which) {
                        case 0:
                            newsDatabase.updateFontSize(String.valueOf(15));
                            newsDatabase.updateSelector(String.valueOf(0));
                            newsDatabase.updateFontLevel("Small");
                            textViewFontSize.setText("Small");
                            break;
                        case 1:
                            newsDatabase.updateFontSize(String.valueOf(25));
                            newsDatabase.updateSelector(String.valueOf(1));
                            newsDatabase.updateFontLevel("Normal");
                            textViewFontSize.setText("Normal");
                            break;
                        case 2:
                            newsDatabase.updateFontSize(String.valueOf(35));
                            newsDatabase.updateSelector(String.valueOf(2));
                            newsDatabase.updateFontLevel("Large");
                            textViewFontSize.setText("Large");
                            break;
                        default:
                            break;
                    }


                    dialog.dismiss();
                }
            });


            b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });

            android.app.AlertDialog d = b.create();

            return d;
        }
    }

}
