//
//  AddPersonWithRoleTrainingGroupContracts.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import Foundation

protocol AddPersonWithRoleTrainingGroupPresenterProtocol: BasePresenterProtocol {
    var view: AddPersonWithRoleTrainingGroupPresenterDelegate? { get set }
    var interactor: AddPersonWithRoleTrainingGroupInteractorProtocol { get set }
    var router: AddPersonWithRoleTrainingGroupRouterProtocol { get set }
    var role: TrainingGroupPersonRole { get set }
    var searches: [SearchList] { get set }
    var selectedSearches: [SearchList] { get set }
    var viewType: AddPersonWithRoleTrainingGroupViewType { get set }
    var isEdit: Bool { get set }
    var isLogin: Bool { get set }
    var isFollowing: Bool { get set }
    
    func viewDidLoad()
    func search(_ text: String)
    func searchCancel()
    func didSelectRowAt(_ indexPath: IndexPath)
    func didTappedCKButton()
    func addTechnicalStaff(_ tag: Int)
}

protocol AddPersonWithRoleTrainingGroupPresenterDelegate: BasePresenterDelegate {
    func setupView()
    func prepareContinueButtonTitle(_ title: String)
    func reloadData()
}

protocol AddPersonWithRoleTrainingGroupInteractorProtocol: BaseInteractorProtocol {
    var delegate: AddPersonWithRoleTrainingGroupInteractorDelegate? { get set }
    
    func search(_ request: [String : Any]) async
    func getFollowing(userId: String, role: Int) async
    func addTrainingGroupUser(_ request: [String: Any]) async
    func updateCoach(_ request: [String: Any]) async
}

protocol AddPersonWithRoleTrainingGroupInteractorDelegate: BaseInteractorDelegate {
    func didSearch(_ searchList: [SearchList])
    func didGetFollowing(_ following: [FollowerModel])
    func didAddTrainingGroupUser()
}

protocol AddPersonWithRoleTrainingGroupRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AddPersonWithRoleTrainingGroupRoutes)
}

enum AddPersonWithRoleTrainingGroupRoutes {
    case addPerson(role: TrainingGroupPersonRole,
                   trainingGroup: TrainingGroupResponse,
                   request: AddPersonWithRoleRequest?)
    case addTechnicalStaff(delegate: AddTechnicalStaffDelegate?,
                           index: Int,
                           image: String,
                           name: String,
                           role: String)
    case dashboard
    case back(delegate: AddPersonWithRoleTrainingGroupDelegate?,
              type: TrainingGroupPersonRole,
              users: [GetTrainingGroupUserModelUser])
}

enum AddPersonWithRoleTrainingGroupViewType {
    case search
    case selection
}

protocol AddPersonWithRoleTrainingGroupDelegate: AnyObject {
    func didAddPersons(type: TrainingGroupPersonRole, users: [GetTrainingGroupUserModelUser])
}
