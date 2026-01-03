//
//  ChatUserInfoContracts.swift
//  Sporthor
//
//  Created by derTurke on 12.04.2025.
//
//

import Foundation

protocol ChatUserInfoPresenterProtocol: BasePresenterProtocol {
    var view: ChatUserInfoPresenterDelegate? { get set }
    var interactor: ChatUserInfoInteractorProtocol { get set }
    var router: ChatUserInfoRouterProtocol { get set }
    
    func viewDidLoad()
    func followButtonTapped()
    func profileButtonTapped()
    func mediaButtonTapped()
    func clearChatButtonTapped()
    func createGroupButtonTapped()
    func handleFollowingStatusChange(userId: String, isFollowing: Bool)
}

protocol ChatUserInfoPresenterDelegate: BasePresenterDelegate {
    func updateUI(with response: ChatUserInfoResponse)
    func updateFollowButton(isFollowing: Bool)
    func updateFollowStatus(userId: String, isFollowing: Bool)
}

protocol ChatUserInfoInteractorProtocol: BaseInteractorProtocol {
    var delegate: ChatUserInfoInteractorDelegate? { get set }
    func fetchUserInfo(userId: String, groupId: String) async
    func followUser(userId: String) async
    func unfollowUser(userId: String) async
}

protocol ChatUserInfoInteractorDelegate: BaseInteractorDelegate {
    func userInfoFetched(_ userInfo: ChatUserInfoResponse)
    func didFollowUser(_ userId: String)
    func didUnfollowUser(_ userId: String)
}

protocol ChatUserInfoRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ChatUserInfoRoutes)
}

enum ChatUserInfoRoutes {
    case profile(userId: String, userName: String)
    case media(groupId: String)
    case clearChat
    case createGroup
    case dismiss
}
