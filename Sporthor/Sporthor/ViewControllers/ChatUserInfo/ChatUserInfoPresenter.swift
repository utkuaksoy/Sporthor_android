//
//  ChatUserInfoPresenter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import ChatKit
import ChatCoordinator
import Factory
import Foundation

final class ChatUserInfoPresenter: BasePresenter {
    weak var view: ChatUserInfoPresenterDelegate? {
        get { return self.baseView as? ChatUserInfoPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor:  ChatUserInfoInteractorProtocol {
        get { return self.baseInteractor as!  ChatUserInfoInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router:  ChatUserInfoRouterProtocol {
        get { return self.baseRouter as!  ChatUserInfoRouterProtocol }
        set { self.baseRouter = newValue }
    }
    private var userId: String?
    private var groupId: String?
    private var isFollowing: Bool = false
    private var response: ChatUserInfoResponse?
    
    // MARK: - Initialize

    init(view: ChatUserInfoPresenterDelegate,
         interactor: ChatUserInfoInteractorProtocol,
         router: ChatUserInfoRouterProtocol,
         userId: String,
         groupId: String
    ) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.userId = userId
        self.groupId = groupId
        self.interactor.delegate = self
    }
}

// MARK: - ChatUserInfoPresenterProtocol
extension ChatUserInfoPresenter: ChatUserInfoPresenterProtocol {
    
    func viewDidLoad() {
        guard let userId, let groupId else { return }
        Task {
            await interactor.fetchUserInfo(userId: userId, groupId: groupId)
        }
    }
    
    func followButtonTapped() {
        guard let userId else { return }
        Task {
            if isFollowing {
                await interactor.unfollowUser(userId: userId)
            } else {
                await interactor.followUser(userId: userId)
            }
        }
    }
    
    func profileButtonTapped() {
        guard let userId, let userName = response?.userName else { return }
        DispatchQueue.main.async {
            self.router.handleRouter(.profile(userId: userId, userName: userName))
        }
    }
    
    func mediaButtonTapped() {
        guard let groupId else { return }
        self.router.handleRouter(.media(groupId: groupId))
    }
    
    func clearChatButtonTapped() {
        
    }
    
    func createGroupButtonTapped() {
        
    }
    
    func handleFollowingStatusChange(userId: String, isFollowing: Bool) {
        guard userId == response?.userId else { return }
        view?.updateFollowStatus(userId: userId, isFollowing: isFollowing)
    }
}

// MARK: - ChatUserInfoInteractorDelegate
extension ChatUserInfoPresenter: ChatUserInfoInteractorDelegate {
    func didFollowUser(_ userId: String) {
        isFollowing = true
        Task {
            view?.updateFollowButton(isFollowing: isFollowing)
        }
    }
    
    func didUnfollowUser(_ userId: String) {
        isFollowing = false
        Task {
            view?.updateFollowButton(isFollowing: isFollowing)
        }
        
    }
    
    func userInfoFetched(_ userInfo: ChatUserInfoResponse) {
        self.response = userInfo
        Task {
            isFollowing = userInfo.isFollow
            view?.updateUI(with: userInfo)
            view?.updateFollowButton(isFollowing: userInfo.isFollow)
        }
    }
}
