//
//  Homev2Presenter.swift
//  Sporthor
//
//  Created by derTurke on 25.06.2025.
//
//

import Foundation
import CommonKit

final class Homev2Presenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: Homev2PresenterDelegate? {
        get { return self.baseView as? Homev2PresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: Homev2InteractorProtocol {
        get { return self.baseInteractor as! Homev2InteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: Homev2RouterProtocol {
        get { return self.baseRouter as! Homev2RouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: Homev2PresenterDelegate,
         interactor: Homev2InteractorProtocol,
         router: Homev2RouterProtocol,
         homeModeType: HomeModeType,
         posts: [Post],
         profileUserId: String?,
         initialPostId: String?) {
        self.homeModeType = homeModeType
        self.posts = posts
        self.profileUserId = profileUserId
        self.initialPostId = initialPostId
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var homeModeType: HomeModeType
    var posts: [Post] = []
    private var profileUserId: String?
    private var initialPostId: String?
    var stories: [Story] = []
    private var selectedIndexPath: IndexPath?
    
    private var currentPage: Int = 1
    private let pageSize: Int = 10
    private var isLoading: Bool = false
    private var isLastPage: Bool = false
}

// MARK: - Homev2PresenterProtocol
extension Homev2Presenter: Homev2PresenterProtocol {
    func viewDidLoad() {
        ApplicationContext.shared.isLogin = true
        prepareNavigationBar()
        setTitle()
        view?.prepareUI()
        if homeModeType == .home {
            getTask()
        } else {
            currentPage = posts.count / pageSize
            view?.reloadData()
        }
        
        if homeModeType == .profile, let initialPostId {
            scrollToPost(with: initialPostId)
        }
    }
    
    private func navigate(_ routes: Homev2Routes) {
        router.handleRouter(routes)
    }
    
    private func prepareNavigationBar() {
        guard homeModeType != .home else { return }
        view?.didSetTitle("Gönderiler")
    }
    
    private func setTitle() {
        guard homeModeType != .home else { return }
        view?.configureNavigationBar()
    }
    
    private func getTask() {
        if homeModeType == .home {
            getStories()
        }
        getPosts(page: 1)
    }
    
    private func getStories() {
        Task { @MainActor in
            await interactor.getStories()
        }
    }
    
    private func getPosts(page: Int = 1) {
        guard !isLoading else { return }
        isLoading = true
        Task { @MainActor in
            if homeModeType == .home {
                await interactor.getPosts(page: page,
                                          pageSize: pageSize)
            } else if homeModeType == .profile, let userId = profileUserId {
                await interactor.getUserPosts(userId: userId,
                                              page: page,
                                              pageSize: pageSize)
            }
        }
    }
    
    func openProfile(username: String, userId: String) {
        guard homeModeType == .home else { return }
        navigate(.profile(username: username, userId: userId))
    }
    
    func didTappedActionButton(tag: Int, indexPath: IndexPath) {
        self.selectedIndexPath = indexPath
        switch tag {
        case 0:
            changeLikedPost(indexPath: indexPath)
        case 1:
            navigate(.comment(delegate: self, postId: posts[indexPath.item].id))
        default:
            break
        }
    }
    
    private func changeLikedPost(indexPath: IndexPath) {
        guard posts.indices.contains(indexPath.item),
              let user = ApplicationContext.shared.authResponse else { return }
        
        var post = posts[indexPath.item]
        post.isLiked.toggle()
        
        if post.isLiked {
            guard let userId = user.userId,
                  let userName = user.userName else { return }
            
            let likedUser = PostLastLikedUser(
                userId: userId,
                username: userName,
                profileImageUrl: user.profilePhoto ?? ""
            )
            post.lastLikedUsers.append(likedUser)
            
            Task { @MainActor in
                await interactor.likePost(id: post.id)
            }
        } else {
            guard let userId = user.userId else { return }
            post.lastLikedUsers.removeAll { $0.userId == userId }
            
            Task { @MainActor in
                await interactor.unlikePost(id: post.id)
            }
        }
        posts[indexPath.item] = post
        view?.didChangeLikePost(post, at: indexPath)
    }
    
    func openAddStory() {
        navigate(.addStory(previewDelegate: self))
    }
    
    func didPullToRefresh() {
        currentPage = 1
        isLastPage = false
        posts.removeAll()
        getTask()
    }
    
    func didTapStoryProfile(_ model: Story) {
        if model.isOwn && model.details.isEmpty {
            navigate(.addStory(previewDelegate: self))
        } else {
            navigate(.story(delegate: self, model: model))
        }
    }
    
    func didReachEndOfPosts(indexPath: IndexPath) {
        guard let section = HomeSection(rawValue: indexPath.section),
              section == .posts,
              indexPath.item == posts.count - 1,
              !isLoading,
              !isLastPage else { return }
        currentPage += 1
        getPosts(page: currentPage)
    }
    
    func openPostSetting(_ model: Post) {
        navigate(.postSetting(delegate: self, model: model))
    }
    
    func didTappedBackButton() {
        navigate(.back)
    }
    
    private func scrollToPost(with postId: String) {
        guard let index = posts.firstIndex(where: { $0.id == postId }) else { return }
        let indexPath = IndexPath(item: index, section: HomeSection.posts.rawValue)
        view?.scrollToItem(at: indexPath)
    }
    
    func didTappedHomeHeaderIcon(_ tag: Int) {
        switch tag {
        case 1: // Calendar
            navigate(.calendarMain)
        case 2: // Notification
            navigate(.notification)
        case -99:
            navigate(.menu)
        default:
            break
        }
    }
}

// MARK: - Homev2InteractorDelegate
extension Homev2Presenter: Homev2InteractorDelegate {
    func didGetPosts(_ posts: [Post]) {
        if currentPage == 1 {
            self.posts = posts
        } else {
            self.posts.append(contentsOf: posts)
        }
        
        if posts.count < pageSize {
            isLastPage = true
        }
        
        isLoading = false
        view?.didEndRefreshing()
        view?.reloadData()
    }
    
    func didGetStories(_ stories: [Story]) {
        self.stories = stories
        view?.reloadData()
    }
    
    func didLikeUnlikePost(_ response: LikeAndUnlikeResponse) {
        guard let selectedIndexPath,
              var post = posts[safe: selectedIndexPath.item] else { return }
        post.isLiked = response.isLiked
        post.likeCount = response.totalLikedCount
        posts[selectedIndexPath.item] = post
        
        view?.didChangeLikePost(post, at: selectedIndexPath)
    }
}

extension Homev2Presenter: PreviewDelegate {
    func didAddStory() {
        getStories()
    }
}

extension Homev2Presenter: StoryDelegate {
    func storyActionDidFinish(_ story: Story, actionType: StoryActionType?) {
        guard let actionType,
              let currentIndex = stories.firstIndex(where: { $0.userId == story.userId }) else {
            return
        }
        
        var nextIndex: Int?
        var direction: SwipeDirection?
        
        switch actionType {
        case .swipeRight:
            nextIndex = currentIndex + 1
            direction = .right
        case .swipeLeft:
            nextIndex = currentIndex - 1
            direction = .left
        case .dismiss:
            dismissCurrentStory()
            return
        }
        
        guard let index = nextIndex,
              stories.indices.contains(index),
              !stories[index].details.isEmpty else {
            dismissCurrentStory()
            return
        }
        
        let nextStory = stories[index]
        
        if let topVC = BaseHelper.shared.currentViewController() as? StoryViewController,
           let storyPresenter = topVC.presenter as? StoryPresenter,
           let direction {
            storyPresenter.update(with: nextStory, direction: direction)
        } else {
            navigate(.story(delegate: self, model: nextStory))
        }
    }
    
    private func dismissCurrentStory() {
        if let topVC = BaseHelper.shared.currentViewController() as? StoryViewController,
           let storyPresenter = topVC.presenter as? StoryPresenter {
            storyPresenter.dismiss()
        }
    }
    
    func didWatchedStory(_ story: Story, index: Int) {
        guard let storyIndex = stories.firstIndex(where: { $0.userId == story.userId }),
              stories.indices.contains(storyIndex),
              stories[storyIndex].details.indices.contains(index) else { return }
        stories[storyIndex].details[index].isWatched = true
    }
    
    func didDismissStories() {
        getStories()
    }
}

extension Homev2Presenter: PostSettingDelegate {
    func dismissHideOrRemove(model: Post) {
        guard let index = posts.firstIndex(where: { $0.id == model.id }) else { return }
        posts.remove(at: index)
        view?.reloadData()
    }
    
    func didBlockUser(model: Post) {
        getPosts()
    }
}

extension Homev2Presenter: CommentViewDelegate {
    func reloadCommentCount(commentCount: Int, postId: String) {
        guard let index = posts.firstIndex(where: { $0.id == postId }) else { return }
        posts[index].commentCount = commentCount
        view?.reloadData()
    }
}
