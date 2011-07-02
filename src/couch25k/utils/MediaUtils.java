package couch25k.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

public class MediaUtils {
    /** Plays an MP3 file once. */
    public static void playSound(String action) {
        try {
            InputStream in =
                MediaUtils.class.getResourceAsStream("/" + action + ".mp3");
            Player player = Manager.createPlayer(in, "audio/mpeg");
            player.addPlayerListener(new ClosePlayerListener());
            player.start();
        } catch (MediaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Loads an image file as an immutable Image. */
    public static Image loadImage(String name) {
        Image result = null;
        try {
            InputStream in =
                MediaUtils.class.getResourceAsStream("/" + name + ".png");
            result = Image.createImage(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class ClosePlayerListener implements PlayerListener {
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
}
