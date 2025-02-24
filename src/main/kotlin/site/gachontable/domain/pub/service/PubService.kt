package site.gachontable.domain.pub.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.gachontable.domain.menu.domain.Menu
import site.gachontable.domain.menu.port.out.MenuRepository
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.pub.domain.repository.PubRepository
import site.gachontable.domain.pub.domain.repository.ThumbnailRepository
import site.gachontable.domain.pub.exception.PubNotFoundException
import site.gachontable.presentation.pub.dto.request.PubRegisterRequest
import site.gachontable.presentation.pub.dto.response.GetPubDetailsResponse
import site.gachontable.presentation.pub.dto.response.GetPubsResponse
import site.gachontable.presentation.shared.dto.response.RegisterResponse

@Service
class PubService(
    private val pubRepository: PubRepository,
    private val menuRepository: MenuRepository,
    private val thumbnailRepository: ThumbnailRepository,
) {
    @Transactional(readOnly = true)
    fun findAllPubs(): MutableList<GetPubsResponse> {
        val pubs = pubRepository.findAll()

        return pubs.stream()
            .map<GetPubsResponse> { pub: Pub ->
                val thumbnails: MutableList<String> = thumbnailRepository.findUrlsByPub(pub)
                GetPubsResponse.from(pub, thumbnails)
            }.toList()
    }

    @Transactional(readOnly = true)
    fun findPubDetail(pubId: Int): GetPubDetailsResponse {
        val pub = pubRepository.findById(pubId)
            .orElse(throw PubNotFoundException())

        val thumbnails: MutableList<String> = thumbnailRepository.findUrlsByPub(pub)
        val menus: MutableList<Menu> = menuRepository.findAllByPub(pub)

        return GetPubDetailsResponse.of(
            pub, thumbnails, menus
        )
    }

    fun register(request: PubRegisterRequest): RegisterResponse {
        pubRepository.save(createPub(request))

        return RegisterResponse(true, "주점 등록 성공")
    }

    fun createPub(request: PubRegisterRequest): Pub {
        return Pub.create(
            request.pubName,
            request.oneLiner,
            request.instagramUrl,
            request.minutes,
            request.menuUrl,
            request.openStatus,
            request.waitingStatus,
            INITIAL_WAITING_COUNT
        )
    }

    companion object {
        private const val INITIAL_WAITING_COUNT = 0
    }
}
