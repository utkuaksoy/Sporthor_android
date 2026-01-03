//
//  AddPersonAndTechnicalStaffTrainingGroupContracts.swift
//  Sporthor
//
//  Created by derTurke on 27.10.2025.
//
//

import Foundation

protocol AddPersonAndTechnicalStaffTrainingGroupPresenterProtocol: BasePresenterProtocol {
    var view: AddPersonAndTechnicalStaffTrainingGroupPresenterDelegate? { get set }
    var interactor: AddPersonAndTechnicalStaffTrainingGroupInteractorProtocol { get set }
    var router: AddPersonAndTechnicalStaffTrainingGroupRouterProtocol { get set }
    var cellModels: [NameValueDetailModel] { get set }
    
    func viewDidLoad()
    func didTappedCellButton(_ tag: Int)
    func didTappedCKButton(_ tag: Int)
}

protocol AddPersonAndTechnicalStaffTrainingGroupPresenterDelegate: BasePresenterDelegate {
    func setupView()
    func prepareClubInfo(_ trainingGroup: TrainingGroupResponse)
    func reloadData()
}

protocol AddPersonAndTechnicalStaffTrainingGroupInteractorProtocol: BaseInteractorProtocol {
    var delegate: AddPersonAndTechnicalStaffTrainingGroupInteractorDelegate? { get set }
}

protocol AddPersonAndTechnicalStaffTrainingGroupInteractorDelegate: BaseInteractorDelegate {
}

protocol AddPersonAndTechnicalStaffTrainingGroupRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AddPersonAndTechnicalStaffTrainingGroupRoutes)
}

enum AddPersonAndTechnicalStaffTrainingGroupRoutes {
    case addPersonWithRole(_ role: TrainingGroupPersonRole,
                           trainingGroup: TrainingGroupResponse)
    case dashboard
}

enum TrainingGroupPersonRole: Int {
    case technicalStaff = 0
    case person
}
