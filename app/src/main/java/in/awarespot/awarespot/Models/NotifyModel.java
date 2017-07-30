package in.awarespot.awarespot.Models;

/**
 * Created by sai on 30/7/17.
 */

public class NotifyModel {

    public  String body;
    public  String title;
    public long timeStamp;


    public NotifyModel(String body, String title, long timeStamp) {
        this.body = body;
        this.title = title;
        this.timeStamp = timeStamp;
    }

    public NotifyModel() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
