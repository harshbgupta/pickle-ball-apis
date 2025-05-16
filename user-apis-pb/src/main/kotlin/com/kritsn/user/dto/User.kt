package com.kritsn.user.dto

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_table")
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0, // Mutable field for JPA
    var firstName: String? = null,
    var lastName: String? = null,
    var mobileNumber: String = "",
    var email: String? = null,
    var userType: String? = null,
    private var createdAt: Date = Date(),
    private var updatedAt: Date = Date()
){
    //Use @PrePersist to set the createdAt field only when a new entity is being persisted.
    @PrePersist
    fun onCreate() {
        createdAt = Date()
        updatedAt = Date()
    }

    //Use @PreUpdate to update the updatedAt field every time the entity is updated.
    @PreUpdate
    fun onUpdate() {
        updatedAt = Date()
    }
}