//
//  ProfileSettingPresenter.swift
//  Sporthor
//
//  Created by derTurke on 16.08.2025.
//
//

import Foundation

final class ProfileSettingPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ProfileSettingPresenterDelegate? {
        get { return self.baseView as? ProfileSettingPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ProfileSettingInteractorProtocol {
        get { return self.baseInteractor as! ProfileSettingInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ProfileSettingRouterProtocol {
        get { return self.baseRouter as! ProfileSettingRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: ProfileSettingPresenterDelegate,
         interactor: ProfileSettingInteractorProtocol,
         router: ProfileSettingRouterProtocol,
         delegate: ProfileSettingDelegate? = nil,
         userId: String = "") {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.profileSettingDelegate = delegate
        self.userId = userId
    }
    private weak var profileSettingDelegate: ProfileSettingDelegate?
    private var userId: String = ""
    var items: [ProfileSettingItems] = []
}

// MARK: - ProfileSettingPresenterProtocol
extension ProfileSettingPresenter: ProfileSettingPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        prepareItems()
    }
    
    private func prepareItems() {
        items = ProfileSettingItems.allCases
        view?.reloadData()
    }
    
    private func navigate(_ routes: ProfileSettingRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didSelectRow(at indexPath: IndexPath) {
        guard let selectedItem = items[safe: indexPath.row] else { return }
        switch selectedItem {
        case .blockUser:
            blockUser()
        }
    }
    
    private func blockUser() {
        guard !userId.isEmpty else { return }
        let request: [String: Any] = ["targetUserId": userId]
        Task { @MainActor in
            await interactor.blockUser(request)
        }
    }
}

// MARK: - ProfileSettingInteractorDelegate
extension ProfileSettingPresenter: ProfileSettingInteractorDelegate {
    func didBlockUser() {
        navigate(.blockUser(delegate: profileSettingDelegate))
    }
}
