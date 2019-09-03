package com.example.twiliosms;

import com.example.twiliosms.domain.Employee;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import org.springframework.stereotype.Component;
import spark.Request;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

@Component
public class SmsApp {

    private Map<String, Employee> phoneNumberMap = new HashMap<>();
    private boolean isPartner = false;
    private static final String TEST_NUMBER = "<<PHONE NUMBER>>";

    @PostConstruct
    private void populateMap(){
        Employee e = new Employee();
        e.setFirstName("Eric");
        e.setLastName("Test");
        e.setPhoneNumber(TEST_NUMBER);
        e.setPartnerNumber("US12345");
        phoneNumberMap.put(TEST_NUMBER, e);
    }

    public void sms() {
//        populateMap();
        get("/", (req, res) -> "Hello Web");

        post("/sms", (req, res) -> {
//            System.out.println("attributes: ");
//            req.attributes().forEach(System.out::println);
//            System.out.println("headers: ");
//            req.headers().forEach(System.out::println);
//
//            req.cookies().forEach((x,y) -> {
//                System.out.println("cookie key: " + x);
//                System.out.println("cookie value: " + y);
//            });
//            req.params().forEach((x,y) -> {
//                System.out.println("param key: " + x);
//                System.out.println("param value: " + y);
//            });

            Map<SmsRequestEnum, String> reqMap = parseRequest(req);

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

            String textMsg = reqMap.get(SmsRequestEnum.BODY).toUpperCase();
            String to = reqMap.get(SmsRequestEnum.TO);
            String from = reqMap.get(SmsRequestEnum.FROM);

            Employee partner = getValidPartner(from);
            if(null != partner){
                isPartner = true;
            }

            if(isPartner){
                System.out.println("Partner: " + partner.getFirstName());

                if(textMsg.contains("SBUX HELP")){
                    generateHelpMenu(response);
                }
                else if (textMsg.contains("CLOCK")){
                    if(textMsg.contains("IN")){
                        generateClockMenu(partner, response, ActionTypeEnum.CLOCK_IN);
                    }
                    else if (textMsg.contains("OUT")){
                        generateClockMenu(partner, response, ActionTypeEnum.CLOCK_OUT);
                    }
                    else {
                        //PLEASE CLARIFY YOUR ACTION
                        response.append("Please clarify your intent.  Help menu displayed for your convenience.\n");
                        generateHelpMenu(response);
                    }
                }
                else if (textMsg.contains("MEAL")){
                    if(textMsg.contains("IN")){
                        generateClockMenu(partner, response, ActionTypeEnum.MEAL_IN);
                    }
                    else if (textMsg.contains("OUT")){
                        generateClockMenu(partner, response, ActionTypeEnum.MEAL_OUT);
                    }
                    else {
                        // PLEASE CLARIFY YOUR ACTION
                        response.append("Please clarify your intent.  Help menu displayed for your convenience.\n");
                        generateHelpMenu(response);
                    }

                }
//                else if (textMsg.contains("SCHEDULE")){
//
//                }
                else {
                    // PLEASE CLARIFY YOUR ACTION
                    response.append("Please clarify your intent.  Help menu displayed for your convenience.\n");
                    generateHelpMenu(response);

                }
            }
            else {
                // BEFORE YOU CAN SEND REQUESTS, PLEASE REGISTER
                // register by COPY AND PASTE:
                // FIRST NAME: <firstName>
                // LAST NAME: <lastName>
                // PARTNER NUMBER: <partnerNbr>
                response.append("PLEASE REGISTER");
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

    private void generateClockMenu(Employee partner, StringBuilder response, ActionTypeEnum actionType) {
        response.append("Thank you, ")
                .append(partner.getFullName())
                .append(". Your ")
                .append(actionType.getActionType())
                .append(" has been accepted for: ")
                .append(LocalDateTime.now().toString());
    }

    private void generateHelpMenu(StringBuilder response) {
        response.append("SBUX HELP - Display all commands.\n")
                .append("CLOCK IN - Clock in to your shift.\n")
                .append("CLOCK OUT - Clock out of your shift.\n")
                .append("MEAL IN - Clock in from your meal.\n")
                .append("MEAL OUT - Clock out from your meal.\n")
                .append("VIEW SCHEDULE - Schedule for current week displayed.");
    }

    private Employee getValidPartner(String phoneNbr) {
        System.out.println("Incoming Phone Number: " + phoneNbr);
        return phoneNumberMap.get(phoneNbr);
    }

    private Map<SmsRequestEnum, String> parseRequest(Request req) {
        Map<SmsRequestEnum, String> result = new HashMap<>();

        String[] sList = req.body().split("&");
        List<String> stringList = Arrays.asList(sList);

        result.put(SmsRequestEnum.BODY, stringList.stream()
                .filter(x -> x.startsWith(SmsRequestEnum.BODY.getRequestValue()))
                .map(x -> x.substring(SmsRequestEnum.BODY.getRequestValue().length()).replace("+", " "))
                .findFirst()
                .orElse(""));

        result.put(SmsRequestEnum.FROM, stringList.stream()
                .filter(x -> x.startsWith(SmsRequestEnum.FROM.getRequestValue()))
                .map(x -> x.substring(SmsRequestEnum.FROM.getRequestValue().length()).replace("%2B", ""))
                .findFirst()
                .orElse(""));

        result.put(SmsRequestEnum.TO, stringList.stream()
                .filter(x -> x.startsWith(SmsRequestEnum.TO.getRequestValue()))
                .map(x -> x.substring(SmsRequestEnum.TO.getRequestValue().length()).replace("%2B", ""))
                .findFirst()
                .orElse(""));

        return result;
    }
}