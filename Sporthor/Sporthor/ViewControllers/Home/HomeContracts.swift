//
//  HomeContracts.swift
//  Sporthor
//
//  Created by derTurke on 9.03.2025.
//
//

import UIKit

protocol HomePresenterProtocol: BasePresenterProtocol {
    var view: HomePresenterDelegate? { get set }
    var interactor: HomeInteractorProtocol { get set }
    var router: HomeRouterProtocol { get set }
    var homeModeType: HomeModeType { get set }
    
    func viewDidLoad()
    func openProfile(username: String, userId: String)
    func didTappedActionButton(tag: Int, indexPath: IndexPath)
    func openAddStory()
    func didPullToRefresh()
    func didTapStoryProfile(_ model: Story)
    func didReachEndOfPosts(indexPath: IndexPath)
    func openPostSetting(_ model: Post)
    func didTappedBackButton()
    func didTappedHomeHeaderIcon(_ tag: Int)
}

protocol HomePresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func didApplySnapshot(_ snapshot: NSDiffableDataSourceSnapshot<HomeSection, HomeItem>)
    func didEndRefreshing()
    func didChangeLikePost(_ post: Post, at indexPath: IndexPath)
    func configureNavigationBar()
    func scrollToItem(at indexPath: IndexPath)
}

protocol HomeInteractorProtocol: BaseInteractorProtocol {
    var delegate: HomeInteractorDelegate? { get set }
    
    func getInfo() async
    func getPosts(page: Int, pageSize: Int) async
    func getStories() async
    func likePost(id: String) async
    func unlikePost(id: String) async
    func getUserPosts(userId: String, page: Int, pageSize: Int) async
}

protocol HomeInteractorDelegate: BaseInteractorDelegate {
    func didGetPosts(_ posts: [Post])
    func didGetStories(_ stories: [Story])
    func didLikeUnlikePost(_ response: LikeAndUnlikeResponse)
}

protocol HomeRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: HomeRoutes)
}

enum HomeRoutes {
    case profile(username: String, userId: String)
    case addStory(previewDelegate: PreviewDelegate?)
    case story(delegate: StoryDelegate?, model: Story)
    case comment(postId: String)
    case postSetting(delegate: PostSettingDelegate?, model: Post)
    case back
    case calendarMain
    case menu
}

// MARK: - HomeSection Enum
enum HomeSection: Int, CaseIterable {
    case header = 0
    case stories
    case posts
}

// MARK: - HomeItem Enum
enum HomeItem: Hashable {
    case header(HeaderModel)
    case story(Story)
    case post(Post)
    
    static func == (lhs: HomeItem, rhs: HomeItem) -> Bool {
        switch (lhs, rhs) {
        case (.header(let a), .header(let b)): return a == b
        case (.story(let a), .story(let b)):
            return a.userId == b.userId &&
            a.details.count == b.details.count &&
            a.isWatched == b.isWatched
        case (.post(let a), .post(let b)):
            return a.id == b.id &&
            a.likeCount == b.likeCount &&
            a.commentCount == b.commentCount &&
            a.isLiked == b.isLiked &&
            a.lastLikedUsers.count == b.lastLikedUsers.count
        default: return false
        }
    }
    
    func hash(into hasher: inout Hasher) {
        switch self {
        case .header(let model): hasher.combine(model)
        case .story(let model):
            hasher.combine(model.userId)
            hasher.combine(model.details.count)
        case .post(let model):
            hasher.combine(model.id)
            hasher.combine(model.likeCount)
            hasher.combine(model.commentCount)
            hasher.combine(model.isLiked)
            hasher.combine(model.lastLikedUsers.count)
        }
    }
}


enum HomeModeType {
    case home
    case profile
}
