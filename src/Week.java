import java.util.Date;

public class Week {
    public Workout[] workouts;
    public Date[] completedAt;

    public Week(Workout[] workouts) {
        this.workouts = workouts;
        this.completedAt = new Date[workouts.length];
    }

    public boolean isCompleted() {
        for (int i = 0; i < completedAt.length; i++) {
            if (completedAt[i] == null) {
                return false;
            }
        }
        return true;
    }

    public int firstIncompleteIndex() {
        for (int i = 0; i < completedAt.length; i++) {
            if (completedAt[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
