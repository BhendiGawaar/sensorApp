# sensorApp

## Acceptance criteria
- The service should be able to receive measurements from each sensor at
the rate of 1 per minute
- If the CO2 level exceeds 2000 ppm the sensor status should be set to WARN
- If the service receives 3 or more consecutive measurements higher than
2000 the sensor status should be set to ALERT
- When the sensor reaches to status ALERT an alert should be stored
- When the sensor reaches to status ALERT it stays in this state until it receives 3
consecutive measurements lower than 2000; then it moves to OK

### API
#### Collect sensor mesurements
```
POST /api/v1/sensors/{uuid}/mesurements
{
    "co2" : 2000,
    "time" : "2019-02-01T18:55:47+00:00"
}
```
#### Sensor status
`GET /api/v1/sensors/{uuid}`

Response:
```
{
"status" : "OK" // Possible status OK,WARN,ALERT
}
```


## Compile
mvn clean install

## Run
Add mysql connection properties to application.properties before running

```mvn spring-boot:run```
