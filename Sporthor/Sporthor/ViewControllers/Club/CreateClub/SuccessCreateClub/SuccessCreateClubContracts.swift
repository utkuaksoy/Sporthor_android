//
//  SuccessCreateClubContracts.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

protocol SuccessCreateClubPresenterProtocol: BasePresenterProtocol {
    var view: SuccessCreateClubPresenterDelegate? { get set }
    var interactor: SuccessCreateClubInteractorProtocol { get set }
    var router: SuccessCreateClubRouterProtocol { get set }
    
    func viewDidLoad()
    func didTappedCKButton(_ tag: Int)
}

protocol SuccessCreateClubPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func prepareTeam(image: String, name: String)
    func prepareSuccessHeaderAndDescription(header: String, description: String)
}

protocol SuccessCreateClubInteractorProtocol: BaseInteractorProtocol {
    var delegate: SuccessCreateClubInteractorDelegate? { get set }
}

protocol SuccessCreateClubInteractorDelegate: BaseInteractorDelegate {
}

protocol SuccessCreateClubRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: SuccessCreateClubRoutes)
}

enum SuccessCreateClubRoutes {
    case sendAuthorizationLetter(delegate: SuccessCreateClubDelegate?)
    case skip(delegate: SuccessCreateClubDelegate?)
}

protocol SuccessCreateClubDelegate: AnyObject {
    func didTappedSendAuthorizationLetter()
    func didTappedSkipButton()
}
