import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Tracks jogging and walking intervals for the Couch-to-5k running program.
 */
public class Couch25K extends MIDlet implements CommandListener, PlayerListener {
    static final int STATE_SELECT_WEEK = 1;
    static final int STATE_SELECT_WORKOUT = 2;
    static final int STATE_WORKOUT_SELECTED = 3;
    static final int STATE_WORKOUT = 4;
    static final int STATE_WORKOUT_PAUSED = 5;
    static final int STATE_WORKOUT_COMPLETE = 6;

    // MIDlet state ------------------------------------------------------------

    /** Current app state. */
    int state;
    /** Workout configuration and completion status. */
    Week[] weeks;
    /** Index of selected week on the Select Week screen. */
    int selectedWeek;
    /** Workout configuration for the selected week. */
    Week week;
    /** Index of selected workout on the Select Workout screen. */
    int selectedWorkout;
    /** Active Workout configuration. */
    Workout workout;
    /** State persistence. */
    WorkoutStore workoutStore;

    void initialiseState() {
        weeks = Workouts.getWorkouts();
        workoutStore = new WorkoutStore();
        workoutStore.setCompletion(weeks);
    }

    // Workout tracking --------------------------------------------------------

    /** Workout timer reference, used for stopping the timer. */
    Timer workoutTimer;

    /** Creates a Timer which calls updateWorkoutState() each second. */
    void trackWorkoutState(final Couch25K midlet) {
        workoutTimer = new Timer();
        workoutTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                midlet.updateWorkoutState();
            }
        }, 0, 1000);
    }

    /** Index of the current workout step. */
    int currentStep;
    /** Configuration for the current workout step. */
    WorkoutStep step;
    /** Number of seconds elapsed during the current workout step. */
    int stepCounter;
    /** Number of seconds elapsed during the current workout. */
    int workoutCounter;

    /** Prepares to start tracking the currently-selected workout. */
    void resetWorkoutState() {
        currentStep = 0;
        stepCounter = 0;
        workoutCounter = 0;
        step = workout.steps[0];
        updateStepDisplay();
        updateProgressDisplay();
    }

    /**
     * Increments step and workout counters and updates display - it's expected
     * that this method will be called once a second while a Workour is in
     * progress.
     */
    public void updateWorkoutState() {
        workoutCounter++;
        stepCounter++;
        if (stepCounter >= step.duration) {
            if (currentStep == workout.steps.length - 1) {
                // Completed the last step
                finishWorkout();
                return;
            }

            // Advance to the next step
            currentStep++;
            step = workout.steps[currentStep];
            stepCounter = 0;
            updateStepDisplay();
        }
        updateProgressDisplay();
    }

    /**
     * Updates step display and resets the progress gauge for the current step.
     */
    void updateStepDisplay() {
        action.setText(step.action + " for " + secToDuration(step.duration));
        stepCount.setText((currentStep + 1) + " of " + workout.steps.length);
        stepProgress.setValue(0);
        stepProgress.setMaxValue(step.duration);
        if (currentStep > 0) {
            playSound(step.action.toLowerCase());
            display.vibrate(1000);
        }
    }

    /** Updates step and workout progress display. */
    void updateProgressDisplay() {
        stepProgress.setValue(stepCounter);
        stepTime.setText(secToTime(stepCounter));
        workoutProgress.setValue(workoutCounter);
        workoutTime.setText(secToTime(workoutCounter));
    }

    // MIDlet UI ---------------------------------------------------------------

    Display display;
    Image tickImage;
    Font boldUnderlinedFont;
    Font bigBoldFont;
    List selectWeekScreen;
    List selectWorkoutScreen;
    Form workoutSummaryScreen;
    ImageItem completedIcon;
    StringItem completedAt;
    StringItem intervalsLabel;
    Form workoutScreen;
    StringItem action;
    Gauge stepProgress;
    StringItem stepCount;
    StringItem stepTime;
    Gauge workoutProgress;
    StringItem workoutTime;
    Form workoutCompleteScreen;

    // Commands
    Command backCommand = new Command("Back", Command.BACK, 1);
    Command selectCommand = new Command("Select", Command.ITEM, 1);
    Command startCommand = new Command("Start", Command.SCREEN, 1);
    Command markCompleteCommand = new Command("Mark Complete", Command.SCREEN, 2);
    Command pauseCommand = new Command("Pause", Command.SCREEN, 1);
    Command resumeCommand = new Command("Resume", Command.SCREEN, 1);

    void initialiseUI() {
        display = Display.getDisplay(this);
        boldUnderlinedFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD | Font.STYLE_UNDERLINED, Font.SIZE_MEDIUM);
        bigBoldFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        tickImage = loadImage("tick");

        // Week selection screen
        selectWeekScreen = new List("Select Week", Choice.IMPLICIT);
        selectWeekScreen.setSelectCommand(selectCommand);
        selectWeekScreen.setCommandListener(this);

        // Workout selection screen
        selectWorkoutScreen = new List("Select Workout", Choice.IMPLICIT);
        selectWorkoutScreen.setSelectCommand(selectCommand);
        selectWorkoutScreen.addCommand(markCompleteCommand);
        selectWorkoutScreen.addCommand(backCommand);
        selectWorkoutScreen.setCommandListener(this);

        // Workout summary screen
        workoutSummaryScreen = new Form("");
        completedIcon = new ImageItem("Completed", tickImage, Item.LAYOUT_CENTER, null);
        completedAt = new StringItem(null, "");
        completedAt.setLayout(Item.LAYOUT_CENTER);
        intervalsLabel = new StringItem(null, "");
        intervalsLabel.setLayout(Item.LAYOUT_LEFT);
        intervalsLabel.setFont(boldUnderlinedFont);
        workoutSummaryScreen.addCommand(startCommand);
        workoutSummaryScreen.addCommand(backCommand);
        workoutSummaryScreen.setCommandListener(this);

        // Workout screen
        workoutScreen = new Form("");
        action = new StringItem(null, "");
        action.setFont(bigBoldFont);
        stepProgress = new Gauge(null, false, Gauge.INDEFINITE, 0);
        stepProgress.setLayout(Item.LAYOUT_EXPAND);
        stepCount = new StringItem(null, "");
        stepCount.setFont(bigBoldFont);
        stepCount.setLayout(Item.LAYOUT_EXPAND);
        stepTime = new StringItem(null, "");
        stepTime.setFont(bigBoldFont);
        workoutProgress = new Gauge("Workout", false, Gauge.INDEFINITE, 0);
        workoutProgress.setLayout(Item.LAYOUT_EXPAND);
        workoutTime = new StringItem(null, "");
        workoutTime.setLayout(Item.LAYOUT_RIGHT);
        workoutScreen.append(action);
        workoutScreen.append(stepProgress);
        workoutScreen.append(stepCount);
        workoutScreen.append(stepTime);
        workoutScreen.append(workoutProgress);
        workoutScreen.append(workoutTime);
        workoutScreen.addCommand(pauseCommand);
        workoutScreen.setCommandListener(this);

        // Workout completion screen
        workoutCompleteScreen = new Form("Workout Complete");
        workoutCompleteScreen.addCommand(backCommand);
        workoutCompleteScreen.setCommandListener(this);
    }

    // State transitions -------------------------------------------------------

    void init() {
        selectWeekScreen.deleteAll();
        for (int i = 0; i < weeks.length; i++) {
            selectWeekScreen.append(
                "Week " + (i + 1),
                (weeks[i].isCompleted() ? tickImage : null));
        }
        display.setCurrent(selectWeekScreen);
        state = STATE_SELECT_WEEK;
    }

    void selectWeek() {
        selectedWeek = selectWeekScreen.getSelectedIndex();
        week = weeks[selectedWeek];

        selectWorkoutScreen.deleteAll();
        for (int i = 0; i < week.workouts.length; i++) {
            selectWorkoutScreen.append(
                "Workout 1 - " + secToTime(week.workouts[i].totalDuration),
                (week.completedAt[i] != null ? tickImage : null));
        }
        display.setCurrent(selectWorkoutScreen);
        state = STATE_SELECT_WORKOUT;
    }

    void selectWorkout() {
        selectedWorkout = selectWorkoutScreen.getSelectedIndex();
        workout = week.workouts[selectedWorkout];

        workoutSummaryScreen.setTitle("Week " + (selectedWeek + 1) +
                                      " - Workout " + (selectedWorkout + 1));
        workoutSummaryScreen.deleteAll();
        // Completion details
        if (week.completedAt[selectedWorkout] != null) {
            workoutSummaryScreen.append(completedIcon);
            completedAt.setText(formatDate(week.completedAt[selectedWorkout]));
            workoutSummaryScreen.append(completedAt);
        }
        // Workout steps
        intervalsLabel.setText(workout.steps.length + " Intervals\n");
        workoutSummaryScreen.append(intervalsLabel);
        for (int i = 0; i < workout.steps.length; i++) {
            StringItem stepDesc = new StringItem(null,
                workout.steps[i].action + " for " +
                secToDuration(workout.steps[i].duration) + "\n");
            workoutSummaryScreen.append(stepDesc);
        }
        display.setCurrent(workoutSummaryScreen);
        state = STATE_WORKOUT_SELECTED;
    }

    void startWorkout() {
        resetWorkoutState();

        workoutScreen.setTitle("Week " + (selectedWeek + 1) +
                               " - Workout " + (selectedWorkout + 1));
        workoutProgress.setMaxValue(workout.totalDuration);
        workoutProgress.setValue(0);
        display.setCurrent(workoutScreen);
        trackWorkoutState(this);
        state = STATE_WORKOUT;
    }

    void pauseWorkout() {
        workoutTimer.cancel();

        workoutScreen.removeCommand(pauseCommand);
        workoutScreen.addCommand(resumeCommand);
        state = STATE_WORKOUT_PAUSED;
    }

    void resumeWorkout() {
        workoutScreen.removeCommand(resumeCommand);
        workoutScreen.addCommand(pauseCommand);
        trackWorkoutState(this);
        state = STATE_WORKOUT;
    }

    void finishWorkout() {
        workoutTimer.cancel();
        week.completedAt[selectedWorkout] =
            workoutStore.completeWorkout(selectedWeek, selectedWorkout);

        display.setCurrent(workoutCompleteScreen);
        playSound("finished");
        state = STATE_WORKOUT_COMPLETE;
    }

    // Utilities ---------------------------------------------------------------

    /** Plays an MP3 file once. */
    void playSound(String action) {
        try {
            InputStream in = getClass().getResourceAsStream("/" + action + ".mp3");
            Player player = Manager.createPlayer(in, "audio/mpeg");
            player.addPlayerListener(this);
            player.start();
        } catch (MediaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Loads an image file as an immutable Image. */
    Image loadImage(String name) {
        Image result = null;
        try {
            InputStream in = getClass().getResourceAsStream("/" + name + ".png");
            result = Image.createImage(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /** Formats seconds as a duration description. */
    String secToDuration(int n) {
        if (n <= 90) {
            return n + " seconds";
        }
        int min = n / 60;
        int sec = n % 60;
        if (sec == 0) {
          return min + " minutes";
        }
        // Spare seconds in Couch-to-5k intervals all round nicely
        return (min + (float)sec/60) + " minutes";
    }

    /** Formats seconds as a time in MM:SS format. */
    String secToTime(int n) {
        int minutes = n / 60;
        int seconds = n % 60;
        return pad(minutes) + ":" + pad(seconds);
    }

    static final String[] DAYS = new String[] {
        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    static final String[] MONTHS = new String[] {
        "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
    static final String[] ORDINALS = new String[] {
        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
    String formatDate(Date date) {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(date);
        StringBuffer sb = new StringBuffer();
        sb.append(pad(cal.get(Calendar.HOUR))).append(":")
          .append(pad(cal.get(Calendar.MINUTE))).append(", ")
          .append(DAYS[cal.get(Calendar.DAY_OF_WEEK)]).append(" ")
          .append(cal.get(Calendar.DAY_OF_MONTH))
          .append(ORDINALS[cal.get(Calendar.DAY_OF_MONTH) % 10]).append(" ")
          .append(MONTHS[cal.get(Calendar.MONTH)]);
        if (cal.get(Calendar.YEAR)!= currentYear ) {
            sb.append(" ").append(cal.get(Calendar.YEAR));
        }
        return sb.toString();
    }

    /** Pads a number with a leading zero if necessary. */
    String pad(int n) {
        if (n < 10) {
            return "0" + n;
        }
        return "" + n;
    }
    // MIDlet API --------------------------------------------------------------

    /** Initialises or resumes the applcation as approriate. */
    protected void startApp() throws MIDletStateChangeException {
        if (display == null) {
            initialiseState();
            initialiseUI();
        }

        if (state == STATE_WORKOUT_PAUSED) {
            resumeWorkout();
        } else {
            init();
        }
    }

    /** Pauses workout tracking if it is currently active. */
    protected void pauseApp() {
        if (state == STATE_WORKOUT) {
            pauseWorkout();
        }
    }

    protected void destroyApp(boolean unconditional)
        throws MIDletStateChangeException {
        workoutStore.close();
    }

    // CommandListener API -----------------------------------------------------

    /**
     * Calls the appropriate transition method based on the current state and
     * the command which was given.
     */
    public void commandAction(Command c, Displayable d) {
        switch (state) {
        case STATE_SELECT_WEEK:
            if (c == selectCommand) selectWeek();
            break;
        case STATE_SELECT_WORKOUT:
            if (c == selectCommand) selectWorkout();
            if (c == backCommand) init();
            if (c == markCompleteCommand) {
                int selectedWorkout = selectWorkoutScreen.getSelectedIndex();
                week.completedAt[selectedWorkout] =
                    workoutStore.completeWorkout(selectedWeek, selectedWorkout);
                // Redraw lazily
                selectWeek();
                selectWorkoutScreen.setSelectedIndex(selectedWorkout, true);
            }
            break;
        case STATE_WORKOUT_SELECTED:
            if (c == startCommand) startWorkout();
            if (c == backCommand) selectWeek();
            break;
        case STATE_WORKOUT:
            if (c == pauseCommand) pauseWorkout();
            break;
        case STATE_WORKOUT_PAUSED:
            if (c == resumeCommand) resumeWorkout();
            break;
        case STATE_WORKOUT_COMPLETE:
            if (c == backCommand) init();
            break;
        }
    }

    // PlayerListener API ------------------------------------------------------

    /**
     * Close every Player we create once it reaches the end of its media, to
     * free up resources.
     */
    public void playerUpdate(Player player, String event, Object eventData) {
        if (event == PlayerListener.END_OF_MEDIA) {
            player.close();
        }
    }
}
