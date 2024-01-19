package com.vidyo.bean;

import jakarta.xml.bind.Marshaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class VidyoBeanConfig {

    @Bean("TestMarsheller")
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.vidyo.portal.stub");
        Map<String,Object> properties = new HashMap<>();
        properties.put(Marshaller.JAXB_FRAGMENT,true);
        marshaller.setMarshallerProperties(properties);
        return marshaller;
    }

}


