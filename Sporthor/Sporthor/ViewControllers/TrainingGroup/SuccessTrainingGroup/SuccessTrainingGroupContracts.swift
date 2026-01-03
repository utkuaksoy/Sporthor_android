//
//  SuccessTrainingGroupContracts.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

protocol SuccessTrainingGroupPresenterProtocol: BasePresenterProtocol {
    var view: SuccessTrainingGroupPresenterDelegate? { get set }
    var interactor: SuccessTrainingGroupInteractorProtocol { get set }
    var router: SuccessTrainingGroupRouterProtocol { get set }
    
    func viewDidLoad()
    func didTappedCKButton(_ tag: Int)
}

protocol SuccessTrainingGroupPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func prepareTeam(image: String, name: String, groupName: String)
    func prepareSuccessHeaderAndDescription(header: String, description: String)
    
}

protocol SuccessTrainingGroupInteractorProtocol: BaseInteractorProtocol {
    var delegate: SuccessTrainingGroupInteractorDelegate? { get set }
}

protocol SuccessTrainingGroupInteractorDelegate: BaseInteractorDelegate {
}

protocol SuccessTrainingGroupRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: SuccessTrainingGroupRoutes)
}

enum SuccessTrainingGroupRoutes {
    case addPersonTraining(trainingGroup: TrainingGroupResponse,
                           model: GetTrainingGroupUserModel?)
    case editPersonTraining(trainingGroup: TrainingGroupResponse,
                            model: GetTrainingGroupUserModel?) 
}
