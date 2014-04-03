package ua.edu.sumdu.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.QuartzJobBean;
import ua.edu.sumdu.playback.PlayerContextHolder;

public class CustomJob extends QuartzJobBean implements StatefulJob {

	protected static Logger logger = LoggerFactory.getLogger(CustomJob.class);
	private TracksPlayer player;
	private int dayBreak;
    private TaskType task = TaskType.PLAY;

    @Override
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
        switch (task) {
            case PLAY: {
                logger.debug("Start play event");
                final TracksPlayer pl = player;
                new Runnable() {
                    @Override
                    public void run() {
                        player.setDayBreak(dayBreak);
                        player.play();
                    }
                }.run();
                logger.debug("End play event");
                break;
            } case STOP: {
                logger.debug("Start stop event");
                PlayerContextHolder.getPlayer().stop();
                logger.debug("End stop event");
            }
        }
	}
    
	public void setPlayer(TracksPlayer player) {
		this.player = player;
	}
    public void setDayBreak(int dayBreak) {
        this.dayBreak = dayBreak;
    }
    public void setTask(TaskType task) {
        this.task = task;
    }
    public static enum TaskType {
        PLAY, STOP
    }
}
