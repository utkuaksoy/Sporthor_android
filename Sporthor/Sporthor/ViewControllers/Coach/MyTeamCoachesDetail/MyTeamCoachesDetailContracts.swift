//
//  MyTeamCoachesDetailContracts.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//
//

import Foundation

protocol MyTeamCoachesDetailPresenterProtocol: BasePresenterProtocol {
    var view: MyTeamCoachesDetailPresenterDelegate? { get set }
    var interactor: MyTeamCoachesDetailInteractorProtocol { get set }
    var router: MyTeamCoachesDetailRouterProtocol { get set }
    var model: GetClubsAndDetailClub { get set }
    var isDeleted: Bool { get set }
    
    func viewDidLoad()
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func didTappedCKButton(_ tag: Int)
    func didSelectRowAt(_ indexPath: IndexPath)
}

protocol MyTeamCoachesDetailPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func reloadData()
    func changeDeleteSubmitButtonHiddenState(_ isHidden: Bool)
}

protocol MyTeamCoachesDetailInteractorProtocol: BaseInteractorProtocol {
    var delegate: MyTeamCoachesDetailInteractorDelegate? { get set }
    func deleteCoach(_ request: [String: Any]) async
}

protocol MyTeamCoachesDetailInteractorDelegate: BaseInteractorDelegate {
    func didDeleteCoach()
}

protocol MyTeamCoachesDetailRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: MyTeamCoachesDetailRoutes)
}

enum MyTeamCoachesDetailRoutes {
    case back
    case editUser(trainingGroup: TrainingGroupResponse, model: GetTrainingGroupUserModel, isUpdateCoach: Bool)
}
