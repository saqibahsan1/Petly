package com.lcpetlylgmg.petly.organization.data

data class Feedback(
    var feedbackId: String?,
    var deletedDate: Long?,
    var deletedBy: String?,
    var dogName: String?,
    var postId: String?,
    var deletedByName: String?,
    var reasonId: Int?,
    var reason: String?,
)
