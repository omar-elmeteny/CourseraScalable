package profilemanagement.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;



public class SerializableResponseEntity<T> extends ResponseEntity<T> implements Serializable {

    public SerializableResponseEntity(HttpStatus status) {
        super(status);
    }

    public SerializableResponseEntity(T body, HttpStatus status) {
        super(body, status);
    }

    public SerializableResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public SerializableResponseEntity(T body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public SerializableResponseEntity(T body, MultiValueMap<String, String> headers, int rawStatus) {
        super(body, headers, rawStatus);
    }

    public SerializableResponseEntity(T body, MultiValueMap<String, String> headers, int rawStatus, HttpStatusCode statusCode) {
        super(body, headers, rawStatus);
    }
}
