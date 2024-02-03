package com.vidyo.soap;

import com.vidyo.mapper.VidyoScheduleMapper;
import com.vidyo.model.VidyoConstant;
import com.vidyo.model.dto.VidyoScheduleDto;
import com.vidyo.model.request.DisconnectConferenceAllRequest;
import com.vidyo.model.request.CreateScheduledRoomRequest;
import com.vidyo.model.response.DisconnectConferenceAllResponse;
import com.vidyo.model.response.LogInResponse;
import com.vidyo.model.response.CreateScheduledRoomResponse;
import com.vidyo.portal.stub.GetEntityByRoomKeyRequest;
import com.vidyo.portal.stub.LogInRequest;
import com.vidyo.repository.VidyoScheduleRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.http.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import static com.vidyo.model.VidyoConstant.ENDPOINT_URL;

@Service
public class VidyoSoapClient extends WebServiceTemplate implements VidyoSoapService{
    @Autowired
    @Qualifier("TestMarsheller")
     private Jaxb2Marshaller jaxb2Marshaller;
    @Autowired
    VidyoScheduleRepository vidyoScheduleRepository;
    @Autowired
    VidyoScheduleMapper vidyoScheduleMapper;

    @Override
    public LogInResponse logIn(String basicAuth) {
        HttpHeaders headers = setHttpHeaders("logIn", basicAuth); // pass header input from request
        LogInRequest soapRequest =  new LogInRequest();  // get dynamic request
        StringWriter stringWriter = new StringWriter();

        jaxb2Marshaller.marshal(soapRequest,new StreamResult(stringWriter)); // convert
        String loginRequest = stringWriter.toString(); // convert in string

        // Set the request entity with headers and the SOAP request
        HttpEntity<String> requestEntity = new HttpEntity<>(requestFormatter(loginRequest), headers);
        System.out.println("SOAP requestEntity of logIn ----" + requestEntity); // add logger
        // Create RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Perform the POST request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(ENDPOINT_URL, requestEntity, String.class);
        // Extract and print the response
        String soapResponse = responseEntity.getBody();
        System.out.println("SOAP Response of loginResponse ----" + soapResponse); // instead add log

        LogInResponse logInResponse = new LogInResponse();
        logInResponse.setStatus(String.valueOf(HttpStatus.OK));
        logInResponse.setMessage(String.valueOf(HttpStatus.ACCEPTED));

        /*ResponseEntity<String> responseEntity = null;
        LogInResponse logInResponse = new LogInResponse();
        try {
            // Perform the POST request
            responseEntity = restTemplate.postForEntity(ENDPOINT_URL, requestEntity, String.class);
            System.out.println("responseEntity----" + responseEntity); //
            // Extract and print the response
            String soapResponse = responseEntity.getBody(); // for further use for pak1 and pak2
            System.out.println("SOAP Response of login ----" + soapResponse); // add logger
            System.out.println("responseEntity getStatusCode...." + responseEntity.getStatusCode()); // 200 OK
            if(Objects.equals(responseEntity.getStatusCode().toString(), "200 OK")){
                logInResponse.setStatus(String.valueOf(HttpStatus.OK));
                logInResponse.setMessage(String.valueOf(HttpStatus.ACCEPTED));
            }
        } catch (Exception exception){
            if(exception.getMessage().contains("401")){
                logInResponse.setStatus(String.valueOf(HttpStatus.UNAUTHORIZED));
                logInResponse.setMessage(String.valueOf(HttpStatus.NOT_FOUND));
            }
        }*/
        return logInResponse;
    }

    @SneakyThrows
    @Override
    public CreateScheduledRoomResponse createScheduledRoom(CreateScheduledRoomRequest request, String basicAuth) {
        // db call and check the room is exist or not
        // if roomName exist then return from db
        // else call soap service get room url and persist in db then return that
        CreateScheduledRoomResponse createScheduledRoomResponse = new CreateScheduledRoomResponse();
        Optional<VidyoScheduleDto> vidyoSchedule  = vidyoScheduleRepository.findById(request.getRoomName());
        if(vidyoSchedule.isPresent()){
            System.out.println("vidyoSchedule roomName found as ----" +request.getRoomName()); // add log
            VidyoScheduleDto vidyoScheduleDto = vidyoSchedule.get();
            createScheduledRoomResponse.setUserName(vidyoScheduleDto.getUserName());
            createScheduledRoomResponse.setRoomName(vidyoScheduleDto.getRoomName());
            createScheduledRoomResponse.setRoomURL(vidyoScheduleDto.getRoomURL());
            createScheduledRoomResponse.setStatus(String.valueOf(HttpStatus.OK));
            createScheduledRoomResponse.setMessage(String.valueOf(HttpStatus.FOUND));
        } else {
            System.out.println("vidyoSchedule not found for roomName ----" + request.getRoomName()); // add log

            HttpHeaders headers = setHttpHeaders("createScheduledRoom", basicAuth); // pass header input from request
            com.vidyo.portal.stub.CreateScheduledRoomRequest soapRequest =  new com.vidyo.portal.stub.CreateScheduledRoomRequest();  // get dynamic request
            StringWriter stringWriter = new StringWriter();

            jaxb2Marshaller.marshal(soapRequest,new StreamResult(stringWriter)); // convert
            String scheduledRoomRequest = stringWriter.toString(); // convert in string

            // Set the request entity with headers and the SOAP request
            HttpEntity<String> requestEntity = new HttpEntity<>(requestFormatter(scheduledRoomRequest), headers);
            System.out.println("SOAP requestEntity of createScheduledRoom ----" + requestEntity); // add logger
            // Create RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            // Perform the POST request
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(ENDPOINT_URL, requestEntity, String.class);
            // Extract and print the response
            String soapResponse = responseEntity.getBody();
            System.out.println("SOAP Response of scheduledRoom ----" + soapResponse); // instead add log

            List<String> roomURL = getFullNameFromXml(soapResponse, "ns1:roomURL");
            System.out.println("roomURL ---- " + roomURL.get(0));

            createScheduledRoomResponse.setUserName(request.getUserName());
            createScheduledRoomResponse.setRoomName(request.getRoomName());
            createScheduledRoomResponse.setRoomURL(roomURL.get(0));
            createScheduledRoomResponse.setStatus(String.valueOf(HttpStatus.OK));
            createScheduledRoomResponse.setMessage(String.valueOf(HttpStatus.CREATED));

            // DB operation
            VidyoScheduleDto vidyoScheduleDto = vidyoScheduleMapper.mapScheduleRoom(request.getRoomName(), request.getUserName(), roomURL.get(0));
            vidyoScheduleRepository.save(vidyoScheduleDto);
        }
        return createScheduledRoomResponse;
    }

    @SneakyThrows
    @Override
    public DisconnectConferenceAllResponse disconnectConferenceAll(DisconnectConferenceAllRequest request, String basicAuth) {
        //Call getEntityByRoomKeyResponse and get EntityId/ConfrenceId to pass in disconnect call request
        String confrenceId = getEntityByRoomKeyResponse(request.getRoomKey(), basicAuth); // pass roomKey

        HttpHeaders headers = setHttpHeaders("disconnectConferenceAll", basicAuth); // pass header input from request
        com.vidyo.portal.stub.DisconnectConferenceAllRequest soapRequest =  new com.vidyo.portal.stub.DisconnectConferenceAllRequest();  // get dynamic request
        soapRequest.setConferenceID(confrenceId); // need to verify
        StringWriter stringWriter = new StringWriter();

        jaxb2Marshaller.marshal(soapRequest,new StreamResult(stringWriter)); // convert
        String disconnectConferenceAllRequest = stringWriter.toString(); // convert in string

        // Set the request entity with headers and the SOAP request
        HttpEntity<String> requestEntity = new HttpEntity<>(requestFormatter(disconnectConferenceAllRequest), headers);
        System.out.println("SOAP requestEntity of disconnectConferenceAll ----" + requestEntity); // add logger
        // Create RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        // Perform the POST request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(ENDPOINT_URL, requestEntity, String.class);
        // Extract and print the response
        String soapResponse = responseEntity.getBody();
        System.out.println("SOAP Response of disconnectConferenceAll ----" + soapResponse); // instead add log

        List<String> status = getFullNameFromXml(soapResponse, "ns1:OK");
        System.out.println("status ---- " + status.get(0));

        DisconnectConferenceAllResponse disconnectConferenceAllResponse = new DisconnectConferenceAllResponse();
        disconnectConferenceAllResponse.setNotificationMsg(status.get(0));
        disconnectConferenceAllResponse.setStatus(String.valueOf(HttpStatus.OK));
        disconnectConferenceAllResponse.setMessage(String.valueOf(HttpStatus.RESET_CONTENT));

        // DB operation to update room status
        Optional<VidyoScheduleDto> vidyoSchedule  = vidyoScheduleRepository.findById(request.getRoomName());
        if(vidyoSchedule.isPresent()){
            VidyoScheduleDto vidyoScheduleDto = vidyoSchedule.get();
            vidyoScheduleDto.setRoomStatus("Inactive");
            vidyoScheduleRepository.save(vidyoScheduleDto);
        }
        return disconnectConferenceAllResponse;
    }

    @SneakyThrows
    public String getEntityByRoomKeyResponse(String roomKey, String basicAuth) {
        HttpHeaders headers = setHttpHeaders("getEntityByRoomKey", basicAuth); // pass header input from request
        GetEntityByRoomKeyRequest soapRequest =  new GetEntityByRoomKeyRequest();  // get dynamic request
        soapRequest.setRoomKey(roomKey); // need to verify
        StringWriter stringWriter = new StringWriter();

        jaxb2Marshaller.marshal(soapRequest,new StreamResult(stringWriter)); // convert
        String getEntityByRoomKeyRequest = stringWriter.toString(); // convert in string

        // Set the request entity with headers and the SOAP request
        HttpEntity<String> requestEntity = new HttpEntity<>(requestFormatter(getEntityByRoomKeyRequest), headers);
        System.out.println("SOAP requestEntity of getEntityByRoomKey ----" + requestEntity); // add logger
        // Create RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        // Perform the POST request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(ENDPOINT_URL, requestEntity, String.class);
        System.out.println("responseEntity ----" + responseEntity);
        // Extract and print the response
        String soapResponse = responseEntity.getBody();
        System.out.println("SOAP Response of getEntityByRoomKey ---- " + soapResponse); // instead add log

        List<String> entityId = getFullNameFromXml(soapResponse, "ns1:entityID");
        System.out.println("entityId/confrenceId ---- " + entityId.get(0)); // add logger

        return entityId.get(0); // return entityId/confrenceId
    }

    private HttpHeaders setHttpHeaders(String serviceName, String basicAuth) {
        HttpHeaders headers = new HttpHeaders();
        // pass content type
        headers.setContentType(MediaType.TEXT_XML);
        // pass SOAPAction as service name
        headers.set(VidyoConstant.SOAP_ACTION,serviceName);
        // dynamic credential
        //String authHeader = "Basic " + base64Encode("ssingh:ssingh");
        //headers.set(HttpHeaders.AUTHORIZATION, authHeader);
        headers.set(HttpHeaders.AUTHORIZATION, basicAuth);
        return headers;
    }

    private String requestFormatter(String soapRequest) {
        String requestFormat = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://portal.vidyo.com/user/v1_1\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n"  +
                soapRequest +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        //System.out.println("soapRequestTest ----" + requestFormat);
        return requestFormat;
    }

    private String base64Encode(String value) {
        byte[] encodedBytes = Base64.getEncoder().encode(value.getBytes());
        return new String(encodedBytes);
    }

    public static Document loadXMLString(String response) throws Exception
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(response));
        return db.parse(is);
    }

    public static List<String> getFullNameFromXml(String response, String tagName) throws Exception {
        Document xmlDoc = loadXMLString(response);
        NodeList nodeList = xmlDoc.getElementsByTagName(tagName);
        List<String> ids = new ArrayList<String>(nodeList.getLength());
        for(int i=0;i<nodeList.getLength(); i++) {
            Node x = nodeList.item(i);
            ids.add(x.getFirstChild().getNodeValue());
            System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
        }
        return ids;
    }
}



