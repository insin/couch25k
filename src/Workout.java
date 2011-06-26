public class Workout {
    public static final String WALK = "Walk";
    public static final String JOG = "Jog";

    public WorkoutStep[] steps;
    public int totalDuration;

    /**
     * @param intervals workout intervals, where intervals alternately represent
     *                  walking and jogging.
     */
    public Workout(int[] intervals) {
        totalDuration = 0;
        steps = new WorkoutStep[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            steps[i] = new WorkoutStep(
                i % 2 == 0 ? WALK : JOG,
                intervals[i]
            );
            totalDuration += intervals[i];
        }
    }
}
