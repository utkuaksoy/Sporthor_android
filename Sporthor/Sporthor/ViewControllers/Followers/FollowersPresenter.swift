//
//  FollowersPresenter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import Foundation

final class FollowersPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: FollowersPresenterDelegate? {
        get { return self.baseView as? FollowersPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: FollowersInteractorProtocol {
        get { return self.baseInteractor as! FollowersInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: FollowersRouterProtocol {
        get { return self.baseRouter as! FollowersRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    var isCurrentUser: Bool = false
    
    // MARK: - Properties

    private(set) var currentTab: FollowDirectionEnum = .followers
    private(set) var followers: [FollowerModel] = []
    private(set) var following: [FollowerModel] = []
    private(set) var followTogethers: [FollowerModel] = []
    private var loadedTabs: Set<FollowDirectionEnum> = []
    private(set) var userId: String?
    private(set) var userName: String?
    private var _followingCount: Int = .zero
    private var _followersCount: Int = .zero
    private var isFirstLoad: Bool = true
    
    // MARK: - Initialize

    init(view: FollowersPresenterDelegate,
         interactor: FollowersInteractorProtocol,
         router: FollowersRouterProtocol,
         direction: FollowDirectionEnum,
         userId: String,
         userName: String,
         followersCount: Int,
         followingCount: Int,
         isCurrentUser: Bool
    ) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.currentTab = direction
        self.userId = userId
        self.userName = userName
        self._followersCount = followersCount
        self._followingCount = followingCount
        self.isCurrentUser = isCurrentUser
        self.interactor.delegate = self
    }
    
    private func configureTabs() {
        if !loadedTabs.contains(currentTab) {
            loadDataForCurrentTab()
        }
        view?.updateTabCounts(
            followers: _followersCount,
            following: _followingCount,
            mutual: .zero
        )
    }
}

// MARK: - FollowersPresenterProtocol
extension FollowersPresenter: FollowersPresenterProtocol {
    
    var followersCount: Int {
        _followersCount
    }
    
    var followingCount: Int {
        _followingCount
    }
    
    func viewDidLoad() {
        view?.configureView()
        configureTabs()
    }
    
    func viewWillAppear() {
//        if !isFirstLoad {
//            configureTabs()
//        }
    }
    
    func setCurrentTab(_ direction: FollowDirectionEnum) {
        currentTab = direction
        if !loadedTabs.contains(direction) {
            loadDataForCurrentTab()
        } else {
            view?.reloadData()
        }
    }
    
    func numberOfRows() -> Int {
        switch currentTab {
        case .followers:
            return followers.count
        case .following:
            return following.count
        case .followTogether:
            return followTogethers.count
        }
    }
    
    func cellForRow(at indexPath: IndexPath) -> FollowerModel? {
        guard indexPath.row >= 0 else { return nil }
        
        switch currentTab {
        case .followers:
            return followers[safe: indexPath.row]
        case .following:
            return following[safe: indexPath.row]
        case .followTogether:
            return followTogethers[safe: indexPath.row]
        }
    }
    
    func didSelectRow(at indexPath: IndexPath) {
        guard let follower = cellForRow(at: indexPath) else { return }
        router.handleRouter(
            .profile(
                userId: follower.id,
                userName: follower.username
            )
        )
    }
    
    func followButtonTapped(at indexPath: IndexPath) {
        guard let follower = cellForRow(at: indexPath) else { return }
        if follower.isFollow {
            Task {
                await interactor.unfollowUser(follower.id)
            }
        } else {
            Task {
                await interactor.followUser(follower.id)
            }
        }
    }
    
    func handleFollowingStatusChange(userId: String, isFollowing: Bool) {
        updateFollowStatus(userId: userId, isFollowing: isFollowing)
        view?.reloadData()
    }
    
    private func loadDataForCurrentTab() {
        view?.showLoading()
        guard let userId else { return }
        switch currentTab {
        case .followers:
            Task {
                await interactor.fetchFollowers(userId: userId)
            }
        case .following:
            Task {
                await interactor.fetchFollowing(userId: userId)
            }
        case .followTogether:
            Task {
                await interactor.fetchFollowTogether(userId: userId)
            }
        }
    }
    
    private func updateFollowStatus(userId: String, isFollowing: Bool) {
        if let index = followers.firstIndex(where: { $0.id == userId }) {
            followers[index].isFollow = isFollowing
        }
        if let index = following.firstIndex(where: { $0.id == userId }) {
            following[index].isFollow = isFollowing
        }
        if let index = followTogethers.firstIndex(where: { $0.id == userId }) {
            followTogethers[index].isFollow = isFollowing
        }
    }
}

// MARK: - FollowersInteractorDelegate
extension FollowersPresenter: FollowersInteractorDelegate {
    
    func didFetchFollowers(_ followers: [FollowerModel]) {
        DispatchQueue.main.async {
            self.loadedTabs.insert(.followers)
            self.followers = followers
            self.view?.reloadData()
            self.view?.hideLoading()
        }
    }
    
    func didFetchFollowing(_ following: [FollowerModel]) {
        DispatchQueue.main.async {
            self.loadedTabs.insert(.following)
            self.following = following
            self.view?.reloadData()
            self.view?.hideLoading()
        }
    }
    
    func didFetchFollowTogether(_ followTogethers: [FollowerModel]) {
        DispatchQueue.main.async {
            self.loadedTabs.insert(.followTogether)
            self.followTogethers = followTogethers
            self.view?.reloadData()
            self.view?.hideLoading()
        }
    }
    
    func didFollowUser(_ userId: String) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            self.updateFollowStatus(userId: userId, isFollowing: true)

            self.view?.reloadData()
        }
    }
    
    func didUnfollowUser(_ userId: String) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            self.updateFollowStatus(userId: userId, isFollowing: false)
            self.view?.reloadData()
        }
    }
    
    func didFailure(_ error: Error) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            view?.hideLoading()
            view?.showError(error)
        }
    }
}

