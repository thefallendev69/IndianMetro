package com.thefallendeveloper.indianmetro

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform