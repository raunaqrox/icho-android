package in.icho.utils;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import in.icho.IchoApplication;
import in.icho.R;

public class Radio {

	public static void fetchImage(ImageView im, String imageUrl,
			boolean featured, boolean cache) {
		// change after dummy

		InputStream ims;
		try {
			if (featured) {
				ims = IchoApplication.applicationContext.getAssets().open(
						"test/" + imageUrl);
				Bitmap b = BitmapFactory.decodeStream(ims);
				if (im != null)
					im.setImageBitmap(b);
			} else {
				Picasso.with(IchoApplication.applicationContext)
						.load(imageUrl).placeholder(R.drawable.blank_image)
						.fit().into(im);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Get and post request handler using volley. and all image fetch using
	 * picasso
	 */
}
