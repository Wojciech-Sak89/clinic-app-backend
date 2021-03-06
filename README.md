# clinic-app-backend
REST API for private clinic. Diploma project prepared during Kodilla bootcamp course.

## Startup manual
0. You might need to install node.js for global use on your PC for Vaadin (frontend framework) to work properly. You can get it here: https://nodejs.org/en/download/
1. Download and run clinic-app-backend from this repository.
2. Download and run clinic-app-frontend. Repository link: https://github.com/Wojciech-Sak89/clinic-app-frontend
    2.1 In case of any problems with com.vaadin.flow.server, start dev server or node_modules delete \node_modules folder and using terminal try commands `npm install` and `npm install webpack-dev-server -g`.
3. Run http://localhost:8081/ on your browser.
    
4. Clinic App is ready to use!

## Clinic App description
 The application consists of two parts: one for the Administrator and the other for the ordinary user (the clinic Patient).
 
 The Administrator can manually add, edit and delete patients, doctors, appointments, staff evaluations. The administrator can also add and modify doctors' schedules, separately modifying working hours for each day and adding emergency hours for urgent patients.
 
 Patients can get recommendations on what specialists to go to based on their symptoms. They can also evaluate and give opinions to doctors.
 The most important functionality of this application is the ability to sign up for visits to doctors and to cancel them.
 
 ## Testing remarks
 To test the `PATIENT - Opinion` and `PATIENT - Book doctor appointment` sections on the frontend, it is required to create at least one patient (in the `ADMIN - Patients` section) with a PESEL number used to be able to log in.
 
 In addition, to fully test the `PATIENT - Book doctor appointment` section, you must create at least one doctor in the `ADMIN - Doctors` section and create a visit for that doctor in the `ADMIN - Appointments` section without assigning a patient to it and setting the appointment status as `OPEN`.
 
 To fully test the automatic sending of e-mails to patients (with tomorrow's appointment reminder and weather forecast), create a patient with an e-mail address to which you have access (in the `ADMIN - Patients` section), and an appointment for this patient (in the `ADMIN - Appointments` section) that will have place the next day (between 12 and 32 hours after running the application) and with the status `RESERVED` or `FOR_EMERGENCY_ONLY`.
 
 Then in the `application.properties` on the backend side, configure the `# EMAIL CONFIGURATION` section  with your email details (allowing less secure applications to access your email account), and in the `com.kodilla.clinic.scheduler.EmailScheduler` class, uncomment `@Scheduled (fixedDelay = 10000)` and comment `@Scheduled (cron = "0 0 14? * MON, TUE, WED, THU, SUN")`. After starting, the application should send an e-mail with a reminder to patient every 10 seconds.
 
 ## Kodilla requirements
 1. REST endpoints
     - Create at least 20 different endpoints.
     - Use each of the known HTTP methods at least once: GET, POST, PUT and DELETE
 2. Downloading data
     - Use at least two external data sources.
 3. Scheduler
     - Design and implement at least one scheduler usage.
 4. Save records to database
     - Implement at least 10 different data writing operations to the database.
 5. Tests
     - Create tests covering at least 65% of the code.
 6. Design Patterns
     - Use at least two different design patterns.
 7. View layer
     - Use the Vaadin library to create a view layer of your application.
   
 ### Ad. 1
   The system consists of 37 endpoints ready to use. Frontend effectively uses at least 20 of them with all known HTTP methods used at least once: GET, POST, PUT and DELETE.
 
 ### Ad. 2
   System uses:
   1. Open weather API (https://openweathermap.org/api) - clinic patient at 2 PM a day before his visit recieves an email message with weather forecast for time and place of his      visit.
   2. API Medic (https://apimedic.com/) at subpage `PATIENT - Specialist recommendation` (http://localhost:8081/specialistRecommendation) patient can get a recommendation to         which doctor he or she should sign up for based on his or hers symptoms.
 
 ### Ad. 3
   Scheduler is used to send emails with reminder messages every day before working day at 2PM for those patients who have visit next day.
 
 ### Ad. 4
  The system consists of 7 entities which gives the minimum of 14 data writing operations to the database (creation and edition).
 
 ### Ad. 5
   Tests cover 86% of classes, 73% of methods and 69% of lines.
 
 ### Ad. 6
 System uses 3 design patterns:
 1. Factory (backend: package com.kodilla.clinic.domain.schedule.factory.ScheduleFactory)
 2. Builder (backend: package com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule)
 3. Singleton (backend: com.kodilla.clinic.outerapi.weather.forecast.personalized.PersonalizedForecast)
 
 ### Ad. 7
   Applied at https://github.com/Wojciech-Sak89/clinic-app-frontend