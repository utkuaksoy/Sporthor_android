//
//  ComplainContracts.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//
//

import Foundation

protocol ComplainPresenterProtocol: BasePresenterProtocol {
    var view: ComplainPresenterDelegate? { get set }
    var interactor: ComplainInteractorProtocol { get set }
    var router: ComplainRouterProtocol { get set }
    
    func viewDidLoad()
    func reasonDidChange(_ text: String)
    func didTappedButton(_ tag: Int)
}

protocol ComplainPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
}

protocol ComplainInteractorProtocol: BaseInteractorProtocol {
    var delegate: ComplainInteractorDelegate? { get set }
    
    func reportPost(_ request: [String: Any]) async
}

protocol ComplainInteractorDelegate: BaseInteractorDelegate {
    func didReportPost()
}

protocol ComplainRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ComplainRoutes)
}

enum ComplainRoutes {
    case dismiss(delegate: ComplainDelegate?)
}

protocol ComplainDelegate: AnyObject {
    func dismissComplain()
}
