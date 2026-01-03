//
//  AddTechnicalStaffPresenter.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import Foundation

final class AddTechnicalStaffPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AddTechnicalStaffPresenterDelegate? {
        get { return self.baseView as? AddTechnicalStaffPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AddTechnicalStaffInteractorProtocol {
        get { return self.baseInteractor as! AddTechnicalStaffInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AddTechnicalStaffRouterProtocol {
        get { return self.baseRouter as! AddTechnicalStaffRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AddTechnicalStaffPresenterDelegate,
         interactor: AddTechnicalStaffInteractorProtocol,
         router: AddTechnicalStaffRouterProtocol,
         delegate: AddTechnicalStaffDelegate? = nil,
         trainingGroupId: String,
         userId: String,
         image: String,
         name: String,
         role: String,
         index: Int,
         isUpdate: Bool) {
        self.trainingGroupId = trainingGroupId
        self.userId = userId
        self.image = image
        self.name = name
        self.role = role
        self.index = index
        self.isUpdate = isUpdate
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.delegate = delegate
    }
    
    private weak var delegate: AddTechnicalStaffDelegate?
    private var trainingGroupId: String
    private var userId: String
    var image: String
    var name: String
    var role: String
    private var index: Int
    private var isUpdate: Bool
    var eventTypes: [EventTypeModel] = []
}

// MARK: - AddTechnicalStaffPresenterProtocol
extension AddTechnicalStaffPresenter: AddTechnicalStaffPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        prepareEventTypes()
    }
    
    private func navigate(_ routes: AddTechnicalStaffRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func prepareEventTypes() {
        let coachRoles = ApplicationContext.shared.getConfiguration.coachRoles ?? []
        eventTypes = coachRoles.compactMap({
            return EventTypeModel(name: $0.name ?? "",
                                  value: $0.value ?? "",
                                  detail: $0.detail ?? "",
                                  isSelected: $0.name == role)
        })
        
        view?.reloadData()
    }
    
    func textFieldDidEndEditing(text: String, tag: Int) {
        role = text
        
        for index in eventTypes.indices {
            eventTypes[index].isSelected = false
        }
        
        if let index = eventTypes.firstIndex(where: { $0.name == role }) {
            eventTypes[index].isSelected = true
        }
        
        view?.reloadData()
    }
    
    func didSelectEventType(with model: EventTypeModel) {
        for index in eventTypes.indices {
            eventTypes[index].isSelected = false
        }
        
        if let index = eventTypes.firstIndex(where: { $0.value == model.value }) {
            eventTypes[index].isSelected = true
            role = model.name
        }
        
        view?.reloadData()
    }
    
    func didTappedCKButton(_ tag: Int) {
        if isUpdate {
            let request: [String: Any] = [
                "trainingGroupId": trainingGroupId,
                "userId": userId,
                "role": role
            ]
            
            Task { @MainActor in
                await interactor.updateTechnicalStaffRole(request)
            }
        } else {
            navigate(.addTechnicalStaffSubmit(delegate: delegate, role: role, index: index))
        }
    }
}

// MARK: - AddTechnicalStaffInteractorDelegate
extension AddTechnicalStaffPresenter: AddTechnicalStaffInteractorDelegate {
    func didUpdateTechnicalStaffRole() {
        navigate(.addTechnicalStaffSubmit(delegate: delegate, role: role, index: index))
    }
}
