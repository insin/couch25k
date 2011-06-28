import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class WorkoutStore {
    private RecordStore recordStore;

    public WorkoutStore() {
        try {
            recordStore = RecordStore.openRecordStore("workouts", true);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }

    /** Records a workout's completion date and returns it. */
    public Date completeWorkout(int week, int workout) {
        Date completionDate = new Date();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);
        try {
            outputStream.writeInt(week);
            outputStream.writeInt(workout);
            outputStream.writeLong(completionDate.getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add the record
        byte[] b = baos.toByteArray();
        try {
            recordStore.addRecord(b, 0, b.length);
        }
        catch (RecordStoreException e) {
            e.printStackTrace();
        }
        return completionDate;
    }

    /** Retrieves completion details and adds to configuration objects. */
    public void setCompletion(Week[] weeks) {
        try {
            RecordEnumeration re = recordStore.enumerateRecords(null, null, false);
            while (re.hasNextElement()) {
                byte[] record = re.nextRecord();
                ByteArrayInputStream bais = new ByteArrayInputStream(record);
                DataInputStream inputStream = new DataInputStream(bais);
                int week = inputStream.readInt();
                int workout = inputStream.readInt();
                Date completedAt = new Date(inputStream.readLong());
                weeks[week].completedAt[workout] = completedAt;
            }
        } catch (RecordStoreNotOpenException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Closes the record store. */
    public void close() {
        try {
            recordStore.closeRecordStore();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }
}
