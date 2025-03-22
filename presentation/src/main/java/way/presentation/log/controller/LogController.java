package way.presentation.log.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import way.application.utils.log.entity.LogEntity;
import way.application.utils.log.service.LogService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("/admin/logs")
    public String getLogs(
            @RequestParam(value = "level", required = false) Integer level,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            Model model
    ) {

        startDate = startDate.isEmpty() ? null : startDate;
        endDate = endDate.isEmpty() ? null : endDate;


        List<LogEntity> logs = logService.searchLogs(level, startDate, endDate);

        model.addAttribute("logs", logs);
        model.addAttribute("level", level);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "logs";
    }
}