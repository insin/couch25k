package couch25k.workouts;

import java.util.Date;

/**
 * Provides a view over a nested list of intervals representing Couch-to-5k
 * workouts, with alternating walking and jogging.
 */
public class WorkoutView {
    public static final int WORKOUTS_PER_WEEK = 3;

    public static final String WALK = "Walk";
    public static final String JOG = "Jog";

    private int[][] intervals;
    private Date[] completionDates;

    public WorkoutView(int[][] intervals, Date[] completionDates) {
        this.intervals = intervals;
        this.completionDates = completionDates;
    }

    public int getWeekCount() {
        return this.intervals.length / WORKOUTS_PER_WEEK;
    }

    public boolean isWeekComplete(int week) {
        int weekStart = week * WORKOUTS_PER_WEEK;
        int weekEnd = weekStart + WORKOUTS_PER_WEEK;
        for (int i = weekStart; i < weekEnd; i++) {
            if (completionDates[i] == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isWorkoutComplete(int week, int workout) {
        return completionDates[WorkoutView.getWorkoutIndex(week, workout)] != null;
    }

    public Workout getFirstInCompleteWorkout() {
        for (int i = 0; i < intervals.length; i++) {
            if (completionDates[i] == null) {
                return getWorkout(i);
            }
        }
        return null;
    }

    public Workout getWorkout(int intervalIndex) {
        return getWorkout(intervalIndex / WORKOUTS_PER_WEEK,
                          intervalIndex % WORKOUTS_PER_WEEK);
    }

    public Workout getWorkout(int week, int workout) {
        return new Workout(week, workout,
                           this.getWorkoutSteps(week, workout),
                           this.getWorkoutDuration(week, workout),
                           completionDates[WorkoutView.getWorkoutIndex(week, workout)]);
    }

    public int getWorkoutDuration(int week, int workout) {
        int[] interval = intervals[WorkoutView.getWorkoutIndex(week, workout)];
        int duration = 0;
        for (int i = 0; i < interval.length; i++) {
            duration += interval[i];
        }
        return duration;
    }

    public WorkoutStep[] getWorkoutSteps(int week, int workout) {
        int[] interval = intervals[WorkoutView.getWorkoutIndex(week, workout)];
        WorkoutStep[] steps = new WorkoutStep[interval.length];
        for (int i = 0; i < interval.length; i++) {
            steps[i] = new WorkoutStep(i % 2 == 0 ? WALK : JOG, interval[i]);
        }
        return steps;
    }

    public void completeWorkout(int week, int workout, Date completionDate ) {
        this.completionDates[WorkoutView.getWorkoutIndex(week, workout)] = completionDate;
    }

    public static int getWorkoutIndex(int week, int workout) {
        return (week * WORKOUTS_PER_WEEK) + workout;
    }
}
