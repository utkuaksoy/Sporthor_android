//
//  ListTrainingGroupContracts.swift
//  Sporthor
//
//  Created by derTurke on 1.07.2025.
//
//

import Foundation

protocol ListTrainingGroupPresenterProtocol: BasePresenterProtocol {
    var view: ListTrainingGroupPresenterDelegate? { get set }
    var interactor: ListTrainingGroupInteractorProtocol { get set }
    var router: ListTrainingGroupRouterProtocol { get set }
    var trainingGroups: [GetTrainingGroupUserModel] { get set }
    var isDeleted: Bool { get set }
    
    func viewDidLoad()
    func viewWillAppear()
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func didSelectRowAt(_ indexPath: IndexPath)
    func ckButtonDidTap(tag: Int)
}

protocol ListTrainingGroupPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func reloadData()
    func changeDeleteSubmitButtonHiddenState(_ isHidden: Bool)
}

protocol ListTrainingGroupInteractorProtocol: BaseInteractorProtocol {
    var delegate: ListTrainingGroupInteractorDelegate? { get set }
    func getTrainingGroupUser() async
    func removeTrainingGroup(_ request: [String: Any]) async
}

protocol ListTrainingGroupInteractorDelegate: BaseInteractorDelegate {
    func didGetTrainingGroupUser(_ response: [GetTrainingGroupUserModel])
    func didRemoveTrainingGroup()
}

protocol ListTrainingGroupRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ListTrainingGroupRoutes)
}

enum ListTrainingGroupRoutes {
    case back
    case updateTrainingGroup(model: GetTrainingGroupUserModel)
}
