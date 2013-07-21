package org.es.api.pojo;

/**
 * Created by Cyril Leroux on 18/06/13.
 */
public class AgendaEvent {

    private long mCalendarId;
    private String mCalendarName;
    private String mTitle;
    private String mDescription;
    private long mStartDateMillis;
    private long mEndDateMillis;
    private String mLocation;
    private long mDurationMillis;
    private boolean mAllDay;

    public AgendaEvent() { }

    public boolean isAllDay() {
        return mAllDay;
    }

    public void setAllDay(boolean allDay) {
        this.mAllDay = allDay;
    }

    public long getDurationMillis() {
        return mDurationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.mDurationMillis = durationMillis;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public long getEndDateMillis() {
        return mEndDateMillis;
    }

    public void setEndDateMillis(long endDateMillis) {
        this.mEndDateMillis = endDateMillis;
    }

    public long getStartDateMillis() {
        return mStartDateMillis;
    }

    public void setStartDateMillis(long startDateMillis) {
        this.mStartDateMillis = startDateMillis;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public long getCalendarId() {
        return mCalendarId;
    }

    public void setCalendarId(long calendarId) {
        this.mCalendarId = calendarId;
    }

    public String getCalendarName() {
        return mCalendarName;
    }

    public void setCalendarName(String calendarName) {
        this.mCalendarName = calendarName;
    }
}
