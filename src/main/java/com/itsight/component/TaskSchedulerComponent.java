package com.itsight.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskSchedulerComponent {
//    private static final Logger LOGGER = LogManager.getLogger(TaskSchedulerComponent.class);

//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//    
//    @Autowired
//    private EmailService emailService;
    
	@Scheduled(cron = "0 0 9-23 * * SUN-SAT")
	public void printTimeInSpecificMoment() {
//		LOGGER.info("The time is now {}", dateFormat.format(new Date()));
//		emailService.sendSimpleMessage();
	}

}
