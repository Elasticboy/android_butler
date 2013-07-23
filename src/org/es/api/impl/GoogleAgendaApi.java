package org.es.api.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.text.format.Time;

import org.es.api.AgendaApi;
import org.es.api.pojo.Agenda;
import org.es.api.pojo.AgendaEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cyril Leroux on 18/06/13.
 */
public class GoogleAgendaApi implements AgendaApi {

    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] CALENDAR_PROJECTION = new String[]{
            Calendars._ID,
            Calendars.CALENDAR_DISPLAY_NAME,
            Calendars.ACCOUNT_NAME,
            Calendars.OWNER_ACCOUNT
    };
    // The indices for the projection array above.
    private static final int CALENDAR_ID_INDEX = 0;
    private static final int CALENDAR_DISPLAY_NAME_INDEX = 1;
    private static final int CALENDAR_ACCOUNT_NAME_INDEX = 2;
    private static final int CALENDAR_OWNER_ACCOUNT_INDEX = 3;

    public static final String[] EVENT_PROJECTION = new String[]{
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
    private static final int EVENT_CALENDAR_ID_INDEX = 0;
    private static final int EVENT_CALENDAR_NAME_INDEX = 1;
    private static final int EVENT_TITLE_INDEX = 2;
    private static final int EVENT_DESCRIPTION_INDEX = 3;
    private static final int EVENT_START_DATE_INDEX = 4;
    private static final int EVENT_END_DATE_INDEX = 5;
    private static final int EVENT_LOCATION_INDEX = 6;
    private static final int EVENT_DURATION_INDEX = 7;
    private static final int EVENT_ALL_DAY_INDEX = 8;


    @Override
    public List<AgendaEvent> checkTodayEvents(Context context, List<String> restrictionList) {

        Time dayStart = new Time();
        dayStart.setToNow();
        dayStart.set(0, 0, 0, dayStart.monthDay, dayStart.month, dayStart.year);

        long startMillis = dayStart.toMillis(false) - 100_000l;
        long endMillis = dayStart.toMillis(false) + 86400_000l + 10_000l;

        return readCalendarEvent(context, startMillis, endMillis, restrictionList);

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
    public List<AgendaEvent> checkUpcomingEvent(Context context, List<String> restrictionList) {

        Time dayStart = new Time();
        dayStart.setToNow();
        dayStart.set(59, 59, 23, dayStart.monthDay, dayStart.month, dayStart.year);

        long startMillis = dayStart.toMillis(false);
        long endMillis = dayStart.toMillis(false) + 86400_000l * 7l + 10_000l;

        return readCalendarEvent(context, startMillis, endMillis, restrictionList);
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

    private List<AgendaEvent> readCalendarEvent(Context context, long startDate, long endDate, List<String> restrictionList) {

        Uri eventsUri = Events.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        StringBuilder eventsSelection = new StringBuilder();
        eventsSelection.append("(");
        eventsSelection.append("(" + Events.DTSTART + " >= ?)");
        eventsSelection.append(" AND ");
        eventsSelection.append("(" + Events.DTEND + " <= ?)");
        if (restrictionList != null && !restrictionList.isEmpty()) {
            eventsSelection.append(" AND ");
            eventsSelection.append("(" + Events.CALENDAR_DISPLAY_NAME + " IN (?))");
        }
        eventsSelection.append(")");

        StringBuilder restrictionCondition = new StringBuilder();
        for (String restriction : restrictionList) {
            restrictionCondition.append(restriction).append(",");
        }

        String[] eventsSelectionArgs;
        if (restrictionList != null && !restrictionList.isEmpty()) {
            eventsSelectionArgs = new String[]{String.valueOf(startDate), String.valueOf(endDate), restrictionCondition.toString()};
        } else {
            eventsSelectionArgs = new String[]{String.valueOf(startDate), String.valueOf(endDate)};
        }

        Cursor cursor = resolver.query(eventsUri, EVENT_PROJECTION, eventsSelection.toString(), eventsSelectionArgs, Events.DTSTART);

        List<AgendaEvent> events = new ArrayList<>();
        AgendaEvent event;

        while (cursor.moveToNext()) {
            event = new AgendaEvent();

            event.setCalendarId(cursor.getLong(EVENT_CALENDAR_ID_INDEX));
            event.setCalendarName(cursor.getString(EVENT_CALENDAR_NAME_INDEX));
            event.setTitle(cursor.getString(EVENT_TITLE_INDEX));
            event.setDescription(cursor.getString(EVENT_DESCRIPTION_INDEX));
            event.setStartDateMillis(cursor.getLong(EVENT_START_DATE_INDEX));
            event.setEndDateMillis(cursor.getLong(EVENT_END_DATE_INDEX));
            event.setLocation(cursor.getString(EVENT_LOCATION_INDEX));
            event.setDurationMillis(cursor.getLong(EVENT_DURATION_INDEX));
            event.setAllDay(toBool(cursor.getInt(EVENT_ALL_DAY_INDEX)));

            events.add(event);
        }
        return events;
    }

    private boolean toBool(int value) {
        return value == 1;
    }

    public List<Agenda> getAgendas(Context context) {

        Uri calendarUri = Calendars.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        // Submit the query and get a Cursor object back.
        Cursor calendarCursor = resolver.query(calendarUri, CALENDAR_PROJECTION, null, null, null);

        List<Agenda> agendaList = new ArrayList<>();
        // Use the cursor to step through the returned records
        while (calendarCursor.moveToNext()) {
            long id = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            id          = calendarCursor.getLong(CALENDAR_ID_INDEX);
            displayName = calendarCursor.getString(CALENDAR_DISPLAY_NAME_INDEX);
            accountName = calendarCursor.getString(CALENDAR_ACCOUNT_NAME_INDEX);
            ownerName   = calendarCursor.getString(CALENDAR_OWNER_ACCOUNT_INDEX);

            agendaList.add(new Agenda(id, displayName, accountName, ownerName));
        }
        return agendaList;
    }
}
