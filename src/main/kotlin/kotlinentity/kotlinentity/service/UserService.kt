package kotlinentity.kotlinentity.service

import kotlinentity.kotlinentity.entity.User
import kotlinentity.kotlinentity.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {
    fun save() {
        val user = User("남세원")
        userRepository.save(user)
    }

    fun get(): List<User> = userRepository.findAll()

    fun delete(id: UUID) {
        val user: User = userRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("에러")
        userRepository.delete(user)
    }
}