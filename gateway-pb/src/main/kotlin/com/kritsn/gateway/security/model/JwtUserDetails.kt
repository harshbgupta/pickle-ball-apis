package com.kritsn.gateway.security.model

import lombok.Getter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


@Getter
class JwtUserDetails(
    private val phoneNumber: String,
    private val id: Long,
    private val token: String,
    grantedAuthorities: List<GrantedAuthority>
) :
    UserDetails {
    private val authorities: Collection<GrantedAuthority> = grantedAuthorities

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String {
        return phoneNumber
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
