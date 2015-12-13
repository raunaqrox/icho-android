package in.icho.data;

import java.io.Serializable;

/**
 * Created by abs on 13/12/15.
 */
public class Item implements Serializable {
    String _id, title, image_extension, uploader;

    @Override
    public String toString() {
        return "Item{" +
                "_id='" + _id + '\'' +
                ", title='" + title + '\'' +
                ", image_extension='" + image_extension + '\'' +
                ", uploader='" + uploader + '\'' +
                '}';
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_extension() {
        return image_extension;
    }

    public String getUploader() {
        return uploader;
    }

}
