//
//  Homev2Contracts.swift
//  Sporthor
//
//  Created by derTurke on 25.06.2025.
//
//

import Foundation

protocol Homev2PresenterProtocol: BasePresenterProtocol {
    var view: Homev2PresenterDelegate? { get set }
    var interactor: Homev2InteractorProtocol { get set }
    var router: Homev2RouterProtocol { get set }
    var homeModeType: HomeModeType { get set }
    var posts: [Post] { get set }
    var stories: [Story] { get set }
    
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

protocol Homev2PresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func reloadData()
    func didEndRefreshing()
    func didChangeLikePost(_ post: Post, at indexPath: IndexPath)
    func configureNavigationBar()
    func scrollToItem(at indexPath: IndexPath)
}

protocol Homev2InteractorProtocol: BaseInteractorProtocol {
    var delegate: Homev2InteractorDelegate? { get set }
    
    func getInfo() async
    func getPosts(page: Int, pageSize: Int) async
    func getStories() async
    func likePost(id: String) async
    func unlikePost(id: String) async
    func getUserPosts(userId: String, page: Int, pageSize: Int) async
}

protocol Homev2InteractorDelegate: BaseInteractorDelegate {
    func didGetPosts(_ posts: [Post])
    func didGetStories(_ stories: [Story])
    func didLikeUnlikePost(_ response: LikeAndUnlikeResponse)
}

protocol Homev2RouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: Homev2Routes)
}

enum Homev2Routes {
    case profile(username: String, userId: String)
    case addStory(previewDelegate: PreviewDelegate?)
    case story(delegate: StoryDelegate?, model: Story)
    case comment(delegate: CommentViewDelegate, postId: String)
    case postSetting(delegate: PostSettingDelegate?, model: Post)
    case back
    case calendarMain
    case menu
    case notification
}
