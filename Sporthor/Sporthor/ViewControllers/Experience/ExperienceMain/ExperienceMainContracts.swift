//
//  ExperienceMainContracts.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//
//

import Foundation

protocol ExperienceMainPresenterProtocol: BasePresenterProtocol {
    var view: ExperienceMainPresenterDelegate? { get set }
    var interactor: ExperienceMainInteractorProtocol { get set }
    var router: ExperienceMainRouterProtocol { get set }
    
    func viewDidLoad()
    func didTappedButton(with tag: Int)
}

protocol ExperienceMainPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func didSetMainTitle(_ title: String)
    func didSetContinueButttonTitle(_ title: String)
}

protocol ExperienceMainInteractorProtocol: BaseInteractorProtocol {
    var delegate: ExperienceMainInteractorDelegate? { get set }
}

protocol ExperienceMainInteractorDelegate: BaseInteractorDelegate {
}

protocol ExperienceMainRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ExperienceMainRoutes)
}

enum ExperienceMainRoutes {
    case experienceJob
}
