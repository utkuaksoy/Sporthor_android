//
//  ExperienceJobContracts.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//
//

import Foundation

protocol ExperienceJobPresenterProtocol: BasePresenterProtocol {
    var view: ExperienceJobPresenterDelegate? { get set }
    var interactor: ExperienceJobInteractorProtocol { get set }
    var router: ExperienceJobRouterProtocol { get set }
    var model: [NameValueDetailModel] { get set }
    var isEdit: Bool { get set }
    
    func viewDidLoad()
    func didSelectItemAt(_ indexPath: IndexPath)
    func didTappedContinueButton(_ tag: Int)
    func back()
}

protocol ExperienceJobPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func didSetTitleAndDescription(title: String, description: String)
    func didSetContinueButtonTitle(_ title: String)
    func reloadData()
    func updateContinueButtonEnabled(_ isEnabled: Bool)
    func updateEditView()
}

protocol ExperienceJobInteractorProtocol: BaseInteractorProtocol {
    var delegate: ExperienceJobInteractorDelegate? { get set }
    func getMyRoles() async
    func updateUserRoles(_ request: [String: Any]) async
}

protocol ExperienceJobInteractorDelegate: BaseInteractorDelegate {
    func didGetMyRoles(_ roles: [String])
    func didUpdateUserRoles(_ roles: [String])
}

protocol ExperienceJobRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ExperienceJobRoutes)
}

enum ExperienceJobRoutes {
    case experienceBranch(updateProfileRequest: UpdateProfileRequest)
    case back
}
