package com.example.philip.alphafitness;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.SystemClock;
import android.util.Log;

import com.example.philip.alphafitness.background.LocationTracker;
import com.example.philip.alphafitness.background.RunningService;
import com.example.philip.alphafitness.background.StepCounter;
import com.example.philip.alphafitness.database.DBContentProvider;
import com.example.philip.alphafitness.database.DBHandler;
import com.example.philip.alphafitness.database.DataHolder;
import com.example.philip.alphafitness.database.StopWatch;
import com.example.philip.alphafitness.database.WorkoutData;
import com.example.philip.alphafitness.userProfile.Watch;

import java.util.ArrayList;
import java.util.Date;

public class Calculator {

    private static double distance;
    private static double calories;
    private String duration;
    private static int steps;
    private double averageSpeed;

    public static Double userWeight = 0.0;


    private DBHandler dbHandler;
    private ArrayList<DataHolder> list;


    private static double STEPS_PER_MILE = 2000;
    Context context;

    public Calculator(Context context) {
        this.context = context;
        this.dbHandler = new DBHandler(context);
        this.list = dbHandler.readFromDB();
    }

    class Week{
        ArrayList<WorkoutData> data = new ArrayList<>();
    }

    public Double getDistanceFromLatLonInMeters(Double lat1, Double lon1, Double lat2, Double lon2) {
        Double R = 6371.0; // Radius of the earth in km
        Double dLat = deg2rad(lat2-lat1);
        Double dLon = deg2rad(lon2-lon1);
        Double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double d = R * c; // Distance in km
        return d*1000.0;
    }

    private Double deg2rad(Double deg) {
        return deg * (Math.PI/180);
    }

    // return distance in miles
    public static double getDistance() {
        steps = StepCounter.getSteps();

        distance = steps / STEPS_PER_MILE;
        //Log.d("StepsInCalculator: ", Long.toString(steps));

        return distance;
    }


    // returns calories burned per mile of running

    public static double getCalories() {
        return userWeight * 0.57 * getDistance();
    }

    public static double getCalories(Double distance) {
        calories = (userWeight * 0.57 * distance);
        return calories;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getAverageSpeed() {
        averageSpeed = (getDistance()*1.6)/ (double) (SystemClock.uptimeMillis() - RunningService.getStartTime()) *1000.0*60.*60.0;

        return averageSpeed;
    }


   /* public double getMaxSpeed() {

        DataHolder lastSavedLocation = null;
        Double maxSpeed = 0.0;

        for(DataHolder holder : list) {

            if(lastSavedLocation == null){

                lastSavedLocation = holder;

            } else {
                Double d = getDistanceFromLatLonInMeters(holder.getLongitude(),
                        holder.getLatitude(),
                        lastSavedLocation.getLongitude(),
                        lastSavedLocation.getLatitude());

                Log.e("distance of points", Double.toString(d));

                long timeDifferenceMilisec = holder.getDate().getTime() - lastSavedLocation.getDate().getTime();

                timeDifferenceMilisec = Math.abs(timeDifferenceMilisec);

                double currentSpeed = (d*1.6 / timeDifferenceMilisec) *1000.0*60.*60.0;

                if(currentSpeed > maxSpeed) {
                    maxSpeed = currentSpeed;
                }
            }
        }

        return maxSpeed;
    }


    public double getMinSpeed() {

        DataHolder lastSavedLocation = null;
        Double minSpeed = null;

        for (DataHolder holder : list) {

            if (lastSavedLocation == null) {

                lastSavedLocation = holder;

            } else {
                Double d = getDistanceFromLatLonInMeters(holder.getLongitude(), holder.getLatitude(), lastSavedLocation.getLongitude(),
                        lastSavedLocation.getLatitude());

                long timeDifference = holder.getDate().getTime() - lastSavedLocation.getDate().getTime();

                double currentSpeed = (d*1.6 / timeDifference) *1000.0*60.*60.0;
                if (minSpeed == null || currentSpeed < minSpeed) {
                    minSpeed = currentSpeed;
                }
            }
        }
        return minSpeed;
    }*/

    public Double getMaxSpeed(){

        ArrayList<StopWatch> stopWatches = dbHandler.getStopWatchData();

        double max = 0.0;

        for (StopWatch watch : stopWatches){
            if(watch.getId() == DBHandler.getCurrentWorkoutID()){
                if(watch.getTime() > max){
                    max = watch.getTime();
                }
            }
        }


        return max/(1000.0*60.0);
    }

    public Double getMinSpeed(){

        ArrayList<StopWatch> stopWatches = dbHandler.getStopWatchData();

        Long min = null;

        for (StopWatch watch : stopWatches){
            if(watch.getId() == DBHandler.getCurrentWorkoutID()){
                if(min == null || watch.getTime() < min){
                    min = watch.getTime();
                }
            }
        }

        if (min == null){
            return 0.0;
        }

        return min/(1000.0*60.0);
    }

    public Integer getWorkoutCount(){
        return dbHandler.getWorkoutCount();
    }


    public WorkoutData getWorkoutDetailsSum(){
        ArrayList<WorkoutData> list = dbHandler.getWorkoutData();
        return getWorkoutDetailsSum(list);
    }

    private WorkoutData getWorkoutDetailsSum(ArrayList<WorkoutData> list){

        Integer steps = 0;
        long duration = 0;
        Double distance = 0.0;
        Integer workoutCount = 0;

        for (WorkoutData data: list){
            steps += data.getSteps();
            Log.e("Calc - duration", Double.toString(data.getDuration()));
            duration += data.getDuration();
            distance += data.getDistance();
            workoutCount += data.getWorkoutCount();
        }
        Log.e("duration", Double.toString(duration));

        WorkoutData sum = new WorkoutData(steps, duration, distance, 0);
        sum.setWorkoutCount(workoutCount);

        return sum;
    }

    public WorkoutData getWorkoutDetailsWeeklyAVG(){
        ArrayList<WorkoutData> list = dbHandler.getWorkoutData();
        Watch watch = new Watch(0);

        int wDayBefore = 0;
        int dayBefore = 0;

        ArrayList<Week> weeks = new ArrayList<>();
        Week week = null;

        for (WorkoutData data: list){
            long time = data.getId();
            watch.setMilliSeconds(time);
            int wDay = watch.getWeekDay();
            int day = watch.getDays();
            Log.e("Data-Day", Long.toString(data.getDuration()));
            if (wDay >= wDayBefore && Math.abs(day-dayBefore) < 7){
                week.data.add(data);
                Log.e("save", Long.toString(data.getDuration()));
            }else{
                if (week != null){
                    weeks.add(week);
                }

                week = new Week();
                week.data.add(data);
                Log.e("newWeek", Long.toString(data.getDuration()));
            }

            wDayBefore = wDay;
            dayBefore = day;
        }
        Log.e("WeekDataCount", Integer.toString(week.data.size()));

        weeks.add(week);

        Log.e("WeekCount", Integer.toString(weeks.size()));

        ArrayList<WorkoutData> data = new ArrayList<>();

        for (Week w: weeks){

            WorkoutData weekSum = getWorkoutDetailsSum(w.data);
            weekSum.setWorkoutCount(w.data.size());

            data.add(weekSum);
            Log.e("week", Double.toString(getWorkoutDetailsSum(w.data).getDuration()));
        }

        WorkoutData sum = getWorkoutDetailsSum(data);
        sum.makeAVG(weeks.size());

        return sum;
    }

}
