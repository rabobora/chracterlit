package com.vamos.characterlit.bidtest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EventPayload(@JsonProperty("bidId") Long bidId,
                           @JsonProperty("memberName") String memberName,
                           @JsonProperty("requestBid") int requestBid) {
        }