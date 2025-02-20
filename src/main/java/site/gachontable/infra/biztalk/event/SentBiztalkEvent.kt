package site.gachontable.infra.biztalk.event

data class SentBiztalkEvent(
    val templateCode: String,
    val userTel: String,
    val variables: HashMap<String, String>,
) {
    companion object {
        fun of(
            templateCode: String, userTel: String, variables: HashMap<String, String>,
        ): SentBiztalkEvent {
            return SentBiztalkEvent(templateCode, userTel, variables)
        }
    }
}