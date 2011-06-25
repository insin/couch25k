public class WorkoutState {
    private Couch25K app;
    private Workout workout;
    public WorkoutStep currentStep;
    public int step;
    public int counter;
    public int totalTime;

    public WorkoutState(Couch25K app, Workout workout) {
        this.app = app;
        this.workout = workout;
        step = 0;
        counter = 0;
        totalTime = 0;
        currentStep = workout.steps[0];
        app.updateStep(1, currentStep);
        app.updateProgress(counter, totalTime);
    }

    public void increment() {
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
