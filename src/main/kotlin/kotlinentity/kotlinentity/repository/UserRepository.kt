package kotlinentity.kotlinentity.repository

import kotlinentity.kotlinentity.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository: JpaRepository<User, UUID> {
}