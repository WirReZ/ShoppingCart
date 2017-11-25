package com.wirrez.shoppingcart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.rustamg.filedialogs.FileDialog;
import com.rustamg.filedialogs.OpenFileDialog;
import com.rustamg.filedialogs.SaveFileDialog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;


public class SettingsActivity extends AppCompatActivity implements SaveFileDialog.OnFileSelectedListener {
    public FragmentManager fragMan;
    public String action;
    public static final int PERMISION_REQUEST_SAVE = 100;
    public static final int PERMISION_REQUEST_OPEN = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferencesScreen ps = new PreferencesScreen();
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, ps)
                .commit();

        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
        }
        fragMan = getSupportFragmentManager();
        action = "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFileSelected(FileDialog dialog, File file) {

        if (action == "save") {
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            FileWriter outputStream;
            Database db = new Database(getApplicationContext());
            try {
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "categories");
                for (PrimaryDrawerItem catItem : db.GetCategoryItems()) {
                    serializer.startTag("", "category");
                    serializer.attribute(null, "icon", String.valueOf(db.getIconOfCategoyItem(catItem.getIdentifier())));
                    serializer.attribute("", "name", String.valueOf(catItem.getTag()));
                    serializer.attribute("", "id", String.valueOf(catItem.getIdentifier()));
                    for (Item item : db.getItems(catItem.getIdentifier())) {
                        serializer.startTag("", "item");
                        serializer.attribute("", "id", String.valueOf(item._id));
                        serializer.attribute("", " name", String.valueOf(item._name));
                        serializer.attribute("", "count", String.valueOf(item._count));
                        serializer.attribute("", "unit", String.valueOf(item._unit));
                        serializer.endTag("", "item");
                    }
                    serializer.endTag("", "category");
                }
                serializer.endTag("", "categories");
                serializer.endDocument();
                outputStream = new FileWriter(file);
                outputStream.write(writer.toString());
                outputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (action == "open") {
            try {
                FileInputStream fis = new FileInputStream(file);
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(fis, null);
                int eventType = parser.getEventType();
                long lastid = -1;
                Database db = new Database(getApplicationContext());
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String element = null;
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            element = parser.getName();
                            if (element.equalsIgnoreCase("category")) {
                                String name = parser.getAttributeValue(null, "name");
                                String icon = parser.getAttributeValue("", "icon");
                                lastid = db.InsertCategoryItem(name, icon);
                            } else if (element.equalsIgnoreCase("item")) {
                                String name = parser.getAttributeValue(null, "name");
                                String count = parser.getAttributeValue(null, "count");
                                String unit = parser.getAttributeValue(null, "unit");
                                if (lastid != -1) {
                                    db.InsertItem(name, count, lastid, unit);
                                }
                            }
                            break;
                    }
                    eventType = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        action = "";
    }


    @SuppressLint("ValidFragment")
    public class PreferencesScreen extends PreferenceFragment implements ActivityCompat.OnRequestPermissionsResultCallback {
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Preference btnXML = findPreference("downloadXML");
            btnXML.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    if (ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_GRANTED) {
                        callSaveDialog();
                    }else
                    {
                        requestPermissionForSave();
                    }
                    return true;
                }
            });
            Preference btnUploadXML = findPreference("uploadXML");
            btnUploadXML.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_GRANTED) {
                        callOpenDialog();
                    }else
                    {
                        requestPermissionForOpen();
                    }
                    return true;
                }
            });

        }

    }
    private void callSaveDialog()
    {
        action = "save";
        FileDialog dialog = new SaveFileDialog();
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
        Bundle args = new Bundle();
        args.putString(FileDialog.EXTENSION, ".xml");
        dialog.setArguments(args);
        dialog.show(fragMan, SaveFileDialog.class.getName());
    }
    private void callOpenDialog()
    {
        action = "open";
        FileDialog dialog = new OpenFileDialog();
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
        Bundle args = new Bundle();
        args.putString(FileDialog.EXTENSION, "xml");
        dialog.setArguments(args);
        dialog.show(fragMan, OpenFileDialog.class.getName());
    }

    private void requestPermissionForSave() {
            ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISION_REQUEST_SAVE);
    }
    private void requestPermissionForOpen() {
            ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISION_REQUEST_OPEN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case PERMISION_REQUEST_OPEN:
            {
                if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    callOpenDialog();
                }else
                {
                    Toast.makeText(this,R.string.permission_need,Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case PERMISION_REQUEST_SAVE:
            {
                if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    callSaveDialog();
                }else
                {
                    Toast.makeText(this,R.string.permission_need,Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}