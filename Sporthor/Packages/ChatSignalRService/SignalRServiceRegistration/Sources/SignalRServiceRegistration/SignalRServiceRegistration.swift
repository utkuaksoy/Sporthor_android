// The Swift Programming Language
// https://docs.swift.org/swift-book


import Factory
import SignalRServiceKit
import SignalRServiceLive

public struct SignalRServiceRegistration {
    public static func registerLive() {
        Container.shared.chatSignalRService.register {
            SignalRServiceLive()
        }
    }
}
