package com.kodilla.clinic.scheduler;

import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.domain.mail.Mail;
import com.kodilla.clinic.service.DbService;
import com.kodilla.clinic.service.SimpleEmailService;
import com.kodilla.clinic.service.WeatherService;
import com.kodilla.clinic.outerapi.weather.forecast.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private WeatherService weatherService;

    @Scheduled(cron = "0 0 14 ? * MON,TUE,WED,THU,SUN")
//    @Scheduled(fixedDelay = 5000)
    public void sendInformationEmail() {
        LOGGER.info("Starting sendInformationEmail()");

        List<Appointment> appointments = dbService.getForthcomingAppointments();
        System.out.println(appointments.get(0).getPatient().getEmail());

        appointments.stream()
                .filter(appointment -> appointment.getDateTime().isAfter(LocalDateTime.now().plusHours(12)))
                .filter(appointment -> appointment.getDateTime().isBefore(LocalDateTime.now().plusHours(32)))
                .forEach(appointment -> {
                    Patient currPatient = appointment.getPatient();
                    WeatherData appointmentWeather =
                            weatherService.getPersonalizedForecast(appointment.getDateTime());

//                    System.out.println("################### APPOINTMENT WEATHER: " + appointmentWeather);
//                    System.out.println("################### VISIT TIME: " + appointment.getDateTime());
//                    System.out.println("################### VISIT TIME STAMP: " + appointment.getDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

                    simpleEmailService.send(new Mail(
                            currPatient.getEmail(),
                            APPOINTMENT_REMINDER,
                            "Dear " + currPatient.getName() + " " + currPatient.getSurname() + "," +
                                    "\nWe would like to remind you that your visit to Dr "
                                    + appointment.getDoctor().getSurname() +
                                    " starts tomorrow at " + appointment.getDateTime().getHour()
                                    + ":" + appointment.getDateTime().getMinute() +
                                    ". Please try to be at our clinic at least 5 minutes prior to appointment." +

                                    "\n\nWeather Forecast near your visit time at our clinic:\n" +
                                    "\t You can expect " + appointmentWeather.getWeather().get(0).getDescription() + ".\n" +
                                    "\t Temperature: " + appointmentWeather.getTemperature().getTemperature() + " \u2103.\n" +

                                    "\n\nBest regards," +
                                    " \nSUPER CLINIC TEAM",
                            ""
                    ));


                });
    }
}