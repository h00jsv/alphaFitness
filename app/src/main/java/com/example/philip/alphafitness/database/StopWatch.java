package com.example.philip.alphafitness.database;

public class StopWatch {
    private long id;
    private long time;

    public StopWatch(long id, long time) {
        this.id = id;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }
}
