package com.wirrez.shoppingcart;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.tbouron.shakedetector.library.ShakeDetector;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingMenuLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private AccountHeader headerResult = null;
    private FlowingDrawer mDrawer = null;
    private float ToolBarOffset;
    private boolean ToolBarFlipped;
    private DrawerArrowDrawable drawerArrowDrawable;
    private static CustomItemAdapter adapter;
    private RecyclerView recyclerView;
    private Drawer DrawerMenu;
    private long lastSelection;
    private Database db;
    private FlowingMenuLayout fml;
    private CoordinatorLayout mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawer = findViewById(R.id.drawerlayout);
        fml = findViewById(R.id.menulayout);
        recyclerView = findViewById(R.id.rvFeed);
        mContent = findViewById(R.id.content);
        setToolBar();

        lastSelection = 1;
        updateListView(lastSelection);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));

        recyclerView.setLayoutManager(mLayoutManager);
        com.melnykov.fab.FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DrawerMenu.getCurrentSelection() == -1 || db.GetCategoryItems().length == 0)Snackbar.make(mContent, R.string.select_category, Snackbar.LENGTH_SHORT).show();
                else
                {
                    MaterialDialog dialog = new AddItemActivity(MainActivity.this, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            AutoCompleteTextView name = (AutoCompleteTextView) dialog.findViewById(R.id.name);
                            TextView qty = (TextView) dialog.findViewById(R.id.qty);
                            AutoCompleteTextView units = (AutoCompleteTextView) dialog.findViewById(R.id.unit);




                            if (!name.getText().toString().isEmpty() && !qty.getText().toString().isEmpty() && !units.getText().toString().isEmpty() && lastSelection != -1) {
                                long id = db.InsertItem(name.getText().toString(), qty.getText().toString(), lastSelection, units.getText().toString());
                                DrawerMenu.removeAllItems();
                                DrawerMenu.addItems(db.GetCategoryItems());
                                DrawerMenu.setSelection(lastSelection);
                                updateListView(lastSelection);
                                dialog.dismiss();
                            } else {
                                Snackbar.make(mContent, R.string.missing_field, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }
                    ).autoDismiss(false).build();
                    dialog.show();
                }
            }
        });
        fab.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new TouchListener(getApplicationContext(), recyclerView, new TouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CustomItemAdapter.ViewHolder itmPosition = (CustomItemAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                db.crossItem(itmPosition._id);
                updateListView(lastSelection);
            }
            @Override
            public void onLongClick(View view, int position) {
                 final CustomItemAdapter.ViewHolder itmPosition = (CustomItemAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);

                new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.edit_item)
                        .items(R.array.item_choice)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which)
                                {
                                    case 0: {
                                        MaterialDialog dialogEdit = new EditItemActivity(MainActivity.this, new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                TextView name = (TextView) dialog.findViewById(R.id.name);
                                                TextView qty = (TextView) dialog.findViewById(R.id.qty);
                                                TextView units = (TextView) dialog.findViewById(R.id.unit);
                                                if (!name.getText().toString().isEmpty() && !qty.getText().toString().isEmpty() && !units.getText().toString().isEmpty() && lastSelection != -1) {
                                                    boolean id = db.updateItem(itmPosition._id,name.getText().toString(), qty.getText().toString(),  units.getText().toString());
                                                    updateListView(lastSelection);
                                                    DrawerMenu.removeAllItems();
                                                    DrawerMenu.addItems(db.GetCategoryItems());
                                                    DrawerMenu.setSelection(lastSelection);
                                                    dialog.dismiss();
                                                } else {
                                                    Snackbar.make(mContent, R.string.missing_field, Snackbar.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                            }
                                        }).autoDismiss(false).build();

                                        EditText name  = (EditText) dialogEdit.getCustomView().findViewById(R.id.name);
                                        EditText qty  = (EditText) dialogEdit.getCustomView().findViewById(R.id.qty);
                                        EditText unit  = (EditText) dialogEdit.getCustomView().findViewById(R.id.unit);
                                        name.setText(itmPosition._name);
                                        qty.setText(String.valueOf(itmPosition._qty));
                                        unit.setText(itmPosition._unit);
                                        dialogEdit.show();
                                        break;
                                    }
                                    case 1: {
                                        boolean ok = db.deleteItem(itmPosition._id);
                                        if(ok) Snackbar.make(mContent, R.string.item_deleted, Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                db.InsertItem(itmPosition._name,String.valueOf(itmPosition._qty),lastSelection,itmPosition._unit);
                                                Toast.makeText(MainActivity.this,R.string.recovered,Toast.LENGTH_SHORT).show();
                                                DrawerMenu.removeAllItems();
                                                DrawerMenu.addItems(db.GetCategoryItems());
                                                DrawerMenu.setSelection(lastSelection);
                                                updateListView(lastSelection);
                                            }
                                        }).show();
                                        else   Snackbar.make(mContent, R.string.item_deleted_error, Snackbar.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                DrawerMenu.removeAllItems();
                                DrawerMenu.addItems(db.GetCategoryItems());
                                DrawerMenu.setSelection(lastSelection);
                                updateListView(lastSelection);

                            }
                        })
                        .show();
            }
        }));

        adapter.notifyDataSetChanged();

        // end of adapter test

        db = new Database(this);

        final IProfile profile = new ProfileDrawerItem().withName("Daniel Walter").withEmail("wirrez@gmail.com").withIcon("https://scontent.fprg1-1.fna.fbcdn.net/v/t31.0-8/21246324_1874906015858663_4452075831135471016_o.jpg?oh=306fa2321ea7a61274e3e8a02ffc4674&oe=5AA9020F");

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withTranslucentStatusBar(false)
                .addProfiles(
                        profile,
                        new ProfileSettingDrawerItem().withName("Add Account").withDescription(R.string.drawer_add_user).withIcon(GoogleMaterial.Icon.gmd_account_add).withIdentifier(1)
                        // new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == 1) {
                            //    IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
                          /*  if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }*/
                        }
                        //false if you have not consumed the event and it should close the drawer
                        return true;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();


        DrawerMenu = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        db.GetCategoryItems()
                )
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.drawer_settings).withIcon(GoogleMaterial.Icon.gmd_settings).withTag("settings"),
                        new SecondaryDrawerItem().withName(R.string.drawer_add_category).withIcon(GoogleMaterial.Icon.gmd_plus).withTag("add_category")).withStickyFooterShadow(false)
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(View view, int position, final IDrawerItem drawerItem) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title(R.string.edit_category)
                                .items(R.array.category_choice)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                        switch (position)
                                        {
                                            case 0: {
                                                final long id = drawerItem.getIdentifier();
                                                MaterialDialog dialogEdit = new EditCategoryActivity(MainActivity.this, new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        TextView name = (TextView) dialog.findViewById(R.id.name);
                                                        if (!name.getText().toString().isEmpty()) {
                                                            boolean ok = db.updateCategory(id,name.getText().toString(),null);
                                                            DrawerMenu.removeAllItems();
                                                            DrawerMenu.addItems(db.GetCategoryItems());
                                                            DrawerMenu.setSelection(lastSelection);
                                                            dialog.dismiss();
                                                        } else {
                                                            Snackbar.make(mContent, R.string.missing_field, Snackbar.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }, new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        dialog.dismiss();
                                                    }
                                                }).autoDismiss(false).build();

                                                EditText name  = (EditText) dialogEdit.getCustomView().findViewById(R.id.name);
                                                name.setText(String.valueOf(drawerItem.getTag()));
                                                dialogEdit.show();
                                                break;
                                            }
                                            case 1: { // TODO
                                                final String name = drawerItem.getTag().toString();
                                                final ArrayList<Item> items = db.getItems(drawerItem.getIdentifier());
                                                long id = db.deleteCategory(drawerItem.getIdentifier());
                                                DrawerMenu.removeAllItems();
                                                DrawerMenu.addItems(db.GetCategoryItems());
                                                if (id != 0) {
                                                    DrawerMenu.setSelection(id);
                                                    lastSelection = id;
                                                }
                                                updateListView(lastSelection);
                                                mDrawer.closeMenu(true);
                                                Snackbar.make(mContent, R.string.category_deleted, Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        Toast.makeText(MainActivity.this,R.string.recovered,Toast.LENGTH_SHORT).show();
                                                        lastSelection = db.InsertCategoryItem(name,null);
                                                        for(Item obj : items)
                                                        {
                                                            db.InsertItem(obj._name,String.valueOf(obj._count),lastSelection,obj._unit);
                                                        }
                                                        DrawerMenu.removeAllItems();
                                                        DrawerMenu.addItems(db.GetCategoryItems());
                                                        DrawerMenu.setSelection(lastSelection);
                                                        updateListView(lastSelection);
                                                    }
                                                }).show();
                                                break;
                                            }
                                        }
                                    }
                                }).show();
                        return true;
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, final IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            if (drawerItem.getTag() == "add_category") {
                                mDrawer.closeMenu();
                                MaterialDialog dialog = new AddCategoryActivity(MainActivity.this, new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        TextView name = (TextView) dialog.findViewById(R.id.name);
                                        if (!name.getText().toString().isEmpty()) {
                                            long id = db.InsertCategoryItem(name.getText().toString(), null);
                                            DrawerMenu.removeAllItems();
                                            DrawerMenu.addItems(db.GetCategoryItems());
                                            if (id != 0) DrawerMenu.setSelection(id);
                                            lastSelection = id;

                                        } else {
                                            Snackbar.make(mContent, R.string.empty_name, Snackbar.LENGTH_SHORT).show();
                                            DrawerMenu.setSelection(lastSelection);
                                        }
                                    }
                                }, new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        DrawerMenu.setSelection(lastSelection);
                                    }
                                }).build();
                                dialog.show();
                            } else {
                                lastSelection = drawerItem.getIdentifier();
                                updateListView(lastSelection);
                                mDrawer.closeMenu(true);
                            }
                        }
                        return true;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .buildView();

        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        fml.addView(DrawerMenu.getSlider());
        //Shake detection
        ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {
            @Override
            public void OnShake()
            {
                    db.cleanCrossed(lastSelection);
                    DrawerMenu.removeAllItems();
                    DrawerMenu.addItems(db.GetCategoryItems());
                    DrawerMenu.setSelection(lastSelection);
                    updateListView(lastSelection);
            }
        });


    }

    private void updateListView(long lastSelection) {
        Database db = new Database(this);
        ArrayList<Item> itm = db.getItems(lastSelection);
        db.close();
        adapter = new CustomItemAdapter(itm);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    protected void setToolBar() {
        ImageView btnToolbar = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));

        btnToolbar.setImageDrawable(drawerArrowDrawable);
        btnToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.toggleMenu();
            }
        });
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                ToolBarOffset = (offsetPixels / 300);
                if (ToolBarOffset >= .3) {
                    ToolBarFlipped = true;
                    drawerArrowDrawable.setFlip(ToolBarFlipped);
                } else {
                    ToolBarFlipped = false;
                    drawerArrowDrawable.setFlip(ToolBarFlipped);
                }
                if (ToolBarOffset > 1) ToolBarOffset = 1;
                drawerArrowDrawable.setParameter(ToolBarOffset);

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        ShakeDetector.start();
    }
    @Override
    protected void onStop() {
        super.onStop();
        ShakeDetector.stop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShakeDetector.destroy();
    }
}
