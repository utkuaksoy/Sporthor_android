//
//  Factory.swift
//  Sporthor
//
//  Created by derTurke on 30.01.2025.
//

import ChatRegistration
import Foundation
import Factory
import DesignKit
import NetworkKit
import SignalRServiceRegistration

extension Container: AutoRegistering {
    public func autoRegister() {
        NetworkKitRegistration.registerLive()
        ChatRegistration.registerLive()
        SignalRServiceRegistration.registerLive()
    }
}
