package ua.edu.sumdu.playback;

public class PlayerContextHolder {
    
    private static Player player;

    public static synchronized Player getPlayer() {
        return Player.getInstance();
    }
}
