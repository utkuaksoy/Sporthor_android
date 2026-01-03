//
//  SuccessSendClubAuthorizationLetterContracts.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import Foundation

protocol SuccessSendClubAuthorizationLetterPresenterProtocol: BasePresenterProtocol {
    var view: SuccessSendClubAuthorizationLetterPresenterDelegate? { get set }
    var interactor: SuccessSendClubAuthorizationLetterInteractorProtocol { get set }
    var router: SuccessSendClubAuthorizationLetterRouterProtocol { get set }
    
    func viewDidLoad()
    func didTappedHomeButton()
}

protocol SuccessSendClubAuthorizationLetterPresenterDelegate: BasePresenterDelegate {
    func prepareSuccessHeaderAndDescription(header: String, description: String)
    func prepareUI()
    func prepareContinueButton(_ text: String)
}

protocol SuccessSendClubAuthorizationLetterInteractorProtocol: BaseInteractorProtocol {
    var delegate: SuccessSendClubAuthorizationLetterInteractorDelegate? { get set }
}

protocol SuccessSendClubAuthorizationLetterInteractorDelegate: BaseInteractorDelegate {
}

protocol SuccessSendClubAuthorizationLetterRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: SuccessSendClubAuthorizationLetterRoutes)
}

enum SuccessSendClubAuthorizationLetterRoutes {
    case home
    case createTrainingGroup(teams: [TeamItemModel])
}
