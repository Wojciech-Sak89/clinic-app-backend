package com.kodilla.clinic.scheduler;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SchedulerSettings {
    @Value("cron = \"0 0 14 ? * SUN,MON,TUE,WED,THU *\"")
    private String fromSundayToThursdayAtTwoPM;
}
