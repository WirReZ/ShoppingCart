package com.wirrez.shoppingcart;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PreferencesScreen())
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
                outputStream = new FileWriter(file+".xml");
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

    public class PreferencesScreen extends PreferenceFragment {
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
                    //Make backup XML
                    action = "save";
                    FileDialog dialog = new SaveFileDialog();
                    dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
                    Bundle args = new Bundle();
                    dialog.show(fragMan, SaveFileDialog.class.getName());


                    return true;
                }
            });
            Preference btnUploadXML = findPreference("uploadXML");
            btnUploadXML.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    action = "open";
                    FileDialog dialog = new OpenFileDialog();
                    dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
                    Bundle args = new Bundle();
                    args.putString(FileDialog.EXTENSION, "xml");
                    dialog.show(fragMan, OpenFileDialog.class.getName());
                    return true;
                }
            });

        }

    }


}