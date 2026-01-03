//
//  AuthenticationTeamContracts.swift
//  Sporthor
//
//  Created by derTurke on 24.02.2025.
//
//

import Foundation

protocol AuthenticationTeamPresenterProtocol: BasePresenterProtocol {
    var view: AuthenticationTeamPresenterDelegate? { get set }
    var interactor: AuthenticationTeamInteractorProtocol { get set }
    var router: AuthenticationTeamRouterProtocol { get set }
    var filteredList: [TeamItemModel] { get set }
    var isEmptyList: Bool { get set }
    
    func viewDidLoad()
    func viewWillAppear()
    func viewWillDisappear()
    func searching(_ text: String)
    func selectedTeam(_ model: TeamItemModel)
    func didTappedButton(tag: Int)
    func openCreateClub()
}

protocol AuthenticationTeamPresenterDelegate: BasePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String)
    func prepareUI()
    func reloadData()
    func prepareCoachButton()
    func prepareClubOfficialButton()
    func preparePlayerButton()
    func hiddenSecondButton(_ isHidden: Bool)
    func setNavigationBarHidden(_ isHidden: Bool)
}

protocol AuthenticationTeamInteractorProtocol: BaseInteractorProtocol {
    var delegate: AuthenticationTeamInteractorDelegate? { get set }
    
    func getTeams() async
    func saveUserTeams(_ request: [String: Any]) async
}

protocol AuthenticationTeamInteractorDelegate: BaseInteractorDelegate {
    func didGetTeams(_ response: TeamResponse)
    func didSaveUserTeams(_ response: SaveUserTeamResponse)
}

protocol AuthenticationTeamRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AuthenticationTeamRoutes)
}

enum AuthenticationTeamRoutes {
    case home
    case trainingGroup(selectedTeams: [TeamItemModel], isLogin: Bool)
    case createClub(isLogin: Bool)
    case sendClubAuthorizationLetter(sportClub: SportClub)
}
