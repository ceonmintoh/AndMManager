/*
 * Copyright (C) 2012-2015 The Android Money Manager Ex Project Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.money.manager.ex.businessobjects;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.money.manager.ex.Constants;
import com.money.manager.ex.database.TablePayee;

/**
 *
 */
public class PayeeService {

    public PayeeService(Context context) {
        mContext = context;
        mPayee = new TablePayee();
    }

    private Context mContext;
    private TablePayee mPayee;

    public TablePayee loadByName(String name) {
        String selection = TablePayee.PAYEENAME + "='" + name + "'";

        Cursor cursor = mContext.getContentResolver().query(
                mPayee.getUri(),
                mPayee.getAllColumns(),
                selection,
                null,
                null);

        if(cursor.moveToFirst()) {
            mPayee.setValueFromCursor(cursor);
        }

        cursor.close();

        return mPayee;
    }

    public int loadIdByName(String name) {
        int result = -1;

        if(TextUtils.isEmpty(name)) return result;

        String selection = TablePayee.PAYEENAME + "=?";

        Cursor cursor = mContext.getContentResolver().query(
                mPayee.getUri(),
                new String[]{TablePayee.PAYEEID},
                selection,
                new String[] { name },
                null);

        if(cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getColumnIndex(TablePayee.PAYEEID));
        }

        cursor.close();

        return result;
    }

    public int createNew(String name) {
        if (TextUtils.isEmpty(name)) return Constants.NOT_SET;

        name = name.trim();

        ContentValues values = new ContentValues();
        values.put(TablePayee.PAYEENAME, name);

        Uri result = mContext.getContentResolver().insert(mPayee.getUri(), values);
        long id = ContentUris.parseId(result);

        return ((int) id);
    }

    public boolean exists(String name) {
        name = name.trim();

        TablePayee payee = loadByName(name);
        return (payee != null);
    }

    public int update(int id, String name) {
        if(TextUtils.isEmpty(name)) return Constants.NOT_SET;

        name = name.trim();

        ContentValues values = new ContentValues();
        values.put(TablePayee.PAYEENAME, name);

        int result = mContext.getContentResolver().update(mPayee.getUri(),
                values,
                TablePayee.PAYEEID + "=" + id, null);

        return result;
    }
}
