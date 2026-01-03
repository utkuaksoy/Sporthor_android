//
//  AppDelegate.swift
//  Sporthor
//
//  Created by derTurke on 30.01.2025.
//

import ChatKit
import DesignKit
import Factory
import FirebaseCore
import FirebaseMessaging
import UIKit
import IQKeyboardManagerSwift
import AppTrackingTransparency
import AdSupport
import FirebaseAnalytics

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        setupKeyboard()
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) { [weak self] in
            guard let self else { return }
            requestTrackingPermissionIfNeeded()
            setupFirebaseNotification()
        }
        FontFamily.registerAllCustomFonts()
        
        if let notification = launchOptions?[.remoteNotification] as? [String: Any] {
            Container.shared.deeplinkManager()?.handlePushNotification(notification, isColdStart: true)
        }
        
        return true
    }
    
    // MARK: UISceneSession Lifecycle
    
    func application(
        _ application: UIApplication,
        configurationForConnecting connectingSceneSession: UISceneSession,
        options: UIScene.ConnectionOptions
    ) -> UISceneConfiguration {
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }
    
    func application(
        _ application: UIApplication,
        didDiscardSceneSessions sceneSessions: Set<UISceneSession>
    ) {}
    
    // MARK: - Custom Methods
    private func setupKeyboard() {
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.resignOnTouchOutside = true
        IQKeyboardManager.shared.toolbarConfiguration.tintColor = ColorName.contentStrong900.color
    }
    
    private func setupFirebaseNotification() {
        FirebaseApp.configure()
        Messaging.messaging().delegate = self
        UNUserNotificationCenter.current().delegate = self
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(options: authOptions) { granted, error in
            if granted {
                DispatchQueue.main.async {
                    UIApplication.shared.registerForRemoteNotifications()
                }
                Messaging.messaging().token { token, error in
                    if let token = token {
                        Container.shared.pushNotificationService().updateToken(token)
                    }
                }
            }
        }
    }
    
    func requestTrackingPermissionIfNeeded() {
        if #available(iOS 14, *) {
            let status = ATTrackingManager.trackingAuthorizationStatus
            switch status {
            case .notDetermined:
                requestTrackingPermission()
            case .authorized:
                Analytics.setAnalyticsCollectionEnabled(true)
            case .denied, .restricted:
                Analytics.setAnalyticsCollectionEnabled(false)
            @unknown default:
                requestTrackingPermission()
            }
        }
    }
    
    func requestTrackingPermission() {
        if #available(iOS 14, *) {
            ATTrackingManager.requestTrackingAuthorization { [weak self] status in
                guard let _ = self else { return }
                switch status {
                case .authorized:
                    Analytics.setAnalyticsCollectionEnabled(true)
                case .denied, .restricted:
                    Analytics.setAnalyticsCollectionEnabled(false)
                case .notDetermined:
                    Analytics.setAnalyticsCollectionEnabled(false)
                @unknown default:
                    Analytics.setAnalyticsCollectionEnabled(false)
                }
            }
        }
    }
    
}

extension AppDelegate: MessagingDelegate, UNUserNotificationCenterDelegate {
    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        Messaging.messaging().apnsToken = deviceToken
    }
    
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        Container.shared.pushNotificationService().updateToken(fcmToken)
    }
    
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        let userInfo = notification.request.content.userInfo
        if let type = userInfo["type"] as? String {
            let messageType = PushNotificationType(typeString: type, data: userInfo)
            
            switch messageType {
            case .chat:
                if let origin = userInfo["origin"] as? String, origin == "local" {
                    completionHandler([.banner, .sound, .badge])
                } else {
                    Container.shared.pushNotificationService().handlePushNotification(data: userInfo, isFromPushClick: false)
                    completionHandler([])
                }
            case .like, .post, .follow, .unknown:
                completionHandler([.banner, .sound, .badge])
            }
        }
    }
    
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let userInfo = response.notification.request.content.userInfo
        Container.shared.deeplinkManager()?.handlePushNotification(userInfo, isColdStart: false)
        completionHandler()
    }
}
