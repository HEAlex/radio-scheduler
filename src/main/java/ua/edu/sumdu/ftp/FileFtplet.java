package ua.edu.sumdu.ftp;


import org.apache.ftpserver.ftplet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ua.edu.sumdu.domain.Mp3File;
import ua.edu.sumdu.service.Mp3FileService;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class FileFtplet extends DefaultFtplet {

    Logger logger = LoggerFactory.getLogger(FileFtplet.class);

    @Autowired
    private Mp3FileService mp3FileService;

    @Override
    public FtpletResult onRenameEnd(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        return null;
    }
    @Override
    public FtpletResult onRmdirStart(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        return null;

    }

    @Override
    public FtpletResult onDeleteEnd(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        return null;
    }

    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request)
                                            throws FtpException, IOException {
        return null;
    }

}
