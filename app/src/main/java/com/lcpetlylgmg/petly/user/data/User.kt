package com.lcpetlylgmg.petly.user.data

data class User(
    var accountType: String? = null,
    var createDate: Long? = null,
    var email: String? = null,
    var fcmToken: String? = null,
    @field:JvmField
    var isApproved: Boolean? = false,
    var name: String? = null,
    var requestedPostIds: List<String>? = null,
    var userId: String? = null,
    var type: String? = null
)

