package com.learning.dayoffmanagement.Until;

import android.content.Context;
import android.widget.Toast;

import com.learning.dayoffmanagement.Model.LeaveForm;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class Utility {

    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    static SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyy hh:mm:aa");
    static SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
    static SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:aa");

    public static String formatDateTime(LocalDate date){
        return sdfDateTime.format(date);
    }
    public static String formatDate(LocalDate date){
        return sdfDate.format(date);
    }
    public static String formatTime(LocalDate date){
        return sdfTime.format(date);
    }

    public static double calTime(LeaveForm leaveForm){
        Calendar startDate = Calendar.getInstance() ,endDate = Calendar.getInstance();
        String getStartDate = leaveForm.getStartDate();
        String getEndDate = leaveForm.getEndDate();

        StringTokenizer stkStartDate = new StringTokenizer(getStartDate,"/");

        String getStartDay = stkStartDate.nextToken();
        String getStartMonth = stkStartDate.nextToken();
        String getStartYear = getStartDate.substring(getStartDate.length()-4);

        startDate.set(Integer.parseInt(getStartYear), Integer.parseInt(getStartMonth), Integer.parseInt(getStartDay));

        StringTokenizer stkEndDate = new StringTokenizer(getEndDate,"/");

        String getEndDay = stkEndDate.nextToken();
        String getEndMonth = stkEndDate.nextToken();
        String getEndYear = getEndDate.substring(getEndDate.length()-4);
        endDate.set(Integer.parseInt(getEndYear), Integer.parseInt(getEndMonth), Integer.parseInt(getEndDay));

        double res = (endDate.getTimeInMillis() - startDate.getTimeInMillis()) / (1000*60*60*24);

        return res;
    }

    public static double calTime(String startDate,String endDate){
        Calendar startDateCal = Calendar.getInstance() ,endDateCal = Calendar.getInstance();

        StringTokenizer stkStartDate = new StringTokenizer(startDate,"/");

        String getStartDay = stkStartDate.nextToken();
        String getStartMonth = stkStartDate.nextToken();
        String getStartYear = startDate.substring(startDate.length()-4);

        startDateCal.set(Integer.parseInt(getStartYear), Integer.parseInt(getStartMonth), Integer.parseInt(getStartDay));

        StringTokenizer stkEndDate = new StringTokenizer(endDate,"/");

        String getEndDay = stkEndDate.nextToken();
        String getEndMonth = stkEndDate.nextToken();
        String getEndYear = endDate.substring(endDate.length()-4);
        endDateCal.set(Integer.parseInt(getEndYear), Integer.parseInt(getEndMonth), Integer.parseInt(getEndDay));

        double res = (endDateCal.getTimeInMillis() - startDateCal.getTimeInMillis()) / (1000*60*60*24);

        return res;
    }

}
