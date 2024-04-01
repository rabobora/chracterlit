package com.vamos.characterlit.items.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vamos.characterlit.users.domain.Users;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_number")
    private Users users;

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

    @Column
    private Long winnerNumber;

    @PrePersist

    protected void onCreate() {
        this.regDate = LocalDateTime.now();
    }

    //게시글 조회시 조회수 +1 관련 로직
    public void increaseViewCount() {

        this.viewCount++;

    }

    public Long getUserNumber() {
        return this.users != null ? this.users.getUserNumber() : null;
    }


    @Builder
    public Items(String title, String content, List<String> image, String thumbnail,
                 LocalDateTime startDate, LocalDateTime endDate, Integer startBid,
                 Integer category, Integer bidStatus, Integer viewCount, Boolean isPaid,Long winnerNumber) {
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
        this.winnerNumber = winnerNumber;
    }

}
