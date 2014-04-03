package ua.edu.sumdu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.domain.Mp3File;
import ua.edu.sumdu.model.SimpleResponse;
import ua.edu.sumdu.model.SimpleResponseImpl;
import ua.edu.sumdu.service.Mp3FileService;
//import ua.edu.sumdu.service.TrackService;
import ua.edu.sumdu.service.TrackService;
import ua.edu.sumdu.util.DateUtils;

import java.util.*;

@Controller
@RequestMapping("/")
public class MainController {

    protected static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    TrackService trackService;

    @Autowired
    Mp3FileService mp3FileService;

    @Value("${breaks}")
    private String sBreaks;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtils.getDateFormat(), false));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getCommonPage() {
        return "default";
    }

    @RequestMapping(value = "controls")
    public String getControlPage(Model model) {
        Date date = DateUtils.clearDate(new Date());
        getWeekTemplate(date, model);
        return "controls";
    }

    @RequestMapping(value = "resync")
    @ResponseBody
    public SimpleResponse resyncFiles(Model model) {
        mp3FileService.updateDatabase();
        return new SimpleResponseImpl(true);
    }

    @RequestMapping(value = "week")
    public String getWeekTemplate(@RequestParam Date date, Model model) {
        date = DateUtils.clearDate(date);
        Date startDate = DateUtils.getWeekStartDate(date);
        Date endDate = DateUtils.getWeekEndDate(date);
        model.addAttribute("files", createMp3FileMap(mp3FileService.getAllMp3Files()));
        model.addAttribute("days", DateUtils.getWeekDays(date));
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("tracks", trackService.getTrackCalendar(startDate));
        model.addAttribute("breaks", sBreaks.split("\\|"));
        return "week";
    }

    private static Map<Integer, Mp3File> createMp3FileMap(List<Mp3File> mp3Files) {
        Map<Integer, Mp3File> res = new HashMap<Integer, Mp3File>(mp3Files.size());
        for (Mp3File file : mp3Files) {
            res.put(file.getId(), file);
        }
        return res;
    }
}
