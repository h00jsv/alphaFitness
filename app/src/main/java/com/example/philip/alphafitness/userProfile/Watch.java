package com.example.philip.alphafitness.userProfile;

import java.util.Calendar;
import java.util.Date;

public class Watch {
    long milliSeconds;

    public Watch(long milliSeconds){
        this.milliSeconds = milliSeconds;
    }

    public Integer getMilliSeconds(){
        return (int) milliSeconds%1000;
    }

    public Integer getSeconds(){
        return (int)((double)milliSeconds/(1000.0)%60);
    }

    public Integer getMinutes(){
        return (int)((double)milliSeconds/(1000.0*60.0)%60);
    }

    public Integer getHours(){
        return (int)((double)milliSeconds/(1000.0*60.0*60.0)%24);
    }

    public Integer getDays(){
        return (int)((double)milliSeconds/(1000.0*60.0*60.0*24.0)) ;
    }

    public void setMilliSeconds(long milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    public Integer getWeekDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(milliSeconds));
        return cal.get(Calendar.DAY_OF_WEEK);
    }

}
