//
//  FollowersContracts.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import UIKit
import Foundation

// MARK: - Interactor
protocol FollowersInteractorProtocol: BaseInteractorProtocol {
    var delegate: FollowersInteractorDelegate? { get set }
    func fetchFollowers(userId: String) async
    func fetchFollowing(userId: String) async
    func fetchFollowTogether(userId: String) async
    func followUser(_ userId: String) async
    func unfollowUser(_ userId: String) async
}

protocol FollowersInteractorDelegate: BaseInteractorDelegate {
    func didFetchFollowers(_ followers: [FollowerModel])
    func didFetchFollowing(_ following: [FollowerModel])
    func didFetchFollowTogether(_ followTogethers: [FollowerModel])
    func didFollowUser(_ userId: String)
    func didUnfollowUser(_ userId: String)
    func didFailure(_ error: Error)
}

// MARK: - Presenter
protocol FollowersPresenterProtocol: BasePresenterProtocol {
    var currentTab: FollowDirectionEnum { get }
    var followers: [FollowerModel] { get }
    var following: [FollowerModel] { get }
    var followTogethers: [FollowerModel] { get }
    var userId: String? { get }
    var userName: String? { get }
    var followersCount: Int { get }
    var followingCount: Int { get }
    var isCurrentUser: Bool { get }
    
    func setCurrentTab(_ direction: FollowDirectionEnum)
    func numberOfRows() -> Int
    func cellForRow(at indexPath: IndexPath) -> FollowerModel?
    func didSelectRow(at indexPath: IndexPath)
    func followButtonTapped(at indexPath: IndexPath)
    func viewDidLoad()
    func viewWillAppear()
    func handleFollowingStatusChange(userId: String, isFollowing: Bool)
}

protocol FollowersPresenterDelegate: BasePresenterDelegate {
    func configureView()
    func showLoading()
    func hideLoading()
    func showError(_ error: Error)
    func reloadData()
    func reloadRow(index: Int)
    func updateTabCounts(followers: Int, following: Int, mutual: Int)
}

// MARK: - Router
protocol FollowersRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ routes: FollowersRoutes)
}

// MARK: - Routes
enum FollowersRoutes {
    case profile(userId: String, userName: String)
}

// MARK: - Direction
enum FollowDirectionEnum: Int, CaseIterable {
    case followers = 0
    case following
    case followTogether
    
    var title: String {
        switch self {
        case .followers:
            return "Takipçi"
        case .following:
            return "Takip"
        case .followTogether:
            return "Ortak"
        }
    }
} 
