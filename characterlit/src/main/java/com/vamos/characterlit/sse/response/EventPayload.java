package com.vamos.characterlit.sse.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EventPayload(@JsonProperty("bidId") Long bidId,
                           @JsonProperty("userNumber") Long userNumber,
                           @JsonProperty("requestBid") int requestBid) {
        }