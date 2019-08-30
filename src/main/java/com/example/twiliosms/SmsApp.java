package com.example.twiliosms;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import spark.Request;

import java.util.Arrays;
import java.util.List;

import static spark.Spark.*;

public class SmsApp {
    public static void main(String[] args) {
        get("/", (req, res) -> "Hello Web");

        post("/sms", (req, res) -> {
            String textMsg = parseRequest(req).toUpperCase();
            /* Registration:
             *      If new phone number, register account.
             *          - Ask for Name and Partner#?
             *              - Insert record into DB.
             *          If existing phone number, take request and send response based on what they need.
             *              - "HELP" sends list of commands they can send.
             *              - "CLOCK IN" - Clock in to your shift.
             *              - "CLOCK OUT" - Clock out of your shift.
             *              - "MEAL IN" - Clock in from your meal.
             *              - "MEAL OUT" - Clock out from your meal.
             *              - "VIEW SCHEDULE" - Schedule for current week displayed.
             *              - "SWAP SHIFT" ?
             */

            StringBuilder response = new StringBuilder();

            if(textMsg.contains("SBUX HELP")){
                response.append("SBUX HELP - Display all commands.\n");
                response.append("CLOCK IN - Clock in to your shift.\n");
                response.append("CLOCK OUT - Clock out of your shift.\n");
                response.append("MEAL IN - Clock in from your meal.\n");
                response.append("MEAL OUT - Clock out from your meal.\n");
                response.append("VIEW SCHEDULE - Schedule for current week displayed.");
            }
            else if (textMsg.contains("CLOCK") || textMsg.contains("MEAL")){

            }
            else if (textMsg.contains("SCHEDULE")){

            }
            else {

            }

            res.type("application/xml");
            Body body = new Body
                    .Builder(response.toString())
                    .build();

            Message sms = new Message
                    .Builder()
                    .body(body)
                    .build();

            MessagingResponse twiml = new MessagingResponse
                    .Builder()
                    .message(sms)
                    .build();

            return twiml.toXml();
        });
    }

    private static String parseRequest(Request req) {
        String[] sList = req.body().split("&");
        List<String> stringList = Arrays.asList(sList);

        return stringList.stream()
                .filter(x -> x.startsWith("Body="))
                .map(x -> x.substring("Body=".length()).replace("+", " "))
                .findFirst()
                .orElse("");
    }
}