package site.gachontable.domain.menu.domain

import jakarta.persistence.*
import site.gachontable.domain.pub.domain.Pub

@Entity(name = "menu")
class Menu(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    val pub: Pub,

    @Column(nullable = false)
    var menuName: String,

    @Column(nullable = false)
    var price: String,

    @Column(nullable = false)
    var oneLiner: String,

    @Column
    var thumbnail: String,
) {
    fun update(menuName: String, price: String, oneLiner: String, thumbnail: String) {
        this.thumbnail = thumbnail
        this.menuName = menuName
        this.price = price
        this.oneLiner = oneLiner
    }

    companion object {
        fun create(
            pub: Pub, menuName: String, price: String, oneLiner: String, thumbnail: String,
        ): Menu {
            return Menu(
                pub = pub,
                menuName = menuName,
                price = price,
                oneLiner = oneLiner,
                thumbnail = thumbnail,
            )
        }
    }
}
