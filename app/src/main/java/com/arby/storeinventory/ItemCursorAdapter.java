package com.arby.storeinventory;

/**
 * Created by rares on 25.08.2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arby.storeinventory.data.ItemContract.ItemEntry;

import static com.arby.storeinventory.R.id.quantity;


/**
 * ItemCursorAdapter is an adapter for a list or grid view
 * that uses a Cursor of item data as its data source. 
 */
public class ItemCursorAdapter extends CursorAdapter {

    // Instance of the CatalogActivity used in the onClickListeners down below
    private CatalogActivity catalogActivity = new CatalogActivity();

    /**
     * Constructs a new {@link ItemCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data
     */
    public ItemCursorAdapter(CatalogActivity context, Cursor c) {
        super(context, c, 0 /* flags */);
        this.catalogActivity = context;
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Binds the item data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current item can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView nameView = (TextView) view.findViewById(R.id.name);
        TextView quantityView = (TextView) view.findViewById(quantity);
        TextView priceView = (TextView) view.findViewById(R.id.price);
        Button buttonView = (Button) view.findViewById(R.id.button);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.details);

        // Extract properties from cursor
        final long id = cursor.getLong(cursor.getColumnIndex(ItemEntry._ID));
        String name = cursor.getString(cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME));
        int quantity = cursor.getInt(cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY));
        String price = cursor.getString(cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE));

        // Populate fields with extracted properties
        nameView.setText(name);
        quantityView.setText(String.valueOf(quantity));
        priceView.setText(price);

        // Needed for the SALE button method, as it has to be final
        final int MQUANTITY = quantity;

        // SALE button click
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MQUANTITY > 0) {
                    catalogActivity.buttonClick(id, MQUANTITY);
                } else {
                    Toast.makeText(catalogActivity, "Quantity must be a positive number", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Redirect the user to the EditActivity by clicking the list item details
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogActivity.listItemClick(id);
            }
        });
    }
}
