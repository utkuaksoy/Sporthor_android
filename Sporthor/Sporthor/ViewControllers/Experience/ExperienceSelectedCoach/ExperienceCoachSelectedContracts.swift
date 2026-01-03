//
//  ExperienceCoachSelectedContracts.swift
//  Sporthor
//
//  Created by derTurke on 16.05.2025.
//
//

import Foundation

protocol ExperienceCoachSelectedPresenterProtocol: BasePresenterProtocol {
    var view: ExperienceCoachSelectedPresenterDelegate? { get set }
    var interactor: ExperienceCoachSelectedInteractorProtocol { get set }
    var router: ExperienceCoachSelectedRouterProtocol { get set }
    var model: [NameValueDetailModel] { get set }
    
    func viewDidLoad()
    func didSelectItemAt(_ indexPath: IndexPath)
    func didTappedContinueButton()
}

protocol ExperienceCoachSelectedPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func didSetTitleAndDescription(title: String, description: String)
    func didSetContinueButtonTitle(_ title: String)
    func reloadData()
    func updateContinueButtonEnabled(_ isEnabled: Bool)
}

protocol ExperienceCoachSelectedInteractorProtocol: BaseInteractorProtocol {
    var delegate: ExperienceCoachSelectedInteractorDelegate? { get set }
}

protocol ExperienceCoachSelectedInteractorDelegate: BaseInteractorDelegate {
}

protocol ExperienceCoachSelectedRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ExperienceCoachSelectedRoutes)
}

enum ExperienceCoachSelectedRoutes {
    case experienceBirthdateAndGender(profileRequest: UpdateProfileRequest)
}
