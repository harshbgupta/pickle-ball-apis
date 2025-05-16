package com.kritsn.user.repository


import com.kritsn.user.dto.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepository:CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    fun findUserById(id: Long?): User?
    fun findUserByMobileNumber(mobileNumber: String): MutableList<User>

//    @Query("SELECT u FROM User u WHERE u.mobileNumber like ?1")
//    fun findUserByMobileNumber(id: Long?): User?
}