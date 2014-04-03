package ua.edu.sumdu.controller;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.domain.Direction;
import ua.edu.sumdu.domain.Mp3File;
import ua.edu.sumdu.domain.Track;
import ua.edu.sumdu.model.SimpleResponse;
import ua.edu.sumdu.model.SimpleResponseImpl;
import ua.edu.sumdu.model.TrackListResponse;
import ua.edu.sumdu.model.TrackResponse;
import ua.edu.sumdu.service.Mp3FileService;
import ua.edu.sumdu.service.TrackService;
import ua.edu.sumdu.util.DateUtils;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/tracks")
public class TrackController {

    protected static Logger logger = LoggerFactory.getLogger(TrackController.class);

    protected SimpleDateFormat dateFormat = DateUtils.getDateFormat();

    @Value("${ftpserver.user.admin.homedirectory}")
    String trackPath;

    @Autowired
    Mp3FileService mp3FileService;

    @Autowired
    TrackService trackService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public TrackListResponse getTracks(@RequestParam(value = "date", defaultValue = "") String dateString) {
        Date date = null;
        if (!dateString.isEmpty()) {
            try {
                date = DateUtils.getDateFormat().parse(dateString);
            } catch (ParseException e) {
                /* ignore */
            }
        }
        if (date == null) date = DateUtils.clearDate(new Date());
        return new TrackListResponse(trackService.getWeekTracks(date));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public TrackResponse addTrack(@ModelAttribute Track track) {
        try {
            trackService.addTrack(track);
            return new TrackResponse(track);
        } catch (HibernateException e) {
            logger.error("File add error", e);
        }
        return new TrackResponse();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TrackResponse getTrack(@PathVariable("id") Integer trackId) {
        return new TrackResponse(trackService.getTrack(trackId));
    }

    @RequestMapping(value = "/{trackId}", method = RequestMethod.DELETE)
    @ResponseBody
    public SimpleResponse removeTrack(@PathVariable Integer trackId) {
        try {
            trackService.deleteTrack(trackId);
            return new SimpleResponseImpl(true);
        } catch (HibernateException e) {
            return new SimpleResponseImpl(false);
        }
    }

    @RequestMapping("/move{direction}/{trackId}")
    @ResponseBody
    public SimpleResponse moveTrack(@PathVariable Direction direction, @PathVariable Integer trackId) {
        trackService.moveTrack(trackId, direction);
        return new SimpleResponseImpl(true);
    }

    @RequestMapping("/play/{fileId}")
    public void getAudioTrack(@PathVariable Integer fileId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Mp3File mp3File = mp3FileService.getMp3File(fileId);
        File file = new File(trackPath + mp3File.getFilename());
        if (file.exists() && file.canRead()) {
            response.setContentType(new MimetypesFileTypeMap().getContentType(file));
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(fileStream, response.getOutputStream());
            fileStream.close();
        } else {
            response.sendError(500);
        }
    }

}
