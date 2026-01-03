//
//  AccountBlockUserListPresenter.swift
//  Sporthor
//
//  Created by derTurke on 15.08.2025.
//
//

import Foundation

final class AccountBlockUserListPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AccountBlockUserListPresenterDelegate? {
        get { return self.baseView as? AccountBlockUserListPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AccountBlockUserListInteractorProtocol {
        get { return self.baseInteractor as! AccountBlockUserListInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AccountBlockUserListRouterProtocol {
        get { return self.baseRouter as! AccountBlockUserListRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AccountBlockUserListPresenterDelegate,
         interactor: AccountBlockUserListInteractorProtocol,
         router: AccountBlockUserListRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    var users: [GetTrainingGroupUserModelUser] = []
    private var selectedIndex: Int = 0
}

// MARK: - AccountBlokeListPresenterProtocol
extension AccountBlockUserListPresenter: AccountBlockUserListPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle("Engellenen Kullanıcılar")
        view?.prepareUI()
        getBlockUser()
    }
    
    private func getBlockUser() {
        Task { @MainActor in
            await interactor.getBlockUser()
        }
    }
    
    private func navigate(_ routes: AccountBlockUserListRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back)
        default:
            break
        }
    }
    
    private func blockUser(targetUserId: String, isAddBlock: Bool) {
        let request: [String: Any] = ["targetUserId": targetUserId]
        Task { @MainActor in
            await interactor.blockUser(request, isAddBlock: isAddBlock)
        }
    }
    
    func didSelectRowAt(_ indexPath: IndexPath) {
        guard let user = users[safe: indexPath.row] else { return }
        self.selectedIndex = indexPath.row
        let isAddBlock = !user.isSelected
        blockUser(targetUserId: user.id, isAddBlock: isAddBlock)
    }
}

// MARK: - AccountBlokeListInteractorDelegate
extension AccountBlockUserListPresenter: AccountBlockUserListInteractorDelegate {
    func didGetBlockUser(_ users: [GetTrainingGroupUserModelUser]) {
        var tempUsers = users
        for i in tempUsers.indices {
            tempUsers[i].isSelected = true
        }
        
        self.users = tempUsers
        view?.reloadData()
    }
    
    func didBlockUser(isAddBlock: Bool) {
        guard let _ = users[safe: selectedIndex] else { return }
        users[selectedIndex].isSelected = isAddBlock
        view?.reloadData()
    }
}
