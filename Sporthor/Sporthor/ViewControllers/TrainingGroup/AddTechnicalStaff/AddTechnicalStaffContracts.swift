//
//  AddTechnicalStaffContracts.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import Foundation

protocol AddTechnicalStaffPresenterProtocol: BasePresenterProtocol {
    var view: AddTechnicalStaffPresenterDelegate? { get set }
    var interactor: AddTechnicalStaffInteractorProtocol { get set }
    var router: AddTechnicalStaffRouterProtocol { get set }
    var image: String { get set }
    var name: String { get set }
    var role: String { get set }
    var eventTypes: [EventTypeModel] { get set }
    
    func viewDidLoad()
    func textFieldDidEndEditing(text: String, tag: Int)
    func didSelectEventType(with model: EventTypeModel)
    func didTappedCKButton(_ tag: Int)
}

protocol AddTechnicalStaffPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func reloadData()
}

protocol AddTechnicalStaffInteractorProtocol: BaseInteractorProtocol {
    var delegate: AddTechnicalStaffInteractorDelegate? { get set }
    
    func updateTechnicalStaffRole(_ request: [String: Any]) async
}

protocol AddTechnicalStaffInteractorDelegate: BaseInteractorDelegate {
    func didUpdateTechnicalStaffRole()
}

protocol AddTechnicalStaffRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AddTechnicalStaffRoutes)
}

enum AddTechnicalStaffRoutes {
    case addTechnicalStaffSubmit(delegate: AddTechnicalStaffDelegate?,
                                 role: String,
                                 index: Int)
}

protocol AddTechnicalStaffDelegate: AnyObject {
    func changeRoleAddTechnicalStaff(at index: Int, to role: String)
}
