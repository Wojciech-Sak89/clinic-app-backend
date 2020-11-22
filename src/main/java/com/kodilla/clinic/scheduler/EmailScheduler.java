package com.kodilla.clinic.scheduler;

import com.kodilla.clinic.config.AdminConfig;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.domain.mail.Mail;
import com.kodilla.clinic.service.DbService;
import com.kodilla.clinic.service.SimpleEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EmailScheduler {
    private static final String APPOINTMENT_REMINDER = "SUPER CLINIC appointment reminder";
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailScheduler.class);

    @Autowired
    private SimpleEmailService simpleEmailService;

    @Autowired
    private DbService dbService;

    @Scheduled(cron = "0 0 14 ? * MON,TUE,WED,THU,SUN")
//    @Value("cron = \"0 0 10 * * *\"")
//    @Scheduled(cron = "0 0 10 * * *")
//    @Scheduled(fixedDelay = 5000)
    public void sendInformationEmail() {
        LOGGER.info("Starting sendInformationEmail()");

        List<Appointment> appointments = dbService.getForthcomingAppointments();
        System.out.println(appointments.get(0).getPatient().getEmail());

        appointments.stream()
                .filter(appointment -> appointment.getDateTime().isAfter(LocalDateTime.now().plusHours(12)))
                .forEach(appointment -> {
                    Patient currPatient = appointment.getPatient();
                    simpleEmailService.send(new Mail(
                            currPatient.getEmail(),
                            APPOINTMENT_REMINDER,
                            "Dear " + currPatient.getName() + " " + currPatient.getSurname() + "," +
                                    "\nWe would like to remind you that your visit to "
                                    + appointment.getDoctor().getSurname() +
                                    " starts tomorrow at " + appointment.getDateTime().getHour()
                                                            + ":" + appointment.getDateTime().getMinute() +
                                    ". Please try to be at our clinic at least 5 minutes prior to appointment." +
                                    "\n\nBest regards," +
                                    " \nSUPER CLINIC TEAM",
                            ""
                    ));
                });
    }
}