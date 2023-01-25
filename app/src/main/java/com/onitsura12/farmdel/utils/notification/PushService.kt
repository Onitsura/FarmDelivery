package com.onitsura12.farmdel.utils.notification

import com.google.firebase.messaging.FirebaseMessagingService

class PushService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }
}