package ua.edu.sumdu.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.edu.sumdu.domain.FileTreeNode;
import ua.edu.sumdu.domain.Mp3File;
import ua.edu.sumdu.domain.UploadItem;
import ua.edu.sumdu.model.SimpleResponse;
import ua.edu.sumdu.model.SimpleResponseImpl;
import ua.edu.sumdu.service.Mp3FileService;
//import ua.edu.sumdu.service.TrackService;
import ua.edu.sumdu.service.TrackService;
import ua.edu.sumdu.util.Mp3FileUtils;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController {

    @Value("${ftpserver.user.admin.homedirectory}")
    String trackPath;

    @Autowired
    Mp3FileService mp3FileService;

    @Autowired
    TrackService trackService;

    Logger logger = LoggerFactory.getLogger(FileController.class);
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public class FileUploadException extends IOException {
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public FileTreeNode getAllFiles(){
        return mp3FileService.getFileTree();
    }

    @RequestMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public void updateFiles(UploadItem uploadItem) throws FileUploadException, UnsupportedEncodingException {
        try {
            MultipartFile file = uploadItem.getFile();
            String filename = new String(file.getOriginalFilename().getBytes(), "UTF-8");
            File tracksDir = new File(trackPath);
            if (!tracksDir.exists())
                tracksDir.mkdirs();
            IOUtils.copy(uploadItem.getFile().getInputStream(),
                    new FileOutputStream(trackPath + File.pathSeparator + filename));
            mp3FileService.createMp3File(filename, "");
        } catch (IOException e) {
            throw new FileUploadException();
        } catch (UnsupportedAudioFileException e) {
            throw new FileUploadException();
        }
    }

    @RequestMapping(value = "/{fileId}", method = RequestMethod.DELETE)
    @ResponseBody
    public SimpleResponse deleteFile(@PathVariable Integer fileId) {
        try {
            trackService.deleteTracksByFile(fileId);
            return new SimpleResponseImpl(true);
        } catch (Exception e) {
            logger.error("Couldn't delete file ID:"+fileId, e);
        }
        return new SimpleResponseImpl(false);
    }

}
