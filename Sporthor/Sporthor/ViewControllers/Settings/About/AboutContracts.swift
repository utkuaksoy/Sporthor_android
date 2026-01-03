//
//  AboutContracts.swift
//  Sporthor
//
//  Created by derTurke on 22.07.2025.
//
//

import Foundation

protocol AboutPresenterProtocol: BasePresenterProtocol {
    var view: AboutPresenterDelegate? { get set }
    var interactor: AboutInteractorProtocol { get set }
    var router: AboutRouterProtocol { get set }
    
    func viewDidLoad()
    func didTappedNavigationButton(_ type: BarButtonItemType)
}

protocol AboutPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
}

protocol AboutInteractorProtocol: BaseInteractorProtocol {
    var delegate: AboutInteractorDelegate? { get set }
}

protocol AboutInteractorDelegate: BaseInteractorDelegate {
}

protocol AboutRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AboutRoutes)
}

enum AboutRoutes {
    case back
}
