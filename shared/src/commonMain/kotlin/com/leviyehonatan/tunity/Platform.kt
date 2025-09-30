package com.leviyehonatan.tunity

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform