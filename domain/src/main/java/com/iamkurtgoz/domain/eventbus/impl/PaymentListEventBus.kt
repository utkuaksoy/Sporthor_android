package com.iamkurtgoz.domain.eventbus.impl

import com.iamkurtgoz.domain.core.CoreEventBus

object PaymentListEventBus : CoreEventBus<PaymentListEventBus.Event>() {
    sealed class Event {
        data object RefreshList : Event()
    }
}
