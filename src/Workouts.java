import java.util.Hashtable;

public class Workouts {
	private static Hashtable workouts;
	static 
	{
		workouts = new Hashtable();
		
		Workout testWorkout = new Workout(new WorkoutStep[] {
	        new WorkoutStep(Workout.WALK, 10)
		  , new WorkoutStep(Workout.JOG, 10)
	      , new WorkoutStep(Workout.WALK, 10)
		});

		// Week 1
		Workout week1Workout = new Workout(new WorkoutStep[] {
			new WorkoutStep(Workout.WALK, 300)
		  , new WorkoutStep(Workout.JOG, 60)
		  , new WorkoutStep(Workout.WALK, 90)
		  , new WorkoutStep(Workout.JOG, 60)
	      , new WorkoutStep(Workout.WALK, 90)
		  , new WorkoutStep(Workout.JOG, 60)
		  , new WorkoutStep(Workout.WALK, 90)
		  , new WorkoutStep(Workout.JOG, 60)
	      , new WorkoutStep(Workout.WALK, 90)
		  , new WorkoutStep(Workout.JOG, 60)
		  , new WorkoutStep(Workout.WALK, 90)
		  , new WorkoutStep(Workout.JOG, 60)
	      , new WorkoutStep(Workout.WALK, 90)
		  , new WorkoutStep(Workout.JOG, 60)
		  , new WorkoutStep(Workout.WALK, 90)
		  , new WorkoutStep(Workout.JOG, 60)
	      , new WorkoutStep(Workout.WALK, 90)
		});
		workouts.put("1-1", testWorkout);
		workouts.put("1-2", week1Workout);
		workouts.put("1-3", week1Workout);

		Workout week2Workout = new Workout(new WorkoutStep[] {
				new WorkoutStep(Workout.WALK, 300)
			  , new WorkoutStep(Workout.JOG, 90)
			  , new WorkoutStep(Workout.WALK, 120)
			  , new WorkoutStep(Workout.JOG, 90)
		      , new WorkoutStep(Workout.WALK, 120)
			  , new WorkoutStep(Workout.JOG, 90)
			  , new WorkoutStep(Workout.WALK, 120)
			  , new WorkoutStep(Workout.JOG, 90)
		      , new WorkoutStep(Workout.WALK, 120)
			  , new WorkoutStep(Workout.JOG, 90)
			  , new WorkoutStep(Workout.WALK, 120)
			  , new WorkoutStep(Workout.JOG, 90)
		      , new WorkoutStep(Workout.WALK, 120)
			});
		workouts.put("2-1", week2Workout);
		workouts.put("2-2", week2Workout);
		workouts.put("2-3", week2Workout);
	}

	public static Workout getWorkout(int week, int workout) {
		return (Workout)workouts.get(week + "-" + workout);
	}
}
