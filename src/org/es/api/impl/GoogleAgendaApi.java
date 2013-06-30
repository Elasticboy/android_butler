package org.es.api.impl;

import android.accounts.AccountManager;
import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.text.format.Time;
import android.util.Patterns;

import org.es.api.AgendaApi;
import org.es.butler.pojo.AgendaEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Cyril Leroux on 18/06/13.
 */
public class GoogleAgendaApi implements AgendaApi {

    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] CALENDAR_PROJECTION = new String[] {
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT                  // 3
    };
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX            = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX  = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX  = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    public static final String[] EVENT_PROJECTION = new String[] {
            Events.CALENDAR_ID,
            Events.CALENDAR_DISPLAY_NAME,
            Events.TITLE,
            Events.DESCRIPTION,
            Events.DTSTART,
            Events.DTEND,
            Events.EVENT_LOCATION,
            Events.DURATION,
            Events.ALL_DAY
    };
    // The indices for the projection array above.
    private static final int PROJECTION_CALENDAR_ID_INDEX   = 0;
    private static final int PROJECTION_CALENDAR_NAME_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX         = 2;
    private static final int PROJECTION_DESCRIPTION_INDEX   = 3;
    private static final int PROJECTION_START_DATE_INDEX    = 4;
    private static final int PROJECTION_END_DATE_INDEX      = 5;
    private static final int PROJECTION_LOCATION_INDEX      = 6;
    private static final int PROJECTION_DURATION_INDEX      = 7;
    private static final int PROJECTION_ALL_DAY_INDEX       = 8;


    @Override
    public List<AgendaEvent> checkTodayEvents(Context context) {

        Time dayStart = new Time();
        dayStart.setToNow();
        dayStart.set(0, 0, 0, dayStart.monthDay, dayStart.month, dayStart.year);

        long startMillis    = dayStart.toMillis(false) - 100_000l;
        long endMillis      = dayStart.toMillis(false) + 86400_000l + 10_000l;

        return readCalendarEvent(context, startMillis, endMillis);

//        // Run query
//        Uri uri = Calendars.CONTENT_URI;
//        String calendarSelection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
//                + Calendars.ACCOUNT_TYPE + " = ?) AND ("
//                + Calendars.OWNER_ACCOUNT + " = ?))";
//        String[] calendarSelectionArgs = new String[]{email, "com.google", email};
//
//        ContentResolver resolver = context.getContentResolver();
//        // Submit the query and get a Cursor object back.
//        Cursor calendarCursor = resolver.query(uri, CALENDAR_PROJECTION, calendarSelection, calendarSelectionArgs, null);
//
//        // Use the cursor to step through the returned records
//        while (calendarCursor.moveToNext()) {
//            long calID = 0;
//            String displayName = null;
//            String accountName = null;
//            String ownerName = null;
//
//            // Get the field values
//            calID       = calendarCursor.getLong(PROJECTION_ID_INDEX);
//            displayName = calendarCursor.getString(PROJECTION_DISPLAY_NAME_INDEX);
//            accountName = calendarCursor.getString(PROJECTION_ACCOUNT_NAME_INDEX);
//            ownerName   = calendarCursor.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
//
//            getEvents()
//        }
    }

    @Override
    public List<AgendaEvent> checkUpcomingEvent(Context context) {

        Time dayStart = new Time();
        dayStart.setToNow();
        dayStart.set(59, 59, 23, dayStart.monthDay, dayStart.month, dayStart.year);

        long startMillis    = dayStart.toMillis(false);
        long endMillis      = dayStart.toMillis(false) + 86400_000l *7l + 10_000l;

        return readCalendarEvent(context, startMillis, endMillis);
    }

//    private String getOwnerAccount(Context context) {
//        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
//        Account[] accounts = AccountManager.get(context).getAccounts();
//        for (Account account : accounts) {
//            if (emailPattern.matcher(account.name).matches()) {
//                return account.name;
//            }
//        }
//
//        return null;
//    }

    public List<AgendaEvent> readCalendarEvent(Context context, long startDate, long endDate) {

        Uri eventsUri = Events.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        String eventsSelection = "(("
                + Events.DTSTART + " >= ?) AND ("
                + Events.DTEND + " <= ?))";
        String[] eventsSelectionArgs = new String[]{String.valueOf(startDate), String.valueOf(endDate)};

        Cursor cursor = resolver.query(eventsUri, EVENT_PROJECTION, eventsSelection, eventsSelectionArgs, Events.DTSTART);

        List<AgendaEvent> events = new ArrayList<>();
        AgendaEvent event;

        while (cursor.moveToNext()) {
            event = new AgendaEvent();

            event.setCalendarId(cursor.getLong(PROJECTION_CALENDAR_ID_INDEX));
            event.setCalendarName(cursor.getString(PROJECTION_CALENDAR_NAME_INDEX));
            event.setTitle(cursor.getString(PROJECTION_TITLE_INDEX));
            event.setDescription(cursor.getString(PROJECTION_DESCRIPTION_INDEX));
            event.setStartDateMillis(cursor.getLong(PROJECTION_START_DATE_INDEX));
            event.setEndDateMillis(cursor.getLong(PROJECTION_END_DATE_INDEX));
            event.setLocation(cursor.getString(PROJECTION_LOCATION_INDEX));
            event.setDurationMillis(cursor.getLong(PROJECTION_DURATION_INDEX));
            event.setAllDay(toBool(cursor.getInt(PROJECTION_ALL_DAY_INDEX)));

            events.add(event);
        }
        return events;
    }

    private boolean toBool(int value) {
        return value == 1;
    }
}
