package com.vamos.characterlit.pay.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Point {

    @Id
    @NotNull
//    @OneToOne
//    @JoinColumn(name = "userNumber")
    private Long userNumber;
    @Setter
    private int allPoint;
    @Setter
    private int usablePoint;
}
