package com.arby.storeinventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.arby.storeinventory.data.ItemContract.ItemEntry;

/**
 * Display list of items that were entered and stored in the app
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ItemCursorAdapter cursorAdapter;
    private static final int STORE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        // Find the ListView which will be populated with the item data
        ListView itemListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items
        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);
        // Setup cursor adapter using cursor from last step
        cursorAdapter = new ItemCursorAdapter(this, null);
        // Attach cursor adapter to the ListView
        itemListView.setAdapter(cursorAdapter);

        // Prepare the loader. Either re-connect with an existing one,
        // or start a new one
        getLoaderManager().initLoader(STORE_LOADER_ID, null, this);
    }

    /**
     * Helper method to insert hardcoded item data into the database. For debugging purposes only
     */
    private void insertItem() {
        // Create a ContentValues object where column names are the keys,
        // and example item attributes are the values
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME, "Example product");
        values.put(ItemEntry.COLUMN_ITEM_IMAGE, "android.resource://com.arby.storeinventory/drawable/ic_example");
        values.put(ItemEntry.COLUMN_ITEM_PRICE, "6.99");
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, 0);
        values.put(ItemEntry.COLUMN_ITEM_SUPPLIER, "+40 727 543 212");

        // Insert a new row for example into the provider using the ContentResolver
        // Receive the new content URI that will allow us to access data in the future
        Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertItem();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method to delete all items in the database
     */
    private void deleteAllItems() {
        int rowsDeleted = getContentResolver().delete(ItemEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from store database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_PRICE
        };
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed
        return new CursorLoader(this, ItemEntry.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Called when the last Cursor provided to onLoadFinished()
        // above is about to be closed. Need to make sure we are not using it
        cursorAdapter.swapCursor(null);
    }

    public void listItemClick(long id) {
        // This is called when the user selects a list item, directing them to the editor
        Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
        Uri itemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
        intent.setData(itemUri);
        startActivity(intent);
    }

    public void buttonClick(long id, int quantity) {
        // Method called for the SALE button, which reduces quantity by 1
        Uri itemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
        quantity--;
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantity);
        int rowsAffected = getContentResolver().update(itemUri, values, null, null);
    }
}
