package site.gachontable.domain.pub.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.gachontable.domain.pub.domain.Pub

interface PubRepository : JpaRepository<Pub, Int>
