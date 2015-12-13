package in.icho.utils;

import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import in.icho.IchoApplication;
import in.icho.R;

public class Radio {

    public static void fetchThumbailImage(ImageView im, String title, String ext) {
        // change after dummy
        String imageUrl = null;
        try {
            imageUrl = URLStore.S3_URL + URLEncoder.encode(title.trim() + "." + ext.trim(), "UTF-8");
            System.out.println(imageUrl);
            Picasso.with(IchoApplication.applicationContext)
                    .load(imageUrl)
                    .centerCrop()
                    .resize(Utilities.dp(75), Utilities.dp(75))
                    .placeholder(R.mipmap.ic_launcher)
                    .into(im);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void fetchFullImage(ImageView im, String title, String ext) {
        // change after dummy
        String imageUrl = null;
        try {
            imageUrl = URLStore.S3_URL + URLEncoder.encode(title.trim() + "." + ext.trim(), "UTF-8");
            System.out.println(imageUrl);
            Picasso.with(IchoApplication.applicationContext)
                    .load(imageUrl).centerCrop()
                    .resize(im.getMeasuredWidth(), im.getMeasuredHeight())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(im);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static void fetchList(String url, FutureCallback<String> callback) {
        Ion.with(IchoApplication.applicationContext)
                .load(url)
                .asString()
                .setCallback(callback);
    }



	/*
     * Get and post request handler using volley. and all image fetch using
	 * picasso
	 */
}
