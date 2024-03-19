# Hamster Backend

This is the API Background where our frontend framework (= VueJS) sends request to.

## Installation guide for developer (not using docker) to edit program

- git clone https://github.com/S1m0K/Hamster_Backend
- open maven spring project in your IDE
- run ProjectWebsiteApplication 

You can apply some changes in settings.properties, but be careful! You might need to reinstall your database!

## Credentials

Standard credentials to log in with admin permissions:  
username: admin  
password: admin  

Should be changed when publishing it!

## Rest Services

All API-endpoints for the course system are documented in the file `api-guide.html`.

### Test

The course system functionality can be tested via the unit tests at `src/test/java/at.ac.htlinn.hamsterbackend.courseManagement`