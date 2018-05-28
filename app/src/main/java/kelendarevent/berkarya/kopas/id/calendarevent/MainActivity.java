package kelendarevent.berkarya.kopas.id.calendarevent;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Instances;
import android.provider.CalendarContract.Events;

public class MainActivity extends AppCompatActivity {
    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] CALENDAR_PROJECTION = new String[] {
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;


    public static final String[] INSTANCE_PROJECTION = new String[] {
            Instances.EVENT_ID,             // 0
            Instances.CALENDAR_ID,          // 1
            Instances.BEGIN,                // 2
            Instances.END,                  // 3
            Instances.TITLE,                // 4
            Instances.DESCRIPTION,          // 5
            Instances.EVENT_LOCATION,       // 6
    };

    private static final int PROJECTION_CALENDAR_ID = 1;
    private static final int PROJECTION_BEGIN_INDEX = 2;
    private static final int PROJECTION_END_INDEX = 3;
    private static final int PROJECTION_TITLE_INDEX = 4;
    private static final int PROJECTION_DESCRIPTION_INDEX = 5;
    private static final int PROJECTION_EVENT_LOCATION_INDEX = 6;


    public static final String[] EVENT_PROJECTION = new String[] {
            Events.CALENDAR_ID,             // 0
            Events.TITLE,                   // 1
            Events.EVENT_LOCATION,          // 2
            Events.DESCRIPTION,             // 3
            Events.DTSTART,                 // 4
            Events.DTEND,                   // 5
    };

    private static final int PROJECTION_EVENT_CALENDAR_ID = 1;
    private static final int PROJECTION_EVENT_TITLE = 2;
    private static final int PROJECTION_EVENT_EVENT_LOCATIONX = 3;
    private static final int PROJECTION_EVENT_DESCRIPTION = 4;
    private static final int PROJECTION_EVENT_DTSTART = 5;
    private static final int PROJECTION_EVENT_DTEND = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            // Run query
            Cursor cur = null;
            ContentResolver cr = getContentResolver();
            Uri uri = Calendars.CONTENT_URI;
            cur = cr.query(uri, CALENDAR_PROJECTION, null, null, null);

            List<String> columns = new ArrayList<String>();
            while (cur.moveToNext()) {

                List<String> columns2 = new ArrayList<String>();

                long calID = 0;
                String displayName = null;
                String accountName = null;
                String ownerName = null;

                //List<String> columns_calendar = new ArrayList<String>();
                // Get the field values
                calID = cur.getLong(PROJECTION_ID_INDEX);
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                // Do something with the values...
                columns2.add(displayName);
                ///columns2.add(accountName);
                //columns2.add(ownerName);



                // Specify the date range you want to search for recurring
                // event instances
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2018, 1, 1, 0, 0);
                long startMillis = beginTime.getTimeInMillis();
                Calendar endTime = Calendar.getInstance();
                endTime.set(2018, 12, 30, 0, 0);
                long endMillis = endTime.getTimeInMillis();

                Cursor cure = null;
                ContentResolver cre = getContentResolver();

                // Construct the query with the desired date range.
                Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
                ContentUris.appendId(builder, startMillis);
                ContentUris.appendId(builder, endMillis);

                String selection = Instances.CALENDAR_ID + " = ?";
                String[] selectionArgs = new String[] {String.valueOf(calID)};
                // Submit the query
                cure =  cre.query(builder.build(),
                        INSTANCE_PROJECTION,
                        selection,
                        selectionArgs,
                        null);
                while (cure.moveToNext()) {
                    String title = null;
                    long eventID = 0;
                    long beginVal = 0;
                    long endVal = 0;
                    long calenderID = 0;

                    List<String> columns_event = new ArrayList<String>();
                    // Get the field values
                    eventID = cure.getLong(PROJECTION_ID_INDEX);
                    beginVal = cure.getLong(PROJECTION_BEGIN_INDEX);
                    endVal = cure.getLong(PROJECTION_BEGIN_INDEX);
                    title = cure.getString(PROJECTION_TITLE_INDEX);
                    calenderID = cure.getLong(PROJECTION_CALENDAR_ID);

                    columns_event.add(title);
                    columns_event.add(String.valueOf(beginVal));
                    columns_event.add(String.valueOf(endVal));

                    columns2.addAll(columns_event);
                }
                columns.addAll(columns2);

            }
            //Log.e("extract",columns.toString());

            Log.e("extractEvent",columns.toString());

        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
