package com.example.restapisample

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.annotation.PostConstruct
import javax.persistence.*

@RestController
class UserController constructor(private val userRepository: UserRepository) {

    @GetMapping("/user")
    fun get(): UserListResponse {
        return UserListResponse(userRepository.findAll(), "ok", "get all user.")
    }

    @PostMapping("/user/create")
    fun create(@RequestBody user: User): UserCreateResponse {
        return UserCreateResponse(userRepository.save(user), "ok", "create user success!!")
    }

    @PostConstruct
    fun init() {
        createSeed()
    }

    private fun createSeed() {
        if (userRepository.findAll().isEmpty()) {
            userRepository.save(
                    User(name = "test.tarou", email = "sample@example.com")
            )
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

data class UserListResponse(
        val users: List<User>,
        val status: String,
        val message: String
)

data class UserCreateResponse(
        val user: User,
        val status: String,
        val message: String
)

interface UserRepository : JpaRepository<User, Long>