# DashboardCameraDetectionEventLog

Dashboard uses REST API. All the dependencies have been added to pom.xml file, the main ones being Lombok, Jackson- Data Binding, Apache HttpClient5, slf4j-simplea.

# Build of the app
For getting the logs is responsible endpoint REST POST GET /api/v1/logs. Two parameters have to send to it, from and to both in format yyyy-mm-dd. Dates define range from which the logs are supposed to be downloaded. Endpoint requires login and password, for that was used header Authorization: Basic.
For reading values from the response was used ObjectMapper.
For displaying table was used TableView, which was updated using ObservableList.

# Practical use
Dates are gotten from DatePickers and the results are displayed after pressing button Show.
Logs where danger was detected are marked in red.

# Screenshots
![image](https://github.com/Alexis2502/DashboardCameraDetectionEventLog/assets/53090176/60516c55-5014-435d-a321-addca804ac7b)

![image](https://github.com/Alexis2502/DashboardCameraDetectionEventLog/assets/53090176/d115cdf9-dddf-4a55-9214-7ee638825211)
