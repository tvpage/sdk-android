package com.tvpage.lib.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tvpage.lib.R;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by MTPC on 9/29/2016.
 */

public class CommonUtils {
    public static final String TAG = CommonUtils.class.getSimpleName();

    public static String VIDEO_GALLERY_SOURCE_KEY = "VIDEO_GALLERY_SOURCE_KEY";
    public static String VIDEO_GALLERY_SOURCE_VALUE = "VIDEO_GALLERY_SOURCE_VALUE";

    public static final String PARCABLE_VIDEO_MODEL_KEY = "PARCABLE_VIDEO_MODEL_KEY";

    public static final int NUMBER_OF_RESULT_TO_RETURN = 4;

    public static final String VIDEO_DETAIL_FROM_CHANNEL_LIST_KEY = "VIDEO_DETAIL_FROM_CHANNEL_LIST_KEY";
    public static final String VIDEO_DETAIL_FROM_CHANNEL_LIST_VALUE = "VIDEO_DETAIL_FROM_CHANNEL_LIST_VALUE";

    public static final String VIDEO_DETAIL_FROM_CHANNEL_DETAIL_KEY = "VIDEO_DETAIL_FROM_CHANNEL_DETAIL_KEY";
    public static final String VIDEO_DETAIL_FROM_CHANNEL_DETAIL_VALUE = "VIDEO_DETAIL_FROM_CHANNEL_DETAIL_VALUE";

    public static final String VIDEO_DETAIL_OPEN_SCREEN = "video_detail_open";
    public static final String VIDEO_DETAIL_OPEN_SCREEN_CHANNEL = "video_detail_open_channel";
    public static final String VIDEO_DETAIL_OPEN_SCREEN_CHANNEL_DETAIL = "video_detail_open_channel_detail";

    public static String getDateFromTimestamp(long timeStamp, String dateFormat) {

        try {
            DateFormat sdf = new SimpleDateFormat(dateFormat);
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getCurrentTime(String format) {
        String currentTime = "";

        if (format != null) {
            long currentMilli = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentMilli);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            currentTime = simpleDateFormat.format(calendar.getTime());

            return currentTime;
        } else {
            return currentTime;
        }

    }

    public static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static int getCurrentHour() {
        int hour = 0;
        Calendar cal = Calendar.getInstance(); //Create Calendar-Object
        cal.setTime(new Date());               //Set the Calendar to now
        hour = cal.get(Calendar.HOUR_OF_DAY); //Get the hour from the calendar

        return hour;
    }

    public static int getCurrentMinute() {
        int minutes = 0;
        Calendar cal = Calendar.getInstance(); //Create Calendar-Object
        cal.setTime(new Date());               //Set the Calendar to now
        minutes = cal.get(Calendar.MINUTE); //Get the hour from the calendar

        return minutes;
    }

    public static int getCurrentSecond() {
        int second = 0;
        Calendar cal = Calendar.getInstance(); //Create Calendar-Object
        cal.setTime(new Date());               //Set the Calendar to now
        second = cal.get(Calendar.SECOND); //Get the hour from the calendar

        return second;
    }

    public static String getCurrentTimeInDesiredFormat(String formatToGet) {
        try {
            String str = "";
            //return 24 hour format as kk is used
            SimpleDateFormat sdf = new SimpleDateFormat(formatToGet);
            str = sdf.format(new Date());
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void makeToast(String message, Context mContext) {
        try {
            Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int getPixelValue(Context context, int dp) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, resources.getDisplayMetrics());
    }

    public static String convertTimeToTwelveHFmt(String timeToConvert) {
        try {
            final String time = timeToConvert;

            try {
                final SimpleDateFormat sdf24H = new SimpleDateFormat("kk:mm");
                final SimpleDateFormat sdf12H = new SimpleDateFormat("hh:mm a");
                final Date dateObj = sdf24H.parse(time);

                // Log.d("con t: ","cov t: "+sdf12H.format(dateObj));
                return sdf12H.format(dateObj);
            } catch (final ParseException e) {
                e.printStackTrace();
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static boolean isInternetConnected(Context context) {
        boolean isConnected = false;
        try {

            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            return isConnected;
        } catch (Exception e) {
            e.printStackTrace();
            return isConnected;
        }

    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static String getStringText(Context context, int textToget) {
        String textToReturn = "";
        try {
            textToReturn = context.getResources().getString(textToget);
            return textToReturn;
        } catch (Exception e) {
            e.printStackTrace();
            return textToReturn;
        }

    }

    public static Drawable getDrawables(Context context, int drawables) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return context.getResources().getDrawable(drawables, null);
            } else {
                return context.getResources().getDrawable(drawables);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Date convertTimeToDate(String times) {
        Date dtToReturn = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("kk:mm");
            try {
                Date date = format.parse(times);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                dtToReturn = calendar.getTime();

                return dtToReturn;
            } catch (ParseException e) {

                e.printStackTrace();
                return dtToReturn;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return dtToReturn;
        }
    }


    public static void sout(String object) {
        try {
            System.out.println("" + object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static OkHttpClient getOkHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);


        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build();
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


    public static PackageInfo appVersionNameandCode(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return pInfo;
    }

    public static void cancelAllNotifications(Context context) {
        try {
            // Clear all notification

            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImageGlide(Context context, String image, ImageView imageView) {
        try {
            if (!image.startsWith("http")) {
                if (!image.startsWith("//")) {
                    image = "http://" + image;
                } else {
                    image = "http:" + image;
                }
            }
            Glide.with(context).
                    load(image).
                    crossFade().
                    placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImageGlideProductDetails(Context context, String image, ImageView imageView) {
        try {
            if (!image.startsWith("http")) {
                if (!image.startsWith("//")) {
                    image = "http://" + image;
                } else {
                    image = "http:" + image;
                }
            }

            Glide.with(context).
                    load(image).
                    crossFade().
                    placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImageGlideProduct(Context context, String image, ImageView imageView) {
        try {
            if (!image.startsWith("http")) {
                if (!image.startsWith("//")) {
                    image = "http://" + image;
                } else {
                    image = "http:" + image;
                }
            }

            Glide.with(context).
                    load(image).
                    crossFade()
                    .override(300, 300).
                    placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImageGlideProductDetail(Context context, String image, ImageView imageView) {
        try {
            if (!image.startsWith("http")) {
                if (!image.startsWith("//")) {
                    image = "http://" + image;
                } else {
                    image = "http:" + image;
                }
            }
            Glide.with(context).
                    load(image).
                    crossFade().
                    placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isValidString(String str) {
        if (str != null && str.trim().length() > 0)
            return true;
        else
            return false;
    }

    public static int getVisibility(String visibility) {
        Log.d(TAG, "getVisibility: " + visibility);
        switch (visibility) {
            case "1":
                return VISIBLE;
            case "0":
                return GONE;
            default:
                return VISIBLE;
        }
    }

    public static int getTextStyle(String textStyle) {
        switch (textStyle) {
            case "0":
                return Typeface.BOLD;
            case "1":
                return Typeface.NORMAL;
            case "2":
                return Typeface.ITALIC;
            default:
                return Typeface.NORMAL;
        }
    }

    public static int getGravity(String gravity) {
        Log.d(TAG, "getGravity: " + gravity);
        switch (gravity) {
            case "0":
                return Gravity.CENTER;
            case "1":
                return Gravity.TOP;
            case "2":
                return Gravity.LEFT;
            case "3":
                return Gravity.RIGHT;
            case "4":
                return Gravity.BOTTOM;
            default:
                return Gravity.CENTER;
        }
    }

    public static Typeface getTypeFaceFromFontPath(Context context, String path, String defaultFont) {
        Typeface customFontTypeFace = Typeface.createFromAsset(context.getAssets(), path);
        if (customFontTypeFace == null) {
            customFontTypeFace = Typeface.createFromAsset(context.getAssets(), defaultFont);
        }
        return customFontTypeFace;
    }

    public static String getVisibility(Enums.VISIBILITY visibility) {
        switch (visibility) {
            case VISIBLE:
                return "1";
            case GONE:
                return "0";
            default:
                return "1";
        }
    }

    public static String getGravity(Enums.GRAVITY gravity) {
        switch (gravity) {
            case CENTER:
                return "0";
            case TOP:
                return "1";
            case LEFT:
                return "2";
            case RIGHT:
                return "3";
            case BOTTOM:
                return "4";
            default:
                return "0";
        }
    }

    public static String getTextStyle(Enums.FONT_STYLE textStyle) {
        switch (textStyle) {
            case BOLD:
                return "0";
            case NORMAL:
                return "1";
            case ITALIC:
                return "2";
            default:
                return "1";
        }
    }

    public static Drawable changeDrawableColor(Drawable drawable, int color) {
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    public static String getTransformation(Enums.TRANSFORMATION textStyle) {
        switch (textStyle) {
            case UPPER_CASE:
                return "0";
            case LOWER_CASE:
                return "1";
            default:
                return "0";
        }
    }

    public static float getValidFloat(float size, float defaultValue) {
        if (size != 0.0f)
            return size;
        else
            return defaultValue;
    }
}
