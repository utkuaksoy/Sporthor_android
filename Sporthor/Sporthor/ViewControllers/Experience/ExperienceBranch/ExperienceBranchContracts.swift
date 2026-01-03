//
//  ExperienceBranchContracts.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//
//

import Foundation

protocol ExperienceBranchPresenterProtocol: BasePresenterProtocol {
    var view: ExperienceBranchPresenterDelegate? { get set }
    var interactor: ExperienceBranchInteractorProtocol { get set }
    var router: ExperienceBranchRouterProtocol { get set }
    var model: [NameValueDetailModel] { get set }
    
    func viewDidLoad()
    func didSelectItemAt(_ indexPath: IndexPath)
    func didTappedContinueButton(_ tag: Int)
}

protocol ExperienceBranchPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func didSetTitleAndDescription(title: String, description: String)
    func didSetContinueButtonTitle(_ title: String)
    func reloadData()
    func updateContinueButtonEnabled(_ isEnabled: Bool)
}

protocol ExperienceBranchInteractorProtocol: BaseInteractorProtocol {
    var delegate: ExperienceBranchInteractorDelegate? { get set }
}

protocol ExperienceBranchInteractorDelegate: BaseInteractorDelegate {
}

protocol ExperienceBranchRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ExperienceBranchRoutes)
}

enum ExperienceBranchRoutes {
    case experienceBirthdateAndGender(updateProfileRequest: UpdateProfileRequest)
    case experienceCoachSelected(updateProfileRequest: UpdateProfileRequest)
}
