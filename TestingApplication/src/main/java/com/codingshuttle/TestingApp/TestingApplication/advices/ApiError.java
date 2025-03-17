package com.codingshuttle.TestingApp.TestingApplication.advices;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

//@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
public class ApiError {

    @JsonFormat(pattern = "hh:mm:ss dd-MM-YYYY")
    private LocalDateTime timeStamp;

    private HttpStatus httpStatus;

    private String error;

    public ApiError() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus httpStatus, String error) {
        this.timeStamp = LocalDateTime.now();
        this.httpStatus = httpStatus;
        this.error = error;
    }

}
