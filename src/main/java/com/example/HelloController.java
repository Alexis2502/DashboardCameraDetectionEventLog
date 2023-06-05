package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.net.URIBuilder;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class HelloController {

    @FXML
    private TableView<EventLogEntry> tableView;

    @FXML
    private TableColumn<EventLogEntry, Boolean> dangerCol;

    @FXML
    private TableColumn<EventLogEntry, Date> dateCol;

    @FXML
    private DatePicker fromDate;

    @FXML
    private TableColumn<EventLogEntry, String> idCol;

    @FXML
    private TableColumn<EventLogEntry, String> peopleCol;

    @FXML
    private Button showBtn;

    @FXML
    private DatePicker toDate;

    @FXML
    private TableColumn<EventLogEntry, String> userCol;

    @FXML
    @SneakyThrows({IOException.class, URISyntaxException.class})
    void onshowBtn(ActionEvent event){
        // Configuration
        var endpointBaseUrl = "https://7six8mafg2.execute-api.eu-central-1.amazonaws.com/prod";
        var endpointPath = "/api/v1/logs";
        var authUsername = "admin";
        var authPassword = "password";

        // Parameters (date range for event log fetch operation)
        var from = String.valueOf(fromDate.getValue());
        var to = String.valueOf(toDate.getValue());
        String path = "C://Users/aleks/OneDrive/Pulpit";

        // Constructing the required objects
        var endpointUri = new URIBuilder(endpointBaseUrl + endpointPath)
                .addParameter("from", from)
                .addParameter("to", to)
                .build();
        var authCredentialsBase64 = Base64.getEncoder().encodeToString(String.format("%s:%s", authUsername, authPassword).getBytes());

        try(var httpClient = HttpClients.createDefault()) {
            // Creating GET request object
            var httpRequest = new HttpGet(endpointUri);
            httpRequest.addHeader("Authorization", "Basic " + authCredentialsBase64);

            // Sending the request and fetching the response
            try (var httpResponse = httpClient.execute(httpRequest)) {
                handleResponse(httpResponse,path);
            }
        }
    }

    @SneakyThrows({IOException.class})
    public void handleResponse(CloseableHttpResponse response,String path){
        if (response.getCode() != 200) {
            System.out.println("Server returned error code: " + response.getCode());
            return;
        }
        ObjectMapper jsonMapper = new ObjectMapper();
        EventLogEntry[] eventLog = jsonMapper.readValue(response.getEntity().getContent(), EventLogEntry[].class);

        idCol.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("userLogin"));
        dangerCol.setCellValueFactory(new PropertyValueFactory<>("dangerDetected"));
        peopleCol.setCellValueFactory(new PropertyValueFactory<>("detectedPeople"));

        ObservableList<EventLogEntry> oblist = FXCollections.observableArrayList(List.of(eventLog));
        tableView.setItems(oblist);
        tableView.setRowFactory(tv-> new TableRow<>() {
            @Override
            public void updateItem(EventLogEntry item, boolean empty) {
                super.updateItem(item, empty);
                if(item==null){
                    setStyle("");
                }
                else if (item.isDangerDetected()) {
                    setStyle("-fx-background-color: tomato;");
                }else{
                    setStyle("");
                }
            }
        });

        for (EventLogEntry logEntry : eventLog) {
            System.out.println(logEntry);
        }
    }

}
