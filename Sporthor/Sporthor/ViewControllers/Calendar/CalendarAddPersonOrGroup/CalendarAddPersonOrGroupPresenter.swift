//
//  CalendarAddPersonOrGroupPresenter.swift
//  Sporthor
//
//  Created by derTurke on 29.05.2025.
//
//

import Foundation

final class CalendarAddPersonOrGroupPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarAddPersonOrGroupPresenterDelegate? {
        get { return self.baseView as? CalendarAddPersonOrGroupPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarAddPersonOrGroupInteractorProtocol {
        get { return self.baseInteractor as! CalendarAddPersonOrGroupInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarAddPersonOrGroupRouterProtocol {
        get { return self.baseRouter as! CalendarAddPersonOrGroupRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarAddPersonOrGroupPresenterDelegate,
         interactor: CalendarAddPersonOrGroupInteractorProtocol,
         router: CalendarAddPersonOrGroupRouterProtocol,
         delegate: CalendarAddPersonOrGroupDelegate?,
         selectedGroups: [TeamItemModel],
         selectedUsers: [GetTrainingGroupUserModelUser]) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.delegate = delegate
        self.selectedGroups = selectedGroups
        self.selectedUsers = selectedUsers
    }
    
    private var getTrainingGroupUsers: [GetTrainingGroupUserModel] = []
    var groups: [TeamItemModel] = []
    private var users: [GetTrainingGroupUserModelUser] = []
    var filteredUsers: [GetTrainingGroupUserModelUser] = []
    private weak var delegate: CalendarAddPersonOrGroupDelegate?
    private var selectedGroups: [TeamItemModel] = []
    private var selectedUsers: [GetTrainingGroupUserModelUser] = []
}

// MARK: - CalendarAddPersonOrGroupPresenterProtocol
extension CalendarAddPersonOrGroupPresenter: CalendarAddPersonOrGroupPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        getTrainingGroupUser()
    }
    
    private func navigate(_ routes: CalendarAddPersonOrGroupRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    private func getTrainingGroupUser() {
        Task { @MainActor in
            await interactor.getTrainingGroupUser()
        }
    }
    
    func didSelectRowAt(_ indexPath: IndexPath) {
        switch indexPath.section {
        case 1:
            filteredUsers[indexPath.row].isSelected = !filteredUsers[indexPath.row].isSelected
            
            if let index = users.firstIndex(where: { filteredUsers[indexPath.row].username == $0.username }) {
                users[index].isSelected = !users[indexPath.row].isSelected
            }
            
            // User seçtiğinde group seçimlerini siler
            for index in groups.indices { groups[index].isSelected = false }
            
            view?.reloadData()
        default:
            break
        }
    }
    
    func selectedGroup(_ model: TeamItemModel) {
        guard let index = groups.firstIndex(where: { $0.value == model.value }) else { return }

        let isCurrentlySelected = groups[index].isSelected

        // Tüm seçimleri kaldır
        for i in groups.indices { groups[i].isSelected = false }
        
        // Eğer daha önce seçiliyse, yani seçim kaldırmak istiyorsak yapma, yoksa seç
        groups[index].isSelected = !isCurrentlySelected
        
        // User seçimlerini sıfırla
        for i in filteredUsers.indices { filteredUsers[i].isSelected = false }
        for i in users.indices { users[i].isSelected = false }

        view?.reloadData()
    }
    
    func searchBarTextDidChange(_ text: String) {
        guard !text.isEmpty else {
            filteredUsers = users
            view?.reloadData()
            return
        }

        let searchText = text.lowercased(with: Locale(identifier: ApplicationContext.shared.lang))

        filteredUsers = users.filter {
            $0.name.lowercased(with: Locale(identifier: ApplicationContext.shared.lang)).contains(searchText) ||
            $0.username.lowercased(with: Locale(identifier: ApplicationContext.shared.lang)).contains(searchText)
        }

        view?.reloadData()
    }
    
    func didTappedSubmitButton() {
        selectedGroups = groups.filter({ $0.isSelected })
        selectedUsers = users.filter({ $0.isSelected })
        
        navigate(.dismiss(delegate: delegate, persons: selectedUsers, groups: selectedGroups))
    }
}

// MARK: - CalendarAddPersonOrGroupInteractorDelegate
extension CalendarAddPersonOrGroupPresenter: CalendarAddPersonOrGroupInteractorDelegate {
    func didGetTrainingGroupUser(_ response: [GetTrainingGroupUserModel]) {
        self.getTrainingGroupUsers = response
        
        groups = response.map { TeamItemModel(name: $0.groupName, value: $0.groupId, image: $0.groupImage) }
        let groupIds = Set(groups.map { $0.value })
        selectedGroups = selectedGroups.filter { groupIds.contains($0.value) }
        
        let selectedGroupIds = Set(selectedGroups.map { $0.value })
        for index in groups.indices {
            groups[index].isSelected = selectedGroupIds.contains(groups[index].value)
        }
        
        var seenUserIds = Set<String>()
        users = response
            .flatMap { $0.users }
            .filter { user in
                if seenUserIds.contains(user.id) {
                    return false
                } else {
                    seenUserIds.insert(user.id)
                    return true
                }
            }
        let userIds = Set(users.map { $0.id })
        selectedUsers = selectedUsers.filter { userIds.contains($0.id)}
        let selectedIds = Set(selectedUsers.map { $0.id })
        for index in users.indices {
            users[index].isSelected = selectedIds.contains(users[index].id)
        }
        
        filteredUsers = users
        
        view?.reloadData()
    }
}
