package in.awarespot.awarespot.activities.firstActivity;

/**
 * Created by kusha_000 on 24-06-2017.
 */

public class CardItem {
    private int mTextResource;
    private int mTitleResource;

    public CardItem(int title, int text) {
        mTitleResource = title;
        mTextResource = text;
    }

    public int getText() {
        return mTextResource;
    }

    public int getTitle() {
        return mTitleResource;
    }
}

