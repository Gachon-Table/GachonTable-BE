package site.gachontable.domain.waiting.type

enum class Position(
    val positionKo: String,
) {
    ONSITE("현장"),
    REMOTE("원격"),
    CANCEL("취소"),
}
