//
//  AddPersonTrainingGroupPresenter.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class AddPersonTrainingGroupPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AddPersonTrainingGroupPresenterDelegate? {
        get { return self.baseView as? AddPersonTrainingGroupPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AddPersonTrainingGroupInteractorProtocol {
        get { return self.baseInteractor as! AddPersonTrainingGroupInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AddPersonTrainingGroupRouterProtocol {
        get { return self.baseRouter as! AddPersonTrainingGroupRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AddPersonTrainingGroupPresenterDelegate,
         interactor: AddPersonTrainingGroupInteractorProtocol,
         router: AddPersonTrainingGroupRouterProtocol,
         trainingGroup: TrainingGroupResponse,
         model: GetTrainingGroupUserModel?,
         isUpdateCoach: Bool = false) {
        self.trainingGroup = trainingGroup
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.model = model
        self.isUpdateCoach = isUpdateCoach
    }
    private var trainingGroup: TrainingGroupResponse
    var viewType: AddPersonTrainingGroupViewType = .selection
    private var searchText: String = ""
    private var searchWorkItem: DispatchWorkItem?
    var searches: [SearchList] = []
    var selectedSearches: [SearchList] = []
    private var model: GetTrainingGroupUserModel?
    private var isUpdateCoach: Bool = false
}

// MARK: - AddPersonTrainingGroupPresenterProtocol
extension AddPersonTrainingGroupPresenter: AddPersonTrainingGroupPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle("Grup Üyelerini Ekle")
        view?.prepareUI()
        view?.prepareClub(image: trainingGroup.logo,
                          name: trainingGroup.teamName,
                          groupName: trainingGroup.groupName)
        
        if let model {
            selectedSearches = model.coaches.compactMap {
                SearchList(id: $0.id,
                           image: $0.imageUrl,
                           name: $0.summary,
                           userName: $0.name,
                           attribute: "",
                           type: "",
                           summary: $0.summary,
                           isPast: false,
                           isSelected: true,
                           isTempFollowing: false)
            }
            view?.reloadData()
        }

    }
    
    private func navigate(_ routes: AddPersonTrainingGroupRoutes) {
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
            let request: [String: Any] = ["searchTerm": text.localizedLowercase]
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
                    selectedSearches[index].isSelected = false
                }
            }

        case .selection:
            guard selectedSearches.indices.contains(indexPath.row) else { return }
            var item = selectedSearches[indexPath.row]
            item.isSelected.toggle()
            selectedSearches[indexPath.row] = item

            if !item.isSelected {
                guard let index = selectedSearches.firstIndex(where: { $0.id == item.id }) else {
                    return
                }
                
                selectedSearches[index].isSelected = false
            }
        }

        view?.reloadData()
    }
    
    func didTappedButton(_ tag: Int) {
        switch tag {
        case 0:
            addTrainingGroupUser()
        default:
            break
        }
    }
    
    private func addTrainingGroupUser() {
        let userIds = selectedSearches.filter({ $0.isSelected }).map { return $0.id }
        if isUpdateCoach {
            let request: [String: Any] = ["coachId": userIds,
                                          "trainingGroupId": trainingGroup.trainingGroupId]
            Task { @MainActor in
                await interactor.updateCoach(request)
            }
        } else {
            let request: [String: Any] = ["groupId": trainingGroup.trainingGroupId,
                                          "users": userIds]
            
            Task { @MainActor in
                await interactor.addTrainingGroupUser(request)
            }
        }
    }
}

// MARK: - AddPersonTrainingGroupInteractorDelegate
extension AddPersonTrainingGroupPresenter: AddPersonTrainingGroupInteractorDelegate {
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
    
    func didAddTrainingGroupUser() {
        showAlert(delegate: self, type: .success, message: "İşleminiz başarıyla gerçekleştirilmiştir.")
    }
}

extension AddPersonTrainingGroupPresenter: AlertViewDelegate {
    func didTappedAlertButton(_ tag: Int) {
        navigate(.dashboard)
    }
}
