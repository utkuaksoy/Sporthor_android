//
//  AddPersonWithRoleTrainingGroupPresenter.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import Foundation

final class AddPersonWithRoleTrainingGroupPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AddPersonWithRoleTrainingGroupPresenterDelegate? {
        get { return self.baseView as? AddPersonWithRoleTrainingGroupPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AddPersonWithRoleTrainingGroupInteractorProtocol {
        get { return self.baseInteractor as! AddPersonWithRoleTrainingGroupInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AddPersonWithRoleTrainingGroupRouterProtocol {
        get { return self.baseRouter as! AddPersonWithRoleTrainingGroupRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AddPersonWithRoleTrainingGroupPresenterDelegate,
         interactor: AddPersonWithRoleTrainingGroupInteractorProtocol,
         router: AddPersonWithRoleTrainingGroupRouterProtocol,
         role: TrainingGroupPersonRole,
         trainingGroup: TrainingGroupResponse,
         isFirst: Bool = false,
         requestModel: AddPersonWithRoleRequest? = nil,
         isEdit: Bool = false,
         selectedUsers: [GetTrainingGroupUserModelUser] = [],
         selectedCoaches: [GetTrainingGroupUserModelUser] = [],
         delegate: AddPersonWithRoleTrainingGroupDelegate? = nil,
         isUpdateCoach: Bool) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.role = role
        self.trainingGroup = trainingGroup
        self.isFirst = isFirst
        self.requestModel = requestModel
        self.isEdit = isEdit
        self.selectedUsers = selectedUsers
        self.selectedCoaches = selectedCoaches
        self.delegate = delegate
        self.isUpdateCoach = isUpdateCoach
    }
    
    var role: TrainingGroupPersonRole = .person
    private var trainingGroup: TrainingGroupResponse?
    private var isFirst: Bool = false
    private var searchText: String = ""
    private var searchWorkItem: DispatchWorkItem?
    var viewType: AddPersonWithRoleTrainingGroupViewType = .selection
    var searches: [SearchList] = []
    var selectedSearches: [SearchList] = []
    private var requestModel: AddPersonWithRoleRequest?
    var isEdit: Bool = false
    private var selectedUsers: [GetTrainingGroupUserModelUser] = []
    private var selectedCoaches: [GetTrainingGroupUserModelUser] = []
    private weak var delegate: AddPersonWithRoleTrainingGroupDelegate?
    var isLogin: Bool = ApplicationContext.shared.isLogin
    var isFollowing: Bool = false
    var isSuggestion: Bool = false
    var isUpdateCoach: Bool = false
}

// MARK: - AddPersonWithRoleTrainingGroupPresenterProtocol
extension AddPersonWithRoleTrainingGroupPresenter: AddPersonWithRoleTrainingGroupPresenterProtocol {
    func viewDidLoad() {
        setTitle()
        prepareContinueButtonTitle()
        view?.setupView()
        if isLogin {
            if isEdit {
                guard let trainingGroup else { return }
                requestModel = AddPersonWithRoleRequest()
                requestModel?.groupId = trainingGroup.trainingGroupId
                switch role {
                case .person:
                    selectedSearches = selectedUsers.compactMap({
                        return SearchList(
                            id: $0.id,
                            image: $0.imageUrl,
                            name: $0.name,
                            userName: $0.username,
                            attribute: $0.summary,
                            type: "",
                            summary: $0.summary,
                            isPast: false,
                            isSelected: true,
                            isTempFollowing: false)
                    })
                    
                    self.requestModel?.coaches = selectedCoaches.compactMap({
                        return PersonWithRoleModel(name: $0.name, val: $0.id, val2: $0.summary)
                    })
                    
                case .technicalStaff:
                    selectedSearches = selectedCoaches.compactMap({
                        return SearchList(
                            id: $0.id,
                            image: $0.imageUrl,
                            name: $0.name,
                            userName: $0.username,
                            attribute: $0.summary,
                            type: "",
                            summary: $0.summary,
                            isPast: false,
                            isSelected: true,
                            isTempFollowing: false)
                    })
                    requestModel?.users = selectedUsers.compactMap({
                        $0.id
                    })
                }
            } else {
                Task { @MainActor in
                    await interactor.getFollowing(userId: ApplicationContext.shared.userId, role: role == .person ? 0 : 1)
                }
                
            }
        }
    }
    
    private func setTitle() {
        switch role {
        case .technicalStaff:
            view?.didSetTitle("Antrenör Ekle")
        case .person:
            view?.didSetTitle("Sporcu Ekle")
        }
    }
    
    private func prepareContinueButtonTitle() {
        if isEdit {
            switch role {
            case .technicalStaff:
                view?.prepareContinueButtonTitle("Antreman Grubuna Antrenör Ekle")
            case .person:
                view?.prepareContinueButtonTitle("Antreman Grubuna Sporcu Ekle")
            }
        } else {
            if isFirst {
                switch role {
                case .technicalStaff:
                    view?.prepareContinueButtonTitle("Antreman Grubuna Antrenör Ekle")
                case .person:
                    view?.prepareContinueButtonTitle("Antreman Grubuna Sporcu Ekle")
                }
            } else {
                view?.prepareContinueButtonTitle("Devam Et")
            }
        }
    }
    
    private func navigate(_ routes: AddPersonWithRoleTrainingGroupRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func search(_ text: String) {
        self.searchText = text
        searchWorkItem?.cancel()
        
        guard !text.isEmpty else {
            viewType = .selection
            view?.reloadData()
            return
        }
        
        let workItem = DispatchWorkItem { [weak self] in
            guard let self = self else { return }
            let type = role == .technicalStaff ? 1 : 0
            let request: [String: Any] = ["searchTerm": text.localizedLowercase,
                                          "role": type]
            Task {
                @MainActor in
                await self.interactor.search(request)
            }
        }
        
        searchWorkItem = workItem
        DispatchQueue.global().asyncAfter(deadline: .now() + 1, execute: workItem)
    }
    
    func searchCancel() {
        searchWorkItem?.cancel()
        viewType = .selection
        view?.reloadData()
    }
    
    
    func didSelectRowAt(_ indexPath: IndexPath) {
        switch viewType {
        case .search:
            guard searches.indices.contains(indexPath.row) else { return }
            var tempSelectedSearches = selectedSearches
            var item = searches[indexPath.row]
            item.isSelected.toggle()
            searches[indexPath.row] = item

            if item.isSelected {
                if !selectedSearches.contains(where: { $0.id == item.id }) {
                    selectedSearches.append(item)
                } else {
                    guard let index = selectedSearches.firstIndex(where: { $0.id == item.id }) else { return }
                    selectedSearches[index].isSelected = true
                }
            } else {
                if let index = selectedSearches.firstIndex(where: { $0.id == item.id }) {
                    selectedSearches.remove(at: index)
                }
            }
            
            if isLogin && isFollowing && hasChanged(tempSelectedSearches, selectedSearches) {
                isFollowing = false
            }
        case .selection:
            guard selectedSearches.indices.contains(indexPath.row) else { return }
            var item = selectedSearches[indexPath.row]
            item.isSelected.toggle()
            selectedSearches[indexPath.row] = item
        }
        view?.reloadData()
    }
    
    private func hasChanged(_ old: [SearchList], _ new: [SearchList]) -> Bool {
        guard old.count == new.count else { return true }
        for (lhs, rhs) in zip(old, new) {
            if lhs.id != rhs.id || lhs.isSelected != rhs.isSelected {
                return true
            }
        }
        return false
    }
    
    func addTechnicalStaff(_ tag: Int) {
        switch viewType {
        case .search:
            guard let user = searches[safe: tag] else { return }
            if user.isSelected {
                searches[tag].isSelected = false
                if let index = selectedSearches.firstIndex(where: { $0.id == user.id }) {
                    selectedSearches.remove(at: index)
                }
                view?.reloadData()
            } else {
                navigateToAddTechnicalStaff(user: user, tag: tag)
            }
        case .selection:
            guard let user = selectedSearches[safe: tag] else { return }
            if user.isSelected {
                selectedSearches[tag].isSelected = false
                view?.reloadData()
            } else {
                navigateToAddTechnicalStaff(user: user, tag: tag)
            }
        }
    }
    
    private func navigateToAddTechnicalStaff(user: SearchList, tag: Int) {
        navigate(.addTechnicalStaff(delegate: self,
                                    index: tag,
                                    image: user.image,
                                    name: user.name,
                                    role: user.summary))
    }
    
    func didTappedCKButton() {
        guard let trainingGroup else { return }

        let selectedCoaches: [PersonWithRoleModel]? = {
            let users = selectedSearches.compactMap { search -> PersonWithRoleModel? in
                guard search.isSelected else { return nil }
                return PersonWithRoleModel(name: search.name, val: search.id, val2: search.summary)
            }
            return users.isEmpty ? nil : users
        }()

        let selectedUsers: [String]? = {
            let users = selectedSearches.compactMap { search -> String? in
                search.isSelected ? search.id : nil
            }
            return users.isEmpty ? nil : users
        }()

        if requestModel == nil {
            requestModel = AddPersonWithRoleRequest()
            requestModel?.groupId = trainingGroup.trainingGroupId
        }

        switch (isEdit, isFirst, role) {
        case (true, _, .technicalStaff),
             (false, false, .technicalStaff):
            requestModel?.coaches = selectedCoaches ?? []
            addTrainingGroupUser()
        case (true, _, .person),
             (false, false, .person):
            requestModel?.users = selectedUsers ?? []
            addTrainingGroupUser()
        case (false, true, .technicalStaff):
            requestModel?.coaches = selectedCoaches ?? []
            navigate(.addPerson(role: .person,
                                trainingGroup: trainingGroup,
                                request: requestModel))

        case (false, true, .person):
            requestModel?.users = selectedUsers ?? []
            navigate(.addPerson(role: .technicalStaff,
                                trainingGroup: trainingGroup,
                                request: requestModel))
        }
    }
    
    private func addTrainingGroupUser() {
        Task { @MainActor in
            if isUpdateCoach {
                let updateCoachRequestModel = AddCoachesWithRoleRequest(trainingGroupId: trainingGroup?.trainingGroupId ?? "",
                                                                        coaches: requestModel?.coaches ?? [])
                await interactor.updateCoach(updateCoachRequestModel.dictionary() ?? [:])
            } else {
                await interactor.addTrainingGroupUser(requestModel?.dictionary() ?? [:])
            }
        }
    }
}

// MARK: - AddPersonWithRoleTrainingGroupInteractorDelegate
extension AddPersonWithRoleTrainingGroupPresenter: AddPersonWithRoleTrainingGroupInteractorDelegate {
    func didSearch(_ searchList: [SearchList]) {
        let selectedIds = Set(selectedSearches.filter({ $0.isSelected }).map { $0.id })

        self.searches = searchList.map { item in
            var updatedItem = item
            if selectedIds.contains(item.id) {
                updatedItem.isSelected = true
            }
            return updatedItem
        }
        
        viewType = .search
        view?.reloadData()
    }
    
    func didGetFollowing(_ following: [FollowerModel]) {
        if !following.isEmpty { isFollowing = true }
        self.selectedSearches = following.compactMap({
            return $0.toSearchListModel()
        })
        view?.reloadData()
    }
    
    func didAddTrainingGroupUser() {
        if isEdit {
            let users: [GetTrainingGroupUserModelUser] = selectedSearches.compactMap({
                if $0.isSelected {
                    return $0.toGetTrainingGroupUserModelUser()
                }
                return nil
            })
            navigate(.back(delegate: delegate, type: role, users: users))
        } else {
            showAlert(
                delegate: self,
                type: .success,
                message: "Antreman grubunuza kişileriniz eklenmiştir.",
                buttonTitle: "Anasayfaya Git",
                tag: 99
            )
        }
    }
}

// MARK: - AddTechnicalStaffDelegate
extension AddPersonWithRoleTrainingGroupPresenter: AddTechnicalStaffDelegate {
    func changeRoleAddTechnicalStaff(at index: Int, to role: String) {
        switch viewType {
        case .search:
            guard let user = searches[safe: index] else { return }
            let tempSelectedSearches = selectedSearches
            searches[index].isSelected = true
            searches[index].summary = role
            
            if !selectedSearches.contains(where: {user.id == $0.id}) {
                searches[index].isSelected = true
                selectedSearches.append(searches[index])
            } else {
                if let index = selectedSearches.firstIndex(where: {user.id == $0.id }) {
                    selectedSearches[index].isSelected = true
                    selectedSearches[index].summary = role
                }
            }
            
            if isLogin && isFollowing && hasChanged(tempSelectedSearches, selectedSearches) {
                isFollowing = false
            }
        case .selection:
            guard let user = selectedSearches[safe: index] else { return }
            if let index = selectedSearches.firstIndex(where: { user.id == $0.id }) {
                selectedSearches[index].isSelected = true
                selectedSearches[index].summary = role
            }
        }
        
        view?.reloadData()
    }
}

extension AddPersonWithRoleTrainingGroupPresenter: AlertViewDelegate {
    func didTappedAlertButton(_ tag: Int) {
        switch tag {
        case 99:
            navigate(.dashboard)
        default:
            break
        }
    }
}
