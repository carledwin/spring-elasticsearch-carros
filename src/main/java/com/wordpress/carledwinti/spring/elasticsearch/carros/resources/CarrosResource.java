package com.wordpress.carledwinti.spring.elasticsearch.carros.resources;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@RestController
@RequestMapping("/rest/carros")
public class CarrosResource {

    @GetMapping
    public ResponseEntity<?> get() {

        try {

            TransportClient transportClient = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            IndexResponse indexResponse = transportClient.prepareIndex("carros", "_doc", "1")
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("modelo", "Fox")
                            .field("marca", "Volkswagen")
                            .field("valor", 45300)
                            .field("carroceria", "hatch").endObject())
                    .get();
            return new ResponseEntity<String>(indexResponse.getResult().toString(), HttpStatus.OK);
        }catch(UnknownHostException uhe){
            return new ResponseEntity<String>("Falha ao tentar criar o TransportClient para o Elasticsearch. Motivo: " + uhe.getCause() + ", Message: " + uhe.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(IOException ioe){
            return new ResponseEntity<String>("Falha ao tentar criar o objeto json para enviar ao Elasticsearch. Motivo: " + ioe.getCause() + ", Message: " + ioe.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(Exception e){
            return new ResponseEntity<String>("Falha ao tentar recuperar todos os registros no Elasticsearch. Motivo: " + e.getCause() + ", Message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
