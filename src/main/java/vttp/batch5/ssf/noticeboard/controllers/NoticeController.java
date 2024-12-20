package vttp.batch5.ssf.noticeboard.controllers;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers
@Controller
@RequestMapping
public class NoticeController {

    @Autowired 
    private NoticeService noticeSvc;

    private static final Logger logger = Logger.getLogger(NoticeController.class.getName());

    @GetMapping("/")
    public String landing(Model model) {
        model.addAttribute("notice", new Notice());
        return "notice";
    }

    @PostMapping("/notice")
    public String postNotice(Model model, 
        @Valid @ModelAttribute Notice notice,
        BindingResult bindings) {
            
        if(bindings.hasErrors())
            return "notice";
        
        String message = noticeSvc.postToNoticeServer(notice);
        if(message.split(" ").length > 1) {
            logger.info("[Controller] Error posting to service");
            model.addAttribute("error", message);
            return "view3";
        }
        logger.info("[Controller] Successful post");
        model.addAttribute("id", message);
        return "view2";
    }

    @GetMapping(path="/status", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> checkStatus() {
        return noticeSvc.checkHealth();
    }

}
