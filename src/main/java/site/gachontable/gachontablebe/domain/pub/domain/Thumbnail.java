package site.gachontable.gachontablebe.domain.pub.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "thumbnail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Thumbnail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer thumbnailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id", nullable = false)
    private Pub pub;

    @Column(nullable = false)
    private String url;

    public static Thumbnail create(String url, Pub pub) {
        return Thumbnail.builder()
                .url(url)
                .pub(pub)
                .build();
    }

    @Builder
    public Thumbnail(String url, Pub pub) {
        this.url = url;
        this.pub = pub;
    }

    public void update(String url) {
        this.url = url;
    }
}
