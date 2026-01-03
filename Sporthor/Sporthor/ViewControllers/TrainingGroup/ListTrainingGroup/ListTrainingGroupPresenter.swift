//
//  ListTrainingGroupPresenter.swift
//  Sporthor
//
//  Created by derTurke on 1.07.2025.
//
//

import Foundation
import ComponentKit

final class ListTrainingGroupPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ListTrainingGroupPresenterDelegate? {
        get { return self.baseView as? ListTrainingGroupPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ListTrainingGroupInteractorProtocol {
        get { return self.baseInteractor as! ListTrainingGroupInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ListTrainingGroupRouterProtocol {
        get { return self.baseRouter as! ListTrainingGroupRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: ListTrainingGroupPresenterDelegate,
         interactor: ListTrainingGroupInteractorProtocol,
         router: ListTrainingGroupRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var trainingGroups: [GetTrainingGroupUserModel] = []
    var isDeleted: Bool = false
}

// MARK: - ListTrainingGroupPresenterProtocol
extension ListTrainingGroupPresenter: ListTrainingGroupPresenterProtocol {
    func viewDidLoad() {
        view?.prepareNavigationBar()
        view?.didSetTitle("Antreman Grubu Seç")
        view?.prepareUI()
    }
    
    func viewWillAppear() {
        getTrainingGroup()
    }
    
    private func getTrainingGroup() {
        Task { @MainActor in
            await interactor.getTrainingGroupUser()
        }
    }
    
    private func navigate(_ routes: ListTrainingGroupRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back)
        case .textRight:
            unSelectedTrainingGroups()
            isDeleted.toggle()
            view?.changeDeleteSubmitButtonHiddenState(!isDeleted)
            view?.reloadData()
        default:
            break
        }
    }
    
    func didSelectRowAt(_ indexPath: IndexPath) {
        guard let trainingGroup = trainingGroups[safe: indexPath.row] else { return }
        
        if isDeleted {
            unSelectedTrainingGroups()
            trainingGroups[indexPath.row].isSelected = !trainingGroups[indexPath.row].isSelected
            view?.reloadData()
        } else {
            navigate(.updateTrainingGroup(model: trainingGroup))
        }
    }
    
    private func unSelectedTrainingGroups() {
        for index in trainingGroups.indices {
            trainingGroups[index].isSelected = false
        }
    }
    
    func ckButtonDidTap(tag: Int) {
        switch tag {
        case 1:
            guard let trainingGroup = trainingGroups.first(where: { $0.isSelected }) else {
                showAlert(type: .warning, message: "Silme işlemi yapabilmek için 1 adet kulüp seçmelisiniz!")
                return
            }
            showCKDefaultAlert(
                delegate: self,
                message: trainingGroup.groupName + " isimli antreman grubunuzu silmek istediğinize emin misiniz?",
                okTitle: "Evet",
                cancelTitle: "Hayır")
        default:
            break
        }
    }
    
    private func removeTrainingGroup() {
        guard let trainingGroup = trainingGroups.first(where: { $0.isSelected }) else { return }
        let request: [String: Any] = ["trainingGroupId": trainingGroup.groupId]
        Task { @MainActor in
            await interactor.removeTrainingGroup(request)
        }
    }
}

// MARK: - ListTrainingGroupInteractorDelegate
extension ListTrainingGroupPresenter: ListTrainingGroupInteractorDelegate {
    func didGetTrainingGroupUser(_ response: [GetTrainingGroupUserModel]) {
        trainingGroups = response
        view?.changeDeleteSubmitButtonHiddenState(true)
        view?.reloadData()
    }
    
    func didRemoveTrainingGroup() {
        trainingGroups.removeAll { $0.isSelected }
        if trainingGroups.isEmpty {
            view?.changeDeleteSubmitButtonHiddenState(true)
        }
        view?.reloadData()
    }
}

extension ListTrainingGroupPresenter: CKDefaultAlertDelegate {
    func ckDefaultAlertDidTapOK() {
        removeTrainingGroup()
    }
}
