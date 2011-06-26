import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Couch25K extends MIDlet implements CommandListener {
    private static final int STATE_SELECT_WEEK = 1;
    private static final int STATE_SELECT_WORKOUT = 2;
    private static final int STATE_WORKOUT_SELECTED = 3;
    private static final int STATE_WORKOUT = 4;
    private static final int STATE_WORKOUT_PAUSED = 5;
    private static final int STATE_WORKOUT_COMPLETE = 6;

    private int state;
    private int selectedWeek;
    private int selectedWorkout;
    private Workout workout;
    private WorkoutState workoutState;

    private Command backCommand = new Command("Back", Command.BACK, 1);
    private Display display;
    private List selectWeekScreen;
    private Command selectWeekCommand = new Command("Select", Command.ITEM, 1);
    private List selectWorkoutScreen;
    private Command selectWorkoutCommand = new Command("Select", Command.ITEM, 1);
    private Form workoutSummaryScreen;
    private Command startWorkoutCommand = new Command("Start", Command.SCREEN, 1);
    private Form workoutScreen;
    private Command pauseWorkoutCommand = new Command("Pause", Command.SCREEN, 2);
    private Command resumeWorkoutCommand = new Command("Resume", Command.SCREEN, 3);
    private StringItem action;
    private Gauge stepProgress;
    private StringItem timeDisplay;
    private Gauge workoutProgress;
    private Form workoutCompleteScreen;

    // Workout timing
    private Timer workoutTimer;

    // MIDlet API --------------------------------------------------------------

    protected void startApp() throws MIDletStateChangeException {
        if (display == null) {
            display = Display.getDisplay(this);
            // Week selection screen setup
            selectWeekScreen = new List("Select Week", Choice.IMPLICIT,
                new String[] {
                    "Week 1", "Week 2", "Week 3", "Week 4", "Week 5", "Week 6",
                    "Week 7", "Week 8", "Week 9"
                }, null);
            selectWeekScreen.addCommand(selectWeekCommand);
            selectWeekScreen.setCommandListener(this);

            // Workout selection screen setup
            selectWorkoutScreen = new List("Select Workout", Choice.IMPLICIT,
                new String[] {
                    "Workout 1", "Workout 2", "Workout 3"
                }, null);
            selectWorkoutScreen.addCommand(selectWorkoutCommand);
            selectWorkoutScreen.addCommand(backCommand);
            selectWorkoutScreen.setCommandListener(this);

            // Workout summary screen setup
            workoutSummaryScreen = new Form("");
            workoutSummaryScreen.addCommand(startWorkoutCommand);
            workoutSummaryScreen.addCommand(backCommand);
            workoutSummaryScreen.setCommandListener(this);

            // Workout screen setup
            workoutScreen = new Form("");
            action = new StringItem(null, "");
            stepProgress = new Gauge("Step", false, Gauge.INDEFINITE, 0);
            timeDisplay = new StringItem(null, "");
            workoutProgress = new Gauge("Workout", false, Gauge.INDEFINITE, 0);
            workoutScreen.append(action);
            workoutScreen.append(stepProgress);
            workoutScreen.append(timeDisplay);
            workoutScreen.append(workoutProgress);
            workoutScreen.addCommand(pauseWorkoutCommand);
            workoutScreen.setCommandListener(this);

            workoutCompleteScreen = new Form("Workout Complete");
            workoutCompleteScreen.setCommandListener(this);
        }

        if (state == STATE_WORKOUT_PAUSED) {
            startWorkout();
        } else {
          init();
        }
    }

    protected void pauseApp() {
        if (state == STATE_WORKOUT) {
            pauseWorkout();
        }
    }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }

    // CommandListener API -----------------------------------------------------

    public void commandAction(Command c, Displayable d) {
        switch (state) {
        case STATE_SELECT_WEEK:
            if (c == selectWeekCommand) selectWeek();
            break;
        case STATE_SELECT_WORKOUT:
            if (c == selectWorkoutCommand) selectWorkout();
            else if (c == backCommand) init();
            break;
        case STATE_WORKOUT_SELECTED:
            if (c == startWorkoutCommand) startWorkout();
            else if (c == backCommand) selectWeek();
            break;
        case STATE_WORKOUT:
            if (c == pauseWorkoutCommand) pauseWorkout();
            break;
        case STATE_WORKOUT_PAUSED:
            if (c == resumeWorkoutCommand) resumeWorkout();
            break;
        default:
            throw new IllegalStateException("Command " + c +
                                            " not expected in state " + state);
        }
    }

    // State transitions -------------------------------------------------------

    public void init() {
        display.setCurrent(selectWeekScreen);
        state = STATE_SELECT_WEEK;
    }

    public void selectWeek() {
        selectedWeek = selectWeekScreen.getSelectedIndex() + 1;

        display.setCurrent(selectWorkoutScreen);
        state = STATE_SELECT_WORKOUT;
    }

    public void selectWorkout() {
        selectedWorkout = selectWorkoutScreen.getSelectedIndex() + 1;
        workout = Workouts.getWorkout(selectedWeek, selectedWorkout);

        workoutSummaryScreen.setTitle("Week " + selectedWeek +
                                      ", Workout " + selectedWorkout);
        workoutSummaryScreen.deleteAll();
        for (int i = 0; i < workout.steps.length; i++) {
            workoutSummaryScreen.append(new StringItem(null,
                workout.steps[i].action + " for " +
                secondsToDisplayTime(workout.steps[i].duration) + "\n"
            ));
        }
        display.setCurrent(workoutSummaryScreen);
        state = STATE_WORKOUT_SELECTED;
    }

    public void startWorkout() {
        workoutState = new WorkoutState(this, workout);

        workoutScreen.setTitle("Week " + selectedWeek +
                               ", Workout " + selectedWorkout);
        workoutProgress.setMaxValue(workout.totalDuration);
        workoutProgress.setValue(0);
        display.setCurrent(workoutScreen);
        trackWorkoutState(workoutState);
        state = STATE_WORKOUT;
    }

    public void pauseWorkout() {
        workoutTimer.cancel();

        workoutScreen.removeCommand(pauseWorkoutCommand);
        workoutScreen.addCommand(resumeWorkoutCommand);
        state = STATE_WORKOUT_PAUSED;
    }

    public void resumeWorkout() {
        workoutScreen.removeCommand(resumeWorkoutCommand);
        workoutScreen.addCommand(pauseWorkoutCommand);
        trackWorkoutState(workoutState);
        state = STATE_WORKOUT;
    }

    public void finishWorkout() {
        workoutTimer.cancel();

        display.setCurrent(workoutCompleteScreen);
        playSound("finished");
        state = STATE_WORKOUT_COMPLETE;
    }

    // Status update API -------------------------------------------------------

    public void updateStep(int stepNum, WorkoutStep step) {
        action.setText(step.action + " for " + secondsToDisplayTime(step.duration));
        stepProgress.setLabel("Step " + stepNum + " of " + workout.steps.length);
        stepProgress.setValue(0);
        stepProgress.setMaxValue(step.duration);
        if (stepNum > 1) {
            playSound(step.action.toLowerCase());
        }
    }

    public void updateProgress(int progress, int totalTime) {
        stepProgress.setValue(progress);
        timeDisplay.setText(secondsToTime(totalTime));
        workoutProgress.setValue(totalTime);
    }

    // Utilities ---------------------------------------------------------------

    private void trackWorkoutState(final WorkoutState workoutState) {
        workoutTimer = new Timer();
        workoutTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                workoutState.increment();
            }
        }, 0, 1000);
    }

    private void playSound(String action) {
        try {
            Manager.createPlayer(
                getClass().getResourceAsStream("/" + action + ".wav"),
                "audio/x-wav"
            ).start();
        } catch (MediaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String secondsToDisplayTime(int n) {
        if (n <= 90) {
            return n + " sec";
        }
        int min = n / 60;
        int sec = n % 60;
        if (sec == 0) {
          return min + " min";
        }
        return min + " min " + sec + " sec";
    }

    private String secondsToTime(int n) {
        int minutes = n / 60;
        int seconds = n % 60;
        return pad(minutes) + ":" + pad(seconds);
    }

    private String pad(int n) {
        if (n < 10) {
            return "0" + n;
        }
        return "" + n;
    }
}
