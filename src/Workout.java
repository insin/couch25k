public class Workout {
	public static final String WALK = "Walk";
	public static final String JOG = "Jog";
	public WorkoutStep[] steps;
	public int totalDuration;
	public Workout(WorkoutStep[] steps) {
		this.steps = steps;
		this.totalDuration = 0;
		for (int i = 0; i < steps.length; i++) {
			this.totalDuration += steps[i].duration;
		}
	}
}
