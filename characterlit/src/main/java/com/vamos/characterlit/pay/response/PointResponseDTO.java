package com.vamos.characterlit.pay.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PointResponseDTO {
    private int allPoint;
    private int usablePoint;
}
