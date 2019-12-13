package skmess.com.rangedatepicker;

import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TextView;
import skmess.com.R;

public class DefaultDayViewAdapter implements DayViewAdapter {
    @Override
    public void makeCellView(CalendarCellView parent) {

        TextView subTitleTextView = new TextView(
            new ContextThemeWrapper(parent.getContext(), R.style.CalendarTitle));
        subTitleTextView.setDuplicateParentStateEnabled(true);
        subTitleTextView.setGravity(Gravity.BOTTOM);
        parent.addView(subTitleTextView);

        TextView textView = new TextView(
            new ContextThemeWrapper(parent.getContext(), R.style.CalendarCell_CalendarDate));
        textView.setDuplicateParentStateEnabled(true);
        parent.addView(textView);

        parent.setDayOfMonthTextView(textView);
        parent.setSubTitleTextView(subTitleTextView);
    }
}
