//
//  CalendarAddDescriptionContracts.swift
//  Sporthor
//
//  Created by derTurke on 29.05.2025.
//
//

import Foundation

protocol CalendarAddDescriptionPresenterProtocol: BasePresenterProtocol {
    var view: CalendarAddDescriptionPresenterDelegate? { get set }
    var interactor: CalendarAddDescriptionInteractorProtocol { get set }
    var router: CalendarAddDescriptionRouterProtocol { get set }
    
    func viewDidLoad()
    func didChangeDescription(_ description: String)
    func didTappedButton(_ tag: Int)
}

protocol CalendarAddDescriptionPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func didSetDescription(_ description: String)
}

protocol CalendarAddDescriptionInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarAddDescriptionInteractorDelegate? { get set }
}

protocol CalendarAddDescriptionInteractorDelegate: BaseInteractorDelegate {
}

protocol CalendarAddDescriptionRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarAddDescriptionRoutes)
}

enum CalendarAddDescriptionRoutes {
    case dismiss(delegate: CalendarAddDescriptionDelegate?,
                 description: String)
}

protocol CalendarAddDescriptionDelegate: AnyObject {
    func didAddDescription(_ description: String)
}
