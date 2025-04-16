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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
		if (startDate != null && startDate.isEmpty())
			startDate = null;
		if (endDate != null && endDate.isEmpty())
			endDate = null;

		List<LogEntity> logs = logService.searchLogs(level, startDate, endDate);

		ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		for (int i = 0; i < logs.size(); i++) {
			LogEntity log = logs.get(i);
			String message = log.getMessage();
			try {
				if (message != null && message.trim().startsWith("{")) {
					String pretty = objectMapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(objectMapper.readTree(message));
					logs.set(i, log.toBuilder().message(pretty).build());
				}
			} catch (Exception e) {

			}
		}

		model.addAttribute("logs", logs);
		model.addAttribute("level", level);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);

		return "logs";
	}
}