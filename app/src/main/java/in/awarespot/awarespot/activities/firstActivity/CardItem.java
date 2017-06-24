package in.awarespot.awarespot.activities.firstActivity;

/**
 * Created by kusha_000 on 24-06-2017.
 */

public class CardItem {
    private int mImageResource;
    private int mTitleResource;

    public CardItem(int title, int image) {
        mTitleResource = title;
        mImageResource = image;
    }

    public int getText() {
        return mImageResource;
    }

    public int getTitle() {
        return mTitleResource;
    }
}

