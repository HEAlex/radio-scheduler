package ua.edu.sumdu.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ua.edu.sumdu.dao.TrackDao;
import ua.edu.sumdu.playback.PlayerContextHolder;
import ua.edu.sumdu.util.DateUtils;

import java.util.Date;

/**
 * A synchronous worker
 */
public class TracksPlayer {

	private static Logger logger = LoggerFactory.getLogger(TracksPlayer.class);

    private int dayBreak;

    @Value("${ftpserver.user.admin.homedirectory}")
    String trackPath;

    @Autowired
    TrackDao trackDao;

    public void play() {
        logger.debug("Starting to playback.");
        String[] tracks = trackDao.getTracks(DateUtils.clearDate(new Date()), dayBreak);
        PlayerContextHolder.getPlayer().play(trackPath, tracks);
	}

    public int getDayBreak() {
        return dayBreak;
    }

    public void setDayBreak(int dayBreak) {
        this.dayBreak = dayBreak;
    }
}
