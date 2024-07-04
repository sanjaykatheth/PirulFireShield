package com.pirul.springjwt.payload.response;

import com.pirul.springjwt.constants.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageResponse {
    private ResponseMessage message;
}
