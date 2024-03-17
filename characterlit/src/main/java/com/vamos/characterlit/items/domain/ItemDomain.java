package com.vamos.characterlit.items.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String userId;

    @Column(nullable = false)
    private Integer nickname;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Lob
    private String image;

    @Column(nullable = true, length = 255)
    private String thumbnail;

    @Column(nullable = false)
    private LocalDateTime regDate;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Integer startBid;

    @Column(nullable = true)
    private Integer finalBid;

    @Column(nullable = false)
    private Integer bidStatus;

    @Column(nullable = false)
    private Integer category;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false)
    private Boolean isPaid;

    @PrePersist
    protected void onCreate() {
        this.regDate = LocalDateTime.now();
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}
