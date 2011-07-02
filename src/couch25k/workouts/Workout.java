package couch25k.workouts;

import java.util.Date;

public class Workout {
    public int week;
    public int workout;
    public WorkoutStep[] steps;
    public int duration;
    public Date completedAt;
    public Workout(int week, int workout, WorkoutStep[] steps, int duration, Date completedAt) {
        this.week = week;
        this.workout = workout;
        this.steps = steps;
        this.duration = duration;
        this.completedAt = completedAt;
    }
}
