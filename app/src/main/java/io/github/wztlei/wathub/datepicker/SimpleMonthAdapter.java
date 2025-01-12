/*
 * Copyright 2013 Christian De Angelis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.wztlei.wathub.datepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

import java.util.HashMap;


/**
 * An adapter for a list of {@link SimpleMonthView} items.
 */
public class SimpleMonthAdapter extends BaseAdapter implements SimpleMonthView.OnDayClickListener {

    protected static final int MONTHS_IN_YEAR = 12;
    private static final String TAG = "WL/SimpleMonthAdapter";
    protected static int WEEK_7_OVERHANG_HEIGHT = 7;
    private final Context mContext;
    private final DatePickerController mController;
    private CalendarDay mSelectedDay;

    SimpleMonthAdapter(
            Context context,
            DatePickerController controller) {
        mContext = context;
        mController = controller;
        init();
        setSelectedDay(mController.getSelectedDay());
    }

    public CalendarDay getSelectedDay() {
        return mSelectedDay;
    }

    /**
     * Updates the selected day and related parameters.
     *
     * @param day The day to highlight
     */
    public void setSelectedDay(CalendarDay day) {
        mSelectedDay = day;
        notifyDataSetChanged();
    }

    /**
     * Set up the gesture detector and selected time
     */
    protected void init() {
        mSelectedDay = new CalendarDay(System.currentTimeMillis());
    }

    @Override
    public int getCount() {
        return ((mController.getMaxYear() - mController.getMinYear()) + 1) * MONTHS_IN_YEAR;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    @Override
    public View getView(
            int position,
            View convertView,
            ViewGroup parent) {
        SimpleMonthView v;
        HashMap<String, Integer> drawingParams = null;
        if (convertView != null) {
            v = (SimpleMonthView) convertView;
            // We store the drawing parameters in the view so it can be recycled
            drawingParams = (HashMap<String, Integer>) v.getTag();
        } else {
            v = new SimpleMonthView(mContext);
            // Set up the new view
            LayoutParams params = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            v.setLayoutParams(params);
            v.setClickable(true);
            v.setOnDayClickListener(this);
        }
        if (drawingParams == null) {
            drawingParams = new HashMap<>();
        }
        drawingParams.clear();

        final int month = position % MONTHS_IN_YEAR;
        final int year = position / MONTHS_IN_YEAR + mController.getMinYear();
        Log.d(TAG, "Year: " + year + ", Month: " + month);

        int selectedDay = -1;
        if (isSelectedDayInMonth(year, month)) {
            selectedDay = mSelectedDay.day;
        }

        // Invokes requestLayout() to ensure that the recycled view is set with the appropriate
        // height/number of weeks before being displayed.
        v.reuse();

        if (mController.isHighlightWeeksEnabled()) {
            drawingParams.put(SimpleMonthView.VIEW_PARAMS_HIGHLIGHT_WEEK, 1);
        }
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_DAY, selectedDay);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_WEEK_START, mController.getFirstDayOfWeek());
        v.setMonthParams(drawingParams);
        v.invalidate();
        return v;
    }

    private boolean isSelectedDayInMonth(
            int year,
            int month) {
        return mSelectedDay.year == year && mSelectedDay.month == month;
    }

    @Override
    public void onDayClick(
            SimpleMonthView view,
            CalendarDay day) {
        if (day != null) {
            onDayTapped(day);
        }
    }

    /**
     * Maintains the same hour/min/sec but moves the day to the tapped day.
     *
     * @param day The day that was tapped
     */
    protected void onDayTapped(CalendarDay day) {
        mController.tryVibrate();
        mController.onDayOfMonthSelected(day.year, day.month, day.day);
        setSelectedDay(day);
    }

}
