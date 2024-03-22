package com.vamos.characterlit.items.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    // 테스트를 위해 일단 userid를 참조키가 아닌 상품 테이블의 칼럼으로 넣어둠
    @Column
    private Integer userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @Column(length = 30)
    private String nickname;

    @Column(nullable = false)
    private Integer category;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ElementCollection
    private List<String> image;

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

    @Column
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

    //게시글 조회시 조회수 +1 관련 로직
    public void increaseViewCount() {

        this.viewCount++;

    }

    @Builder
    public Items(String title, String content, List<String> image, String thumbnail,
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
