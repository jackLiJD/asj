package com.ald.asjauthlib.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.ald.asjauthlib.auth.model.ContactsModel;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

import java.util.ArrayList;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/3/1 15:51
 * 描述：
 * 修订历史：
 */
public class ContactsUtils {

    /**
     * 获取库表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{Phone.DISPLAY_NAME, Phone.NUMBER, Phone.PHOTO_ID,
            Phone.CONTACT_ID};

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;


    /**
     * 得到手机通讯录联系人信息
     **/
    private static ArrayList<ContactsModel> getPhoneContacts(Context context) {
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContactsModel> contactsModels = new ArrayList<>();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                ContactsModel contactsModel = new ContactsModel();
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (MiscUtils.isEmpty(phoneNumber) || !isNumeric(phoneNumber)) {
                    continue;
                }
                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
//                long contactId = phoneCursor.getLong(phoneCursor.getColumnIndex(Phone.CONTACT_ID));
//                long photoId = phoneCursor.getColumnIndex(Phone.PHOTO_ID);
//
//                if (photoId > 0) {
//                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
//                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
//                    Bitmap contactPhoto = BitmapFactory.decodeStream(input);
//                    contactsModel.setPhoto(contactPhoto);
//
//                }

                contactsModel.setName(contactName);
                contactsModel.setPhoneNumber(phoneNumber);
                contactsModels.add(contactsModel);
            }
            phoneCursor.close();
        }
        return contactsModels;
    }


    /**
     * 获取通讯录(适配锤子手机。。。)
     *
     */
    private static ArrayList<ContactsModel> getContacts(Context context) {
        //联系人的Uri，也就是content://com.android.contacts/contacts
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //指定获取_id和display_name两列数据，display_name即为姓名
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        ArrayList<ContactsModel> contactsModels = new ArrayList<>();
        //根据Uri查询相应的ContentProvider，cursor为获取到的数据集
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        String[] arr = new String[cursor == null ? 0 : cursor.getCount()];
        int i = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ContactsModel contactsModel = new ContactsModel();

                Long id = cursor.getLong(0);
                //获取姓名
                String name = cursor.getString(1);
                //指定获取NUMBER这一列数据
//                String[] phoneProjection = new String[]{
//                        ContactsContract.CommonDataKinds.Phone.NUMBER
//                };
                arr[i] = id + " , 姓名：" + name;

                contactsModel.setName(name);

                //根据联系人的ID获取此人的电话号码
                Cursor phonesCusor = context.getContentResolver().query(
                        Phone.CONTENT_URI,
                        PHONES_PROJECTION,
                        Phone.CONTACT_ID + "=" + id,
                        null,
                        null);

//                long photoId = phonesCusor == null ? -1 : phonesCusor.getColumnIndex(Phone.PHOTO_ID);
//
//                if (photoId > 0) {
//                    uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
//                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri);
//                    Bitmap contactPhoto = BitmapFactory.decodeStream(input);
//                    contactsModel.setPhoto(contactPhoto);
//
//                }

                //因为每个联系人可能有多个电话号码，所以需要遍历
                String num = "";
                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                    do {
                        num += phonesCusor.getString(1)+",";
                        arr[i] += " , 电话号码：" + num;
                        contactsModel.setPhoneNumber(num);
                    } while (phonesCusor.moveToNext());
                    phonesCusor.close();
                }
                contactsModels.add(contactsModel);
                i++;
            } while (cursor.moveToNext());
            cursor.close();
        }

        return contactsModels;
    }

    private static boolean isNumeric(String s) {
        if (MiscUtils.isEmpty(s)) {
            return false;
        }
        String newString = s.trim().replace(" ", "").replace("-", "").replace("+", "");
        return MiscUtils.isNotEmpty(newString) && newString.matches("^[0-9]*$");
    }


    private static ArrayList<ContactsModel> getSimContactAll(Context mContext, String adn) {
        // 读取SIM卡手机号,有两种可能:content://icc/adn与content://sim/adn
        Cursor phoneCursor = null;
        ArrayList<ContactsModel> models = new ArrayList<ContactsModel>();
        try {
            Uri uri = Uri.parse(adn);
            phoneCursor = mContext.getContentResolver().query(uri, null,
                    null, null, null);
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    String phoneNumber = "";
                    for (String column : phoneCursor.getColumnNames()) {
                        if (column.toLowerCase().contains("number")) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(column));
                            break;
                        }
                    }
                    if (MiscUtils.isEmpty(phoneNumber) || !isNumeric(phoneNumber)) {
                        //未查找到号码
                        continue;
                    }
                    //有号码，找出name
                    String contactName = "";
                    for (String column : phoneCursor.getColumnNames()) {
                        if (column.toLowerCase().contains("name")) {
                            contactName = phoneCursor.getString(phoneCursor.getColumnIndex(column));
                            break;
                        }
                    }
                    //姓名为空依然提交
                    // Sim卡中没有联系人头像
                    ContactsModel contactsModel = new ContactsModel();
                    contactsModel.setName(contactName);
                    contactsModel.setPhoneNumber(phoneNumber);
                    models.add(contactsModel);
                }
                phoneCursor.close();
            }
        } catch (Exception e) {
            if (phoneCursor != null)
                phoneCursor.close();
        }
        return models;
    }

    //simId,卡槽位置
    public static int getSubId(Context context, int simId) {
        Uri uri = Uri.parse("content://telephony/siminfo");
        Cursor cursor = null;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            cursor = contentResolver.query(uri, new String[]{"_id", "sim_id"}, "sim_id = ?",
                    new String[]{String.valueOf(simId)}, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(cursor.getColumnIndex("_id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return -1;
    }


    /**
     * 获取所有的联系人
     */
    public static String getPostContacts(Context context) {
        //如果本地存有通讯录就取本地通讯录
        ArrayList<ContactsModel> contactsModels = getPhoneContacts(context);
        ArrayList<ContactsModel> contacts = getContacts(context);
        ArrayList<ContactsModel> simType1 = getSimContactAll(context, "content://icc/adn");
        ArrayList<ContactsModel> simType2 = getSimContactAll(context, "content://sim/adn");
        //卡槽1
        ArrayList<ContactsModel> simType1_1 = getSimContactAll(context, "content://icc/adn/subId/" + getSubId(context, 0));
        //卡槽2
        ArrayList<ContactsModel> simType1_2 = getSimContactAll(context, "content://icc/adn/subId/" + getSubId(context, 1));
        contactsModels.addAll(contacts);
        contactsModels.addAll(simType1);
        contactsModels.addAll(simType2);
        contactsModels.addAll(simType1_1);
        contactsModels.addAll(simType1_2);
        StringBuilder stringBuilder = new StringBuilder();
        for (ContactsModel model : contactsModels) {
            stringBuilder.append(model.toString());
        }
        if (MiscUtils.isEmpty(stringBuilder.toString())) {
            return "";
        }
        //存储通讯录
        return stringBuilder.toString();
    }

    /**
     * 获取所有的联系人列表
     */
    public static ArrayList<ContactsModel> getPostContactsList(Context context) {
        //如果本地存有通讯录就取本地通讯录
//        ArrayList<ContactsModel> contactsModels = getPhoneContacts(context);
        ArrayList<ContactsModel> contactsModels = new ArrayList<>();
        ArrayList<ContactsModel> simType1 = getSimContactAll(context, "content://icc/adn");
        ArrayList<ContactsModel> simType2 = getSimContactAll(context, "content://sim/adn");
        //卡槽1
        ArrayList<ContactsModel> simType1_1 = getSimContactAll(context, "content://icc/adn/subId/" + getSubId(context, 0));
        //卡槽2
        ArrayList<ContactsModel> simType1_2 = getSimContactAll(context, "content://icc/adn/subId/" + getSubId(context, 1));

        ArrayList<ContactsModel> contacts = getContacts(context);
        contactsModels.addAll(contacts);
        contactsModels.addAll(simType1);
        contactsModels.addAll(simType2);
        contactsModels.addAll(simType1_1);
        contactsModels.addAll(simType1_2);

        return contactsModels;
    }
}
