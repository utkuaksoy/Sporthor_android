import Foundation

final class FollowingManager {
    static let shared = FollowingManager()
    private init() {}
    
    private let notificationCenter = NotificationCenter.default
    
    func updateFollowingStatus(userId: String, followStatus: ProfileActionButtonType) {
        let userInfo: [String: Any] = [
            "userId": userId,
            "followStatus": followStatus
        ]
        notificationCenter.post(name: .followingStatusChanged, object: nil, userInfo: userInfo)
    }
    
    func addObserver(_ observer: Any, selector: Selector) {
        notificationCenter.addObserver(observer, selector: selector, name: .followingStatusChanged, object: nil)
    }
    
    func removeObserver(_ observer: Any) {
        notificationCenter.removeObserver(observer, name: .followingStatusChanged, object: nil)
    }
}

extension Notification.Name {
    static let followingStatusChanged = Notification.Name("followingStatusChanged")
} 
