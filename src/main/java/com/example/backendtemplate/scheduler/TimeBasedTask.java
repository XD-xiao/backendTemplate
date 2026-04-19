package com.example.backendtemplate.scheduler;

import com.example.backendtemplate.Utils.EmailUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class TimeBasedTask {


    private final EmailUtil emailUtil = new EmailUtil();

    @Scheduled(fixedRate = 30000) // 每分钟检查一次
    public void checkDatabaseTime() {

    }

    private void performActionIfTimeMatches() {

    }
}