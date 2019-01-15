package com.example.philip.alphafitness.database;


public class WorkoutData {
    private Integer steps;
    private long duration;
    private Double distance;
    private long id;
    private Integer workoutCount = 1;

    public WorkoutData(Integer steps, long duration, Double distance, long id) {
        this.steps = steps;
        this.duration = duration;
        this.distance = distance;
        this.id = id;
    }

    public Integer getSteps() {
        return steps;
    }

    public long getDuration() {
        return duration;
    }

    public Double getDistance() {
        return distance;
    }

    public long getId() {
        return id;
    }

    public Integer getWorkoutCount() {
        return workoutCount;
    }

    public void setWorkoutCount(Integer wokoutCount) {
        this.workoutCount = wokoutCount;
    }

    public void makeAVG(int size){
        steps = (int)((double)steps/(double)size);
        duration /= (double)size;
        distance /= (double)size;
        workoutCount = (int)((double)workoutCount/(double)size);
    }
}
