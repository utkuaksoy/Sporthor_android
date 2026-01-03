//
//  ProfilePresenter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//
//

import ComponentBaseKit
import ChatCoordinator
import ChatKit
import Factory
import IQKeyboardManagerSwift
import UIKit
import Combine

final class ProfilePresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ProfilePresenterDelegate? {
        get { return self.baseView as? ProfilePresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ProfileInteractorProtocol {
        get { return self.baseInteractor as! ProfileInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ProfileRouterProtocol {
        get { return self.baseRouter as! ProfileRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    private(set) var userId: String?
    private(set) var userName: String?
    private var cancellables = Set<AnyCancellable>()
    
    // MARK: - Initialize
    init(view: ProfilePresenterDelegate,
         interactor: ProfileInteractorProtocol,
         router: ProfileRouterProtocol,
         userId: String?,
         userName: String?
    ) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.userId = userId
        self.userName = userName
        self.interactor.delegate = self
        setupSubscriptions()
    }
    
    private func setupSubscriptions() {
        CreatePostDetailPresenter.postCreated
            .sink { [weak self] _ in
                self?.updateProfile()
            }
            .store(in: &cancellables)
    }
}

// MARK: - ProfilePresenterProtocol
extension ProfilePresenter: ProfilePresenterProtocol {
    func viewDidLoad() {
        Task {
            await interactor.fetchProfileService(userId: userId, isUpdate: false)
        }
    }
    
    func viewWillAppear() {
    }
    
    func updateProfile() {
        Task {
            await interactor.fetchProfileService(userId: userId, isUpdate: true)
        }
    }

    private func navigate(_ routes: ProfileRoutes) {
        router.handleRouter(routes)
    }
    
    func checkUserIdReturnMyUser() -> Bool {
        guard let userId, !userId.isEmpty else { return false }
        if ApplicationContext.shared.authResponse?.userId == userId {
            return true
        } else {
            return false
        }
    }
    
    func navigateProfileSetting() {
        navigate(.profileSettings(delegate: self, userId: userId ?? ""))
    }
}

// MARK: - ProfileInteractorDelegate
extension ProfilePresenter: ProfileInteractorDelegate {
    func fetchProfileServiceSuccess(response: ProfileResponseModel, isUpdate: Bool) {
        let items: [any CollectionComponentViewModel] = response.components.map { [weak self] item in
            item.viewModel(delegate: self, defaultInsets: .init(top: 16, left: .zero, bottom: .zero, right: .zero))
        }
        interactor.viewModel.update(with: items)
        if isUpdate {
            interactor.viewModel.getSegmentComponentViewModel?.updateViewModels()
        }
        DispatchQueue.main.async {
            self.view?.configureHeaderView(info: self.interactor.viewModel.profileInfo)
            self.view?.refresh(with: self.interactor.viewModel)
        }
    }
    
    func didFollowUser(_ userId: String) {
        
    }
    
    func didUnfollowUser(_ userId: String) {
        
    }
    
    func didCreateGroupSuccess(response: GroupChatResponseModel, userName: String) {
        DispatchQueue.main.async {
            self.navigate(
                .chat(
                    userId: response.id,
                    userName: userName,
                    image: response.image,
                    toUserId: response.toUserId
                )
            )
        }
    }
}

// MARK: - Profile DataSource Delegates

extension ProfilePresenter: ProfileDataSourceProtocols {
    
    func didTapFollowerView() {
        guard let info = interactor.viewModel.profileInfo else { return }
        if let profileInfoComponentViewModel = interactor.viewModel.profileInfoComponentViewModel {
            navigate(
                .followers(
                    userId: info.id,
                    userName: info.username,
                    direction: .followers,
                    followersCount: profileInfoComponentViewModel.follewerCount,
                    followingCount: profileInfoComponentViewModel.followingCount,
                    isCurrentUser: info.isCurrentUser
                )
            )
        }
    }
    
    func didTapFollowingView() {
        guard let info = interactor.viewModel.profileInfo else {
            return
        }
        if let profileInfoComponentViewModel = interactor.viewModel.profileInfoComponentViewModel {
            navigate(
                .followers(
                    userId: info.id,
                    userName: info.username,
                    direction: .following,
                    followersCount: profileInfoComponentViewModel.follewerCount,
                    followingCount: profileInfoComponentViewModel.followingCount,
                    isCurrentUser: info.isCurrentUser
                )
            )
        }
    }
    
    func didTapPostStatView() {
        guard let indexPath = interactor.viewModel.segmentComponentIndex else { return }
        if let segmentComponent = interactor.viewModel.getSegmentComponentViewModel {
            if let postsSegment = segmentComponent.segments.first(where: { $0.type == .posts }) {
                segmentComponent.switchToSegment(postsSegment)
            }
        }
        view?.scrollTo(indexPath: indexPath)
    }
    
    func didTapPost(selectedPostId: String) {
        if let segmentComponentViewModel = interactor.viewModel.getSegmentComponentViewModel {
            if let postViewModel = segmentComponentViewModel.getViewModel(for: .posts) as? PostsViewModel {
                let posts = postViewModel.posts
                let userId = segmentComponentViewModel.data.userId
                router.handleRouter(.postDetail(posts: posts, profileUserId: userId, initialPostId: selectedPostId))
            }
        }
    }
    
    func didTapButton(ofType type: ProfileActionButtonType) {
        guard let info = interactor.viewModel.profileInfo else { return }
        switch type {
        case .follow:
            followUser()
        case .following:
            unFollowUser()
        case .followRequestSent:
            unFollowUser()
        case .message:
            Task {
                await interactor.createGroup(
                    name: info.name,
                    selectedUserId: info.id,
                    groupImagePath: info.avatar,
                    userName: info.username
                )
            }
        case .invite:
            break
        case .editProfile:
            navigate(.profileEdit(delegate: self))
        }
    }
    
    private func followUser() {
        guard let userId = interactor.viewModel.profileInfo?.id else { return }
        Task {
            await interactor.followUser(userId)
        }
    }
    
    private func unFollowUser() {
        guard let userId = interactor.viewModel.profileInfo?.id else { return }
        Task {
            await interactor.unfollowUser(userId)
        }
    }
}

// MARK: - Profile HeaderView Delegates

extension ProfilePresenter {
    func didTapSettingsButton() {
        router.handleRouter(.settings)
    }
}

extension ProfilePresenter: ProfileComponentsContracts.Components.ViewModelDelegates {
    func didUpdateFollowStatus() {
        DispatchQueue.main.async { [weak self] in
            guard let indexPath = self?.interactor.viewModel.actionButtonsComponentIndex else { return }
            self?.view?.reloadSection(with: indexPath.section)
        }
    }
    
    func contentSizeChanged() {
        DispatchQueue.main.async { [weak self] in
            guard let indexPath = self?.interactor.viewModel.segmentComponentIndex else { return }
            self?.view?.reloadSection(with: indexPath.section)
        }
    }
    
    func showSegmentLoading() {
        view?.showLoading()
    }
    
    func hideSegmentLoading() {
        view?.hideLoading()
    }
}

// MARK: - ProfileEditDelegate
extension ProfilePresenter: ProfileEditDelegate {
    func didSuccessProfileEdit() {
        updateProfile()
    }
}

extension ProfilePresenter: ProfileSettingDelegate {
    func didBlockUser() {
        showAlert(delegate: self,
                  type: .success,
                  message: (userName ?? "") + " kullanıcı adlı kişiyi engellediniz.")
    }
}

extension ProfilePresenter: AlertViewDelegate {
    func didTappedAlertButton(_ tag: Int) {
        navigate(.back)
    }
}
