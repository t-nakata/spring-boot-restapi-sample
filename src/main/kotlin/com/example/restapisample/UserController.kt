package com.example.restapisample

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.persistence.*

@RestController
class UserController @Autowired constructor(private val userRepository: UserRepository) {

    @RequestMapping("/user", method = [RequestMethod.GET])
    fun get(): Users {
        return Users(userRepository.findAll(), "ok", "get all user.")
    }

    @RequestMapping("/user/create", method = [RequestMethod.POST])
    fun create(@RequestBody user: User): UserCreateResponse {
        val result = userRepository.save(user)
        return UserCreateResponse(result, "ok", "create user success!!")
    }

    @EventListener
    fun seed(event: ContextRefreshedEvent) {
        val users = userRepository.findAll()
        if (users.isEmpty()) {
            val user = User(name = "test.tarou", email = "sample@example.com")
            userRepository.save(user)
        }
    }
}


@Entity
@Table(name = "user")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = 0,
        var name: String? = null,
        var email: String? = null,
        var create_at: Date = Date(),
        var update_at: Date = Date()
)

data class Users(
        val users: List<User>,
        val status: String,
        val message: String
)

data class UserCreateResponse(
        val user: User,
        val status: String,
        val message: String
)

@Repository
interface UserRepository : JpaRepository<User, Long>