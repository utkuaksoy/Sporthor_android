//
//  ProfileActionButtonsComponentViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentBaseKit
import UIKit

public protocol ProfileActionButtonsViewModelDelegate: AnyObject {
    func didUpdateFollowStatus()
}

final class ProfileActionButtonsComponentViewModel: CollectionComponentViewModel {
    
    typealias CellType = ProfileActionButtonsCell
    private weak var delegate: ProfileActionButtonsViewModelDelegate?
    var data: ProfileActionButtonsComponent.Data
    var defaultInsets: UIEdgeInsets
    
    init(
        data: ProfileActionButtonsComponent.Data,
        defaultInsets: UIEdgeInsets,
        delegate: ProfileActionButtonsViewModelDelegate?
    ) {
        self.data = data
        self.defaultInsets = defaultInsets
        self.delegate = delegate
        setupNotificationObservers()
    }
    
    deinit {
        removeNotificationObservers()
    }
    
    func size(
        _ collectionView: UICollectionView,
        at indexPath: IndexPath
    ) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: 40)
    }
    
    private func setupNotificationObservers() {
        FollowingManager.shared.addObserver(self, selector: #selector(handleFollowingStatusChange(_:)))
    }
    
    private func removeNotificationObservers() {
        FollowingManager.shared.removeObserver(self)
    }
    
    @objc
    private func handleFollowingStatusChange(_ notification: Notification) {
        guard let userInfo = notification.userInfo,
              let userId = userInfo["userId"] as? String,
              let followStatus = userInfo["followStatus"] as? ProfileActionButtonType,
              userId == data.userId
        else {
            return
        }
        
        handleFollowStatusChange(userId: userId, followStatus: followStatus)
    }
    
    func handleFollowStatusChange(userId: String, followStatus: ProfileActionButtonType) {
        guard let buttons = data.buttons,
              let index = buttons.firstIndex(where: { $0 == .follow || $0 == .following || $0 == .followRequestSent }) else {
            return
        }
        
        var updatedButtons = buttons
        updatedButtons[index] = followStatus
        data.buttons = updatedButtons
        delegate?.didUpdateFollowStatus()
    }
    
    func toggleFollowStatus() {
        guard let buttons = data.buttons,
              let index = buttons.firstIndex(where: { $0 == .follow || $0 == .following || $0 == .followRequestSent }) else {
            return
        }
        
        var updatedButtons = buttons
        var current = updatedButtons[index]
        current.toggleFollowStatus()
        updatedButtons[index] = current
        data.buttons = updatedButtons
        if let userId = data.userId {
            FollowingManager.shared.updateFollowingStatus(userId: userId, followStatus: current)
        }
    }
}
