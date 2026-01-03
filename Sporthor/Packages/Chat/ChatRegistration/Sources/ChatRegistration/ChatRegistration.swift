// The Swift Programming Language
// https://docs.swift.org/swift-book

import Factory
import Foundation
import ChatKit
import ChatFeatureLive
import ChatCoordinator

public struct ChatRegistration {
    public static func registerLive() {
        Container.shared.chatCoordinator.register {
            ChatCoordinatorLive()
        }
    }
}
