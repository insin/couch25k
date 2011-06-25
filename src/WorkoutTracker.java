import java.util.TimerTask;

public class WorkoutTracker extends TimerTask {
    private Couch25K app;
    private Workout workout;
    private WorkoutStep currentStep;
    private int step;
    private int counter;
    private int totalTime;

    public WorkoutTracker(Couch25K app, Workout workout) {
        this.app = app;
        this.workout = workout;
        step = 0;
        counter = 0;
        totalTime = 0;
        currentStep = workout.steps[0];
        app.updateStep(1, currentStep);
        app.updateProgress(counter, totalTime);
    }

    public void run() {
        totalTime++;
        counter++;
        if (counter >= currentStep.duration) {
            if (step == workout.steps.length - 1) {
                // Completed the last step
                app.finishWorkout();
                return;
            }

            // Advance to the next step
            step++;
            currentStep = workout.steps[step];
            counter = 0;
            app.updateStep(step + 1, currentStep);
        }
        app.updateProgress(counter, totalTime);
    }
}