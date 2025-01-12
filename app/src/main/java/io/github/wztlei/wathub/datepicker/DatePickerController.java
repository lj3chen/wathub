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

/**
 * Controller class to communicate among the various components of the date picker dialog.
 */
interface DatePickerController {

    void onYearSelected(int year);

    void onDayOfMonthSelected(
            int year,
            int month,
            int day);

    void registerOnDateChangedListener(DatePickerDialog.OnDateChangedListener listener);

    void unregisterOnDateChangedListener(DatePickerDialog.OnDateChangedListener listener);

    CalendarDay getSelectedDay();

    int getFirstDayOfWeek();

    int getMinYear();

    int getMaxYear();

    void tryVibrate();

    boolean isHighlightWeeksEnabled();
}
