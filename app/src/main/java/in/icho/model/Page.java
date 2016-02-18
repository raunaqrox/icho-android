package in.icho.model;

/**
 * Created by sahebjot on 2/14/16.
 */
public class Page {
    int perPage = 1;
    int page = 1;
    String type = "P";


    public Page(int perPage, int page, String type) {
        this.perPage = perPage;
        this.page = page;
        this.type = type;
    }
}
