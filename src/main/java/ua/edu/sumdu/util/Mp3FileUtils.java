package ua.edu.sumdu.util;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import sun.misc.BASE64Encoder;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Mp3FileUtils {

    public static Long getDuration(File file) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
        return (Long) baseFileFormat.properties().get("duration");
    }

}
