//
//  PostSettingPresenter.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//
//

import Foundation

final class PostSettingPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: PostSettingPresenterDelegate? {
        get { return self.baseView as? PostSettingPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: PostSettingInteractorProtocol {
        get { return self.baseInteractor as! PostSettingInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: PostSettingRouterProtocol {
        get { return self.baseRouter as! PostSettingRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: PostSettingPresenterDelegate,
         interactor: PostSettingInteractorProtocol,
         router: PostSettingRouterProtocol,
         delegate: PostSettingDelegate?,
         model: Post) {
        self.postSettingDelegate = delegate
        self.model = model
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    private weak var postSettingDelegate: PostSettingDelegate?
    private var model: Post
    var items: [PostSettingItems] = []
}

// MARK: - PostSettingPresenterProtocol
extension PostSettingPresenter: PostSettingPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        prepareItems()
    }
    
    private func prepareItems() {
        items = PostSettingItems.allCases
        if ApplicationContext.shared.authResponse?.userName != model.username {
            items.removeAll { $0 == .remove }
        }
        
        if ApplicationContext.shared.authResponse?.userName == model.username {
            items.removeAll { $0 == .blockUser }
        }
        
        view?.reloadData()
    }
    
    private func navigate(_ routes: PostSettingRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didSelectRow(at indexPath: IndexPath) {
        guard let selectedItem = items[safe: indexPath.row] else { return }
        switch selectedItem {
        case .complain:
            navigate(.complain(delegate: self, post: model))
        case .hide:
            hidePost()
        case .blockUser:
            blockUser()
        case .remove:
            deletePost()
        }
    }
    
    private func hidePost() {
        let request: [String: Any] = ["postId": model.id]
        Task { @MainActor in
            await interactor.hidePost(request)
        }
    }
    
    private func deletePost() {
        let request: [String: Any] = ["id": model.id]
        Task { @MainActor in
            await interactor.deletePost(request)
        }
    }
    
    private func blockUser() {
        let request: [String: Any] = ["targetUserId": model.userId]
        Task { @MainActor in
            await interactor.blockUser(request)
        }
    }
}

// MARK: - PostSettingInteractorDelegate
extension PostSettingPresenter: PostSettingInteractorDelegate {
    func didHideOrDeletePost() {
        navigate(.dismissHideOrRemove(delegate: postSettingDelegate, post: model))
    }
    
    func didBlockUser() {
        navigate(.blockUser(delegate: postSettingDelegate, post: model))
    }
}

extension PostSettingPresenter: ComplainDelegate {
    func dismissComplain() {
        navigate(.dismissHideOrRemove(delegate: postSettingDelegate, post: model))
    }
}
