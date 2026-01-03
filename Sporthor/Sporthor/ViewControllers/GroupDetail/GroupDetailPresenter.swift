//
//  GroupDetailPresenter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.04.2025.
//
//

import Foundation

enum GroupDetailSection: Int, CaseIterable {
    case header
    case members
    case footer
}

final class GroupDetailPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: GroupDetailPresenterDelegate? {
        get { return self.baseView as? GroupDetailPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: GroupDetailInteractorProtocol {
        get { return self.baseInteractor as! GroupDetailInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: GroupDetailRouterProtocol {
        get { return self.baseRouter as! GroupDetailRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Private Properties
    private var memberList: [FollowerModel]?
    private var groupDetail: GroupDetailResponseModel?
    private let groupId: String
    private let groupName: String
    private let groupImage: String
    
    // MARK: - Public Properties
    var members: [FollowerModel]? {
        memberList
    }
    
    var groupDetailResponse: GroupDetailResponseModel? {
        groupDetail
    }
    
    // MARK: - Initialize
    init(
        view: GroupDetailPresenterDelegate,
        interactor: GroupDetailInteractorProtocol,
        router: GroupDetailRouterProtocol,
        groupId: String,
        groupName: String,
        groupImage: String
    ) {
        self.groupId = groupId
        self.groupName = groupName
        self.groupImage = groupImage
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
}

// MARK: - GroupDetailPresenterProtocol

extension GroupDetailPresenter: GroupDetailPresenterProtocol {
    func followButtonTapped(at indexPath: IndexPath) {
        guard var member = memberList?[safe: indexPath.item] else { return }

        if member.isFollow {
            Task {
                await interactor.unfollowUser(member.id)
            }
        } else {
            Task {
                await interactor.followUser(member.id)
            }
        }
    }
    
    func viewDidLoad() {
        view?.configureViews()
        configureGroupDetails()
        fetchGroupDetailService(with: groupId)
    }
    
    func didTapEditButton() {
        router.handleRouter(.editGroup(groupId: groupId))
    }
    
    func didTapLeaveGroup() {
        Task {
            await interactor.leaveGroup()
        }
    }
    
    func didTapMediaButton() {
        router.handleRouter(.mediaAndDocument(groupId: groupId))
    }
    
    func didTapRemoveMember(at indexPath: IndexPath) {
        guard let userId = members?[indexPath.row].id else { return }
        Task {
            await interactor.removeMember(userId: userId)
        }
    }
    
    func didTapMemberProfile(at indexPath: IndexPath) {
        guard let member = members?[indexPath.row] else { return }
        router.handleRouter(.profile(userId: member.id, userName: member.username))
    }
    
    func handleFollowingStatusChange(userId: String, isFollowing: Bool) {
        guard var list = memberList,
              let index = memberList?.firstIndex(where: { $0.id == userId }) else { return }

        list[index].isFollow = isFollowing
        memberList = list

        let indexPath = IndexPath(row: index, section: GroupDetailSection.members.rawValue)
        view?.updateFollowStatus(at: indexPath, isFollowing: isFollowing)
    }
}

// MARK: - Private Methods

private extension GroupDetailPresenter {
    func fetchGroupDetailService(with groupId: String) {
        Task {
            await interactor.fetchGroupDetailService(with: groupId)
        }
    }
    
    func configureGroupDetails() {
        view?.configureGroupDetail()
       
    }
}

// MARK: - GroupDetailInteractorDelegate

extension GroupDetailPresenter: GroupDetailInteractorDelegate {
    func fetchGroupDetailSuccess(groupDetailResponse: GroupDetailResponseModel) {
        self.groupDetail = groupDetailResponse
        self.memberList = groupDetailResponse.members
        DispatchQueue.main.async {
            self.configureGroupDetails()
            self.view?.reloadData()
        }
    }
    
    func fetchGroupMembersError(error: String?) {
        showAlert(type: .error, message: error ?? "")
    }
    
    func leaveGroupSuccess() {
        DispatchQueue.main.async {
            self.router.handleRouter(.returnMessages)
        }
    }
    
    func removeMemberSuccess(at indexPath: IndexPath) {
        memberList?.remove(at: indexPath.row)
        DispatchQueue.main.async {
            self.configureGroupDetails()
            self.view?.removeRow(for: indexPath)
        }
    }
    
    func didFollowUser(_ userId: String) {
        guard var list = memberList,
              let index = memberList?.firstIndex(where: { $0.id == userId }) else { return }

        list[index].isFollow = true
        memberList = list

        let indexPath = IndexPath(row: index, section: GroupDetailSection.members.rawValue)
        view?.updateFollowStatus(at: indexPath, isFollowing: true)
    }
    
    func didUnfollowUser(_ userId: String) {
        guard var list = memberList,
              let index = memberList?.firstIndex(where: { $0.id == userId }) else { return }

        list[index].isFollow = false
        memberList = list

        let indexPath = IndexPath(row: index, section: GroupDetailSection.members.rawValue)
        view?.updateFollowStatus(at: indexPath, isFollowing: false)
    }
}
