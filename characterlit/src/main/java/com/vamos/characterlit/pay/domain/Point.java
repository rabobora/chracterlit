package com.vamos.characterlit.pay.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Point {

    @Id
//    @OneToOne
//    @JoinColumn(name = "userNumber")
    private Long userNumber;
    @Setter
    private int allPoint;
    @Setter
    private int usablePoint;
}
