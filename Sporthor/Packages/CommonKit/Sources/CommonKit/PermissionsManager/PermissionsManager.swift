//
//  PushNotificationManager.swift
//  CommonKit
//
//  Created by derTurke on 9.04.2025.
//

import Foundation
import UserNotifications

public final class PermissionsManager {
    static let shared = PermissionsManager()
    
    private init() {}
    
    @discardableResult
    public func notificationRequestAuthorization() async throws -> Bool {
        let center = UNUserNotificationCenter.current()
        let options: UNAuthorizationOptions = [.alert, .badge, .sound]
        let result = try await center.requestAuthorization(options: options)
        return result
    }
    
    @discardableResult
    public func removeAllPendingNotifications() async -> Bool {
        let center = UNUserNotificationCenter.current()
        center.removeAllPendingNotificationRequests()
        return true
    }
}
