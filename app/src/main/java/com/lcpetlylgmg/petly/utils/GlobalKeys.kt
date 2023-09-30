package com.lcpetlylgmg.petly.utils

class GlobalKeys {
    companion object {

        const val ACCOUNT_TYPE = "account_type"
        const val ACCOUNT_TYPE_ADOPT = "toAdopt"
        const val ACCOUNT_TYPE_ORGANIZATION = "organization"
        const val ACCOUNT_TYPE_SHELTER = "shelter"
        const val ACCOUNT_TYPE_AGENT = "bidAgent"

        const val TYPE = "type"
        const val TYPE_USER = "typeUser"
        const val TYPE_SHELTER = "typeShelter"
        const val TYPE_ORGANIZATION = "typeOrganization"

        const val REQUEST_TYPE_TO = "requestTo"
        const val REQUEST_TYPE_FROM = "requestFrom"


        const val ACTION_TYPE = "action_from"
        const val ACTION_NEW = "action_new"
        const val ACTION_UPDATE = "action_update"

        const val THREAD_ID = " threadId"
        const val POST_DELETE = "postDelete"
        const val WEB_DATA = "webData"
        const val FROM_REQUEST = "from_request"
        const val FROM_INBOX = "from_inbox"

        const val USER_TABLE = "user_table"
        const val REQUEST_TABLE = "request_table"
        const val POST_TABLE = "post_table"
        const val CHAT_TABLE = "chat_table"
        const val FEEDBACK_TABLE = "feedback_table"
        const val PRODUCT_TABLE = "product_table"
        const val SERVICES_TABLE = "services_link_table"

        const val POST = "post"
        const val REQUEST = "request"
        const val CHAT = "chat"

    }

    fun getCurrentUnixTimestamp(): Long {
        return System.currentTimeMillis() / 1000
    }

}