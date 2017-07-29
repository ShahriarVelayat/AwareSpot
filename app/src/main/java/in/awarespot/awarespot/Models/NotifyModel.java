package in.awarespot.awarespot.Models;

/**
 * Created by sai on 30/7/17.
 */

public class NotifyModel {

    public static String body;
    public static String title;

    public NotifyModel() {
    }

    public static String getBody() {
        return body;
    }

    public static void setBody(String body) {
        NotifyModel.body = body;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        NotifyModel.title = title;
    }
}
