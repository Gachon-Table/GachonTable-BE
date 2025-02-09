package site.gachontable.domain.waiting.type

enum class Status(
    val statusKo: String,
) {
    ENTERED("입장 완료"),
    CANCELED("취소 완료"),
    WAITING("대기 중"),
    AVAILABLE("입장 가능"),
}
