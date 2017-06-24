package in.awarespot.awarespot.activities.firstActivity;

import android.support.v7.widget.CardView;

/**
 * Created by kusha_000 on 24-06-2017.
 */

public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();

}
