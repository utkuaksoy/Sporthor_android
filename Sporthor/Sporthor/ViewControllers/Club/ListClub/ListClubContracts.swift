//
//  ListClubContracts.swift
//  Sporthor
//
//  Created by derTurke on 3.07.2025.
//
//

import Foundation

protocol ListClubPresenterProtocol: BasePresenterProtocol {
    var view: ListClubPresenterDelegate? { get set }
    var interactor: ListClubInteractorProtocol { get set }
    var router: ListClubRouterProtocol { get set }
    var sportClubs: [SportClub] { get set }
    var isDeleted: Bool { get set }
    
    func viewDidLoad()
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func didSelectRowAt(_ indexPath: IndexPath)
    func ckButtonDidTap(tag: Int)
}

protocol ListClubPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func reloadData()
    func changeDeleteSubmitButtonHiddenState(_ isHidden: Bool)
}

protocol ListClubInteractorProtocol: BaseInteractorProtocol {
    var delegate: ListClubInteractorDelegate? { get set }
    func getSportClub() async
    func removeSportClub(_ request: [String: Any]) async
}

protocol ListClubInteractorDelegate: BaseInteractorDelegate {
    func didGetSportClub(_ sportClubs: [SportClub])
    func didRemoveSportClub()
}

protocol ListClubRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ListClubRoutes)
}

enum ListClubRoutes {
    case back
    case editSportClub(sportClub: SportClub)
}
