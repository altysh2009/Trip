package com.project.altysh.firebaseloginandsaving.mapUtil;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.project.altysh.firebaseloginandsaving.dto.PointL;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;

import java.util.List;

/**
 * Created by Altysh on 3/10/2018.
 */

public class MaPUtil {
    public static Intent get3DView(double lat, double lon) {
        Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + lat + "," + lon);
        //Uri gmmIntentUri = Uri.parse("google.streetview:cbll=29.9774614,31.1329645&cbp=0,30,0,0,-15");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        return mapIntent;

    }

    public static Intent getIntentDir(Trip_DTO trip_dto) {
        Intent intent = new Intent(Intent.ACTION_VIEW, getUri(trip_dto));
        intent.setPackage("com.google.android.apps.maps");
        //startActivity(intent);
        return intent;
    }

    public static String getStaticMapNoRoad(Trip_DTO trip_dto) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://maps.googleapis.com/maps/api/staticmap?");
        StringBuilder builder = new StringBuilder();
        builder.append("size=600x300&maptype=roadmap");
        builder.append("&markers=color:blue|label:").append(trip_dto.getStartPoint()).append("|").append(trip_dto.getStartLatitude()).append(",").append(trip_dto.getStartLongitude());
        for (int i = 0; i < trip_dto.getEndLongitude().size(); i++) {
            builder.append("&markers=color:blue|label:").append(trip_dto.getEndPoint().get(i)).append("|").append(trip_dto.getEndLatitude().get(i)).append(",").append(trip_dto.getEndLongitude().get(i));
        }

        String url = stringBuilder.append(builder.toString().replace(" ", "")).toString();
        return url;

    }

    public static String getStaticMapRoad(String point, List<PointL> pointLS) {
        StringBuilder builder = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        // builder.append(point);
        if (pointLS.size() > 0)
            builder.append("&path=color:0x0000ff|weight:5");
        for (PointL pointL : pointLS) {
            builder.append("|").append(pointL.getLon()).append(",").append(pointL.getLan());
        }

        String url = stringBuilder.append(point).append(builder.toString().replace(" ", "")).toString();
        return url;

    }


    private static Uri getUri(Trip_DTO trip_dto) {
        StringBuffer builder = new StringBuffer();
        builder.append("http//:");
        builder.append("maps.google.com/maps?");
        builder.append("saddr=" + trip_dto.getStartLatitude() + "," + trip_dto.getStartLongitude());
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(trip_dto.getEndLatitude().get(0)).append(",").append(trip_dto.getEndLongitude().get(0));
        for (int i = 1; i < trip_dto.getEndLatitude().size(); i++) {
            stringBuffer.append("+to:");
            stringBuffer.append(trip_dto.getEndLatitude().get(i)).append(",").append(trip_dto.getEndLongitude().get(i));
        }

        builder.append("&daddr=" + stringBuffer.toString());
        //Log.e("uri", "getUri: "+Uri.parse(uri));
        Log.e("uri", "getUri: " + builder);
        //return builder.build();
        return Uri.parse(builder.toString());
    }

}
