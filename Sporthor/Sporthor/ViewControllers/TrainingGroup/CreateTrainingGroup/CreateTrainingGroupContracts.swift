//
//  CreateTrainingGroupContracts.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

protocol CreateTrainingGroupPresenterProtocol: BasePresenterProtocol {
    var view: CreateTrainingGroupPresenterDelegate? { get set }
    var interactor: CreateTrainingGroupInteractorProtocol { get set }
    var router: CreateTrainingGroupRouterProtocol { get set }
    var selectedTeams: [TeamItemModel] { get set }
    var suggestionNames: [String] { get set }
    var selectedTeam: TeamItemModel? { get set }
    var groupName: String { get set }
    var season: [Season] { get set }
    var selectedSeason: Season? { get set }
    
    func viewDidLoad()
    func didTappedCKButton(tag: Int)
    func didTappedChange()
    func textFieldDidEndEditing(_ text: String)
    func didTappedSelectionCollectionViewCell(_ tag: Int)
}

protocol CreateTrainingGroupPresenterDelegate: BasePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String)
    func prepareUI()
    func reloadData()
    func hiddenSkipButton(_ isHidden: Bool)
}

protocol CreateTrainingGroupInteractorProtocol: BaseInteractorProtocol {
    var delegate: CreateTrainingGroupInteractorDelegate? { get set }
    func getSeasons() async
    func addTrainingGroup(_ request: [String: Any], isEdit: Bool) async
    func getRecomendedGroupNames(clubId: String) async
}

protocol CreateTrainingGroupInteractorDelegate: BaseInteractorDelegate {
    func didGetSeasons(_ seasons: [Season])
    func didAddTrainingGroup(_ response: TrainingGroupResponse)
    func didGetRecomendedGroupNames(_ names: [String])
}

protocol CreateTrainingGroupRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CreateTrainingGroupRoutes)
}

enum CreateTrainingGroupRoutes {
    case home
    case back
    case openSelector(title: String,
                      model: [SelectionModel],
                      delegate: SelectionViewDelegate?)
    case successTrainingGroup(trainingGroup: TrainingGroupResponse,
                              model: GetTrainingGroupUserModel?)
}

enum CreateTrainingGroupSelectionType {
    case team
    case season
}
