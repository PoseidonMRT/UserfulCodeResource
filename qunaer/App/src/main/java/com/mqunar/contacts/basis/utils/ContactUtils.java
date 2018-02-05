package com.mqunar.contacts.basis.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.contacts.basis.py.ChineseToPinyinResource;
import com.mqunar.tools.log.QLog;
import java.util.ArrayList;
import java.util.List;

public class ContactUtils {

    public interface LegalInvoke {
        boolean formatNumber(String str);

        boolean isLegalName(String str);

        boolean isLegalNumber(String str);
    }

    public static List<Contact> getContacts(Context context) {
        List<Contact> list = null;
        Cursor query = context.getContentResolver().query(Phone.CONTENT_URI, new String[]{"display_name", "data1"}, null, null, null);
        if (query == null) {
            QLog.e("Contacts", "cursor is empty!", new Object[0]);
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            list = new ArrayList();
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndex("data1"));
                String string2 = query.getString(query.getColumnIndex("display_name"));
                String formatNumber = formatNumber(string);
                if (!isLegalNumber(formatNumber)) {
                    QLog.d("Contacts", " BAD PHONE NUMBER : >>> " + string + " >> NAME : " + string2, new Object[0]);
                } else if (isLegalName(string2)) {
                    Contact contact = new Contact();
                    contact.setPhone(formatNumber);
                    contact.setName(string2);
                    contact.setPrefix(ChineseToPinyinResource.toPrefix(context, contact.getName()));
                    list.add(contact);
                } else {
                    QLog.d("Contacts", " BAD PHONE NAME : >>> " + string + " >> NAME : " + string2, new Object[0]);
                }
            }
            QLog.e("Contacts", "read contacts waste " + (System.currentTimeMillis() - currentTimeMillis) + "ms", new Object[0]);
        }
        return list;
    }

    public static String getPrefix(Context context, Contact contact) {
        if (contact == null) {
            return null;
        }
        return getPrefix(context, contact.getName());
    }

    public static String getPrefix(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return ChineseToPinyinResource.toPrefix(context, str);
    }

    public static String formatNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String replaceAll = str.replaceAll("\\+86", "").replaceAll("\\-", "").replaceAll(" ", "");
        if (replaceAll.startsWith("86")) {
            return replaceAll.replaceFirst("86", "");
        }
        return replaceAll;
    }

    public static boolean isLegalNumber(String str) {
        if (!TextUtils.isEmpty(str) && str.matches("[0-9]*") && str.length() == 11 && str.startsWith("1")) {
            return true;
        }
        return false;
    }

    public static boolean isLegalName(String str) {
        return (TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim())) ? false : true;
    }
}
