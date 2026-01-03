//
//  GroupDetailPresenterProtocol.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.04.2025.
//
//

import Foundation

protocol GroupDetailPresenterProtocol: BasePresenterProtocol, FollowerCellDelegate {
    var view: GroupDetailPresenterDelegate? { get set }
    var interactor: GroupDetailInteractorProtocol { get set }
    var router: GroupDetailRouterProtocol { get set }
    
    var groupDetailResponse: GroupDetailResponseModel? { get }
    var members: [FollowerModel]? { get }
    
    func viewDidLoad()
    func didTapEditButton()
    func didTapLeaveGroup()
    func didTapMediaButton()
    func didTapRemoveMember(at indexPath: IndexPath)
    func didTapMemberProfile(at indexPath: IndexPath)
    func handleFollowingStatusChange(userId: String, isFollowing: Bool)
}

protocol GroupDetailPresenterDelegate: BasePresenterDelegate {
    func configureViews()
    func reloadData()
    func removeRow(for indexPath: IndexPath)
    func configureGroupDetail()
    func updateFollowStatus(at indexPath: IndexPath, isFollowing: Bool)
}

protocol GroupDetailInteractorProtocol: BaseInteractorProtocol {
    var delegate: GroupDetailInteractorDelegate? { get set }
    
    func fetchGroupDetailService(with groupId: String) async
    func leaveGroup() async
    func removeMember(userId: String) async
    func followUser(_ userId: String) async
    func unfollowUser(_ userId: String) async
}

protocol GroupDetailInteractorDelegate: BaseInteractorDelegate {
    func fetchGroupDetailSuccess(groupDetailResponse: GroupDetailResponseModel)
    func leaveGroupSuccess()
    func removeMemberSuccess(at indexPath: IndexPath)
    func didFollowUser(_ userId: String)
    func didUnfollowUser(_ userId: String)
}

protocol GroupDetailRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: GroupDetailRoutes)
}

enum GroupDetailRoutes {
    case editGroup(groupId: String)
    case profile(userId: String, userName: String)
    case returnMessages
    case mediaAndDocument(groupId: String)
}
