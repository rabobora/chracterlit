package com.vamos.characterlit.sse.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EventPayload(@JsonProperty("bidId") Long bidId,
                           @JsonProperty("userId") Long userId,
                           @JsonProperty("requestBid") int requestBid) {
        }