/**
 * Workout intervals from http://www.coolrunning.com/engine/2/2_3/181.shtml
 */
public class Workouts {
    public static Week[] getWorkouts() {
        Week[] weeks = new Week[9];

        Workout week1Workout = new Workout(new int[] {
            300, 60, 90, 60, 90, 60, 90, 60, 90, 60, 90, 60, 90, 60, 90, 60, 90
        });
        weeks[0] = new Week(new Workout[] {
            week1Workout, week1Workout, week1Workout
        });

        Workout week2Workout = new Workout(new int[] {
            300, 90, 120, 90, 120, 90, 120, 90, 120, 90, 120, 90, 120
        });
        weeks[1] = new Week(new Workout[] {
            week2Workout, week2Workout, week2Workout
        });

        Workout week3Workout = new Workout(new int[] {
            300, 90, 90, 180, 180, 90, 90, 180, 180
        });
        weeks[2] = new Week(new Workout[] {
            week3Workout, week3Workout, week3Workout
        });

        Workout week4Workout = new Workout(new int[] {
            300, 180, 90, 300, 150, 180, 90, 300
        });
        weeks[3] = new Week(new Workout[] {
            week4Workout, week4Workout, week4Workout
        });

        weeks[4] = new Week(new Workout[] {
            new Workout(new int[] {
                300, 300, 180, 300, 180, 300
            }),
            new Workout(new int[] {
                300, 480, 300, 480
            }),
            new Workout(new int[] {
                300, 1200
            })
        });

        weeks[5] = new Week(new Workout[] {
            new Workout(new int[] {
                300, 300, 180, 480, 180, 300
            }),
            new Workout(new int[] {
                300, 600, 180, 600
            }),
            new Workout(new int[] {
                300, 1500
            })
        });

        Workout week7Workout = new Workout(new int[] {
            300, 1500
        });
        weeks[6] = new Week(new Workout[] {
            week7Workout, week7Workout, week7Workout
        });


        Workout week8Workout = new Workout(new int[] {
            300, 1680
        });
        weeks[7] = new Week(new Workout[] {
            week8Workout, week8Workout, week8Workout
        });

        Workout week9Workout = new Workout(new int[] {
            300, 1800
        });
        weeks[8] = new Week(new Workout[] {
            week9Workout, week9Workout, week9Workout
        });

        return weeks;
    }
}
