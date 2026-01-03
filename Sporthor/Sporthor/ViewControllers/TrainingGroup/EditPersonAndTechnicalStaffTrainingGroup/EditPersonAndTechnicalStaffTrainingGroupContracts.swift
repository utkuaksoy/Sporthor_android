//
//  EditPersonAndTechnicalStaffTrainingGroupContracts.swift
//  Sporthor
//
//  Created by derTurke on 31.10.2025.
//
//

import UIKit

protocol EditPersonAndTechnicalStaffTrainingGroupPresenterProtocol: BasePresenterProtocol {
    var view: EditPersonAndTechnicalStaffTrainingGroupPresenterDelegate? { get set }
    var interactor: EditPersonAndTechnicalStaffTrainingGroupInteractorProtocol { get set }
    var router: EditPersonAndTechnicalStaffTrainingGroupRouterProtocol { get set }
    var model: GetTrainingGroupUserModel? { get set }
    var viewType: EditPersonAndTechnicalStaffTrainingGroupViewType { get set }
    
    func viewDidLoad()
    func segmentedControl(didSelect index: Int)
    func didSelectRowAt(_ indexPath: IndexPath)
    func deletePersonTrainingGroup(_ indexPath: IndexPath)
    func didTappedCKButton(_ tag: Int)
    func didTappedCKImageTitleView(_ tag: Int)
    func didTappedNavigationButton(_ type: BarButtonItemType)
}

protocol EditPersonAndTechnicalStaffTrainingGroupPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func prepareCKImageTitleView(image: String,
                                 title: String,
                                 rightImage: UIImage,
                                 backgroundImage: UIImage?)
    func reloadData()
    func prepareAddButtonTitle(_ title: String)
    func hiddenTab()
}

protocol EditPersonAndTechnicalStaffTrainingGroupInteractorProtocol: BaseInteractorProtocol {
    var delegate: EditPersonAndTechnicalStaffTrainingGroupInteractorDelegate? { get set }
    
    func addTrainingGroupUser(_ request: [String : Any], isDelete: Bool) async
    func updateCoach(_ request: [String: Any], isDelete: Bool) async
}

protocol EditPersonAndTechnicalStaffTrainingGroupInteractorDelegate: BaseInteractorDelegate {
    func didAddTrainingGroupUser(isDelete: Bool)
}

protocol EditPersonAndTechnicalStaffTrainingGroupRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: EditPersonAndTechnicalStaffTrainingGroupRoutes)
}

enum EditPersonAndTechnicalStaffTrainingGroupRoutes {
    case updateTechnicalStaff(delegate: AddTechnicalStaffDelegate?,
                              trainingGroupId: String,
                              userId: String,
                              image: String,
                              name: String,
                              role: String,
                              index: Int)
    case addPersonTrainingGroup(role: TrainingGroupPersonRole,
                                trainingGroup: TrainingGroupResponse,
                                isEdit: Bool,
                                users: [GetTrainingGroupUserModelUser],
                                coaches: [GetTrainingGroupUserModelUser],
                                delegate: AddPersonWithRoleTrainingGroupDelegate?,
                                isUpdateCoach: Bool)
    case backToListTrainingViewController
    case dashboard
    case back
    case myTeamCoaches
}

enum EditPersonAndTechnicalStaffTrainingGroupViewType: Int {
    case person = 0
    case technicalStaff
}
