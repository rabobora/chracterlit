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
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @Column(nullable = false)
    private Integer userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false)
    private Integer category;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Lob
    private String image;

    @Column
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

    @Builder
    public Items(String title, String content, String image, String thumbnail,
                 LocalDateTime startDate, LocalDateTime endDate, Integer startBid,
                 Integer category, Integer bidStatus, Integer viewCount, Boolean isPaid) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.thumbnail = thumbnail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startBid = startBid;
        this.category = category;
        this.bidStatus = bidStatus;
        this.viewCount = viewCount;
        this.isPaid = isPaid;
    }

}
