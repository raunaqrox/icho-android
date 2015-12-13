package in.icho.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashSet;
import java.util.Set;

import in.icho.IchoApplication;
import in.icho.data.Item;

/**
 * Created by abs on 13/12/15.
 */
public class Store {

    private static final String ICHO_SP = "ICHO_SP";
    public static final String KEY_ICHO_CURRENT_TAG = "currentTag";

    private SharedPreferences sp;


    public Store() {
        checkFiles();
    }

    private void checkFiles() {

    }

    public SharedPreferences getSP() {
        sp = IchoApplication.applicationContext.getSharedPreferences(ICHO_SP, Context.MODE_PRIVATE);
        return sp;
    }

    public void saveItem(Item i) {
        if (readObject(i.get_id()) == null) {
            writeObject(i.get_id(), i);
        }
    }

    private void writeObject(String name, Object obj) {
        try {
            FileOutputStream fos = IchoApplication.applicationContext.openFileOutput(name.trim(), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object readObject(String name) {

        Object obj = null;
        FileInputStream fis;
        ObjectInputStream ois;
        try {
            fis = IchoApplication.applicationContext.openFileInput(name.trim());
            ois = new ObjectInputStream(fis);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
