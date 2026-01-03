//
//  ChatCoordinatorDelegate.swift
//  ChatKit
//
//  Created by Mesut Canbaz on 4.04.2025.
//

import Foundation

public protocol ChatCoordinatorDelegate: AnyObject {
    func navigateToProfile(userId: String, userName: String, isGroup: Bool, groupId: String?)
    func navigateToBack()
}

public extension ChatCoordinatorDelegate {
    
    func navigateToProfile(
        userId: String,
        userName: String,
        isGroup: Bool = false,
        groupId: String? = nil
    ) {
        DispatchQueue.main.async {
            self.navigateToProfile(
                userId: userId,
                userName: userName,
                isGroup: isGroup,
                groupId: groupId
            )
        }
    }
    
    func navigateToBack() {
        navigateToBack()
    }
}
