//
//  ProfileContracts.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//
//

import UIKit

protocol ProfilePresenterProtocol: BasePresenterProtocol, ProfileDataSourceProtocols, ProfileHeaderViewDelegate {
    var view: ProfilePresenterDelegate? { get set }
    var interactor: ProfileInteractorProtocol { get set }
    var router: ProfileRouterProtocol { get set }
    var userId: String? { get }
    var userName: String? { get }

    func viewDidLoad()
    func viewWillAppear()
    func checkUserIdReturnMyUser() -> Bool
    func navigateProfileSetting()
}

protocol ProfilePresenterDelegate: BasePresenterDelegate, UIViewController {
    func refresh(with viewModel: ProfileViewModel)
    func configureHeaderView(info: ProfileInfoModel?)
    func reloadSection(with section: Int)
    func scrollTo(indexPath: IndexPath)
    func showLoading()
    func hideLoading()
}

protocol ProfileInteractorProtocol: BaseInteractorProtocol {
    var delegate: ProfileInteractorDelegate? { get set }
    var viewModel: ProfileViewModel { get }
    
    func fetchProfileService(userId: String?, isUpdate: Bool) async
    func followUser(_ userId: String) async
    func unfollowUser(_ userId: String) async
    func createGroup(
        name: String,
        selectedUserId: String,
        groupImagePath: String?,
        userName: String
    ) async
}

protocol ProfileInteractorDelegate: BaseInteractorDelegate {
    func fetchProfileServiceSuccess(response: ProfileResponseModel, isUpdate: Bool)
    func didFollowUser(_ userId: String)
    func didUnfollowUser(_ userId: String)
    func didCreateGroupSuccess(response: GroupChatResponseModel, userName: String)
}

protocol ProfileRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ProfileRoutes)
}

enum ProfileRoutes {
    case chat(userId: String, userName: String, image: String, toUserId: String?)
    case settings
    case followers(
        userId: String,
        userName: String,
        direction: FollowDirectionEnum,
        followersCount: Int,
        followingCount: Int,
        isCurrentUser: Bool
    )
    case profileEdit(delegate: ProfileEditDelegate)
    case postDetail(
        posts: [Post],
        profileUserId: String,
        initialPostId: String
    )
    case profileSettings(delegate: ProfileSettingDelegate,
                         userId: String)
    case back
}
