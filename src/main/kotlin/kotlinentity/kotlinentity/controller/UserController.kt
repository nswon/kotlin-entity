package kotlinentity.kotlinentity.controller

import kotlinentity.kotlinentity.entity.User
import kotlinentity.kotlinentity.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/new")
    fun save() {
        userService.save()
    }

    @GetMapping
    fun get(): List<User> = userService.get()

    @DeleteMapping("/{id}")
        fun delete(@PathVariable("id") id: UUID) {
        userService.delete(id)
    }
}