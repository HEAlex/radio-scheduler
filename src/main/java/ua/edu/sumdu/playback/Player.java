package ua.edu.sumdu.playback;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class  Player {
    private static Logger logger = LoggerFactory.getLogger(Player.class);
    private static Player self;
    private volatile boolean needBreak;

    private AdvancedPlayer player;

    private Player() {
    }

    public static synchronized Player getInstance() {
        if (self == null) {
            self = new Player();
        }
        return self;
    }

    public void play(final String filename) throws FileNotFoundException, JavaLayerException {
        FileInputStream fis = new FileInputStream(filename);
        BufferedInputStream bis = new BufferedInputStream(fis);
        player = new AdvancedPlayer(bis);
        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackStarted(PlaybackEvent playbackEvent) {
                logger.info("+Start playing track " + filename);
            }

            @Override
            public void playbackFinished(PlaybackEvent playbackEvent) {
                logger.info("-Stop playing track " + filename);
            }
        });
        player.play();
    }

    public void stop() {
        needBreak = true;
        if (player != null) {
            player.stop();
            player.close();
        }
    }

    public void play(String trackPath, String[] tracks) {
        needBreak = false;
        try {
            for (String filename : tracks) {
                play(trackPath + filename);
                if (needBreak)
                    break;
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        } catch (JavaLayerException e) {
            logger.error("File is not correct mp3 file", e);
        }
    }
}
