//
//  MyTeamCoachesContracts.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//
//

import Foundation

protocol MyTeamCoachesPresenterProtocol: BasePresenterProtocol {
    var view: MyTeamCoachesPresenterDelegate? { get set }
    var interactor: MyTeamCoachesInteractorProtocol { get set }
    var router: MyTeamCoachesRouterProtocol { get set }
    var clubs: [GetClubsAndDetailClub] { get set }
    
    func viewDidLoad()
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func didSelectRowAt(_ indexPath: IndexPath)
}

protocol MyTeamCoachesPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func reloadData()
}

protocol MyTeamCoachesInteractorProtocol: BaseInteractorProtocol {
    var delegate: MyTeamCoachesInteractorDelegate? { get set }
    func getClubsAndDetails(_ request: [String: Any]) async
}

protocol MyTeamCoachesInteractorDelegate: BaseInteractorDelegate {
    func didGetClubsAndDetails(_ clubs: [GetClubsAndDetailClub])
}

protocol MyTeamCoachesRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: MyTeamCoachesRoutes)
}

enum MyTeamCoachesRoutes {
    case detail(model: GetClubsAndDetailClub)
    case back
}
