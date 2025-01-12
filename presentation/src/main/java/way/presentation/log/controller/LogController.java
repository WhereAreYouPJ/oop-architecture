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

@Controller
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("/admin/logs")
    public String getLogs(
            @RequestParam(value = "level", required = false) Integer level,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<LogEntity> logPage = logService.searchLogs(level, startDate, endDate, pageRequest);

        model.addAttribute("logs", logPage.getContent());
        model.addAttribute("totalPages", logPage.getTotalPages());
        model.addAttribute("currentPage", logPage.getNumber());
        model.addAttribute("level", level);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "logs";
    }
}