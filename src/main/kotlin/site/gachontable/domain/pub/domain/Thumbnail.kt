package site.gachontable.domain.pub.domain

import jakarta.persistence.*

@Entity(name = "thumbnail")
class Thumbnail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id", nullable = false)
    val pub: Pub,

    @Column(nullable = false)
    var url: String,
) {
    fun update(url: String) {
        this.url = url
    }

    companion object {
        fun create(url: String, pub: Pub): Thumbnail {
            return Thumbnail(
                url = url,
                pub = pub
            )
        }
    }
}
