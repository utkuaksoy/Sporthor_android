//
//  CreateTrainingGroupPresenter.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class CreateTrainingGroupPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CreateTrainingGroupPresenterDelegate? {
        get { return self.baseView as? CreateTrainingGroupPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CreateTrainingGroupInteractorProtocol {
        get { return self.baseInteractor as! CreateTrainingGroupInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CreateTrainingGroupRouterProtocol {
        get { return self.baseRouter as! CreateTrainingGroupRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CreateTrainingGroupPresenterDelegate,
         interactor: CreateTrainingGroupInteractorProtocol,
         router: CreateTrainingGroupRouterProtocol,
         selectedTeams: [TeamItemModel],
         model: GetTrainingGroupUserModel?,
         isLogin: Bool) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.selectedTeams = selectedTeams
        self.model = model
        self.isLogin = isLogin
    }
    var selectedTeams: [TeamItemModel] = []
    var selectedTeam: TeamItemModel?
    var suggestionNames: [String] = []
    var groupName: String = ""
    var season: [Season] = []
    var selectedSeason: Season?
    private var selectionType: CreateTrainingGroupSelectionType = .team
    private var model: GetTrainingGroupUserModel?
    private var isEdit: Bool = false
    private var isLogin: Bool = false
}

// MARK: - CreateTrainingGroupPresenterProtocol
extension CreateTrainingGroupPresenter: CreateTrainingGroupPresenterProtocol {
    func viewDidLoad() {
        getSeasons()
        view?.didSetTitleAndDescriptionText("Harika! Şimdi antrenman grubunu oluştur.",
                                            "Antrenman grubunu oluştur ve devam et.")
        view?.prepareUI()
        view?.hiddenSkipButton(isLogin)
        prepareSelectedTeam()
    }
    
    private func navigate(_ routes: CreateTrainingGroupRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didTappedCKButton(tag: Int) {
        switch tag {
        case 0: // Continue
            addTrainingGroup()
        case 1: // Skip
            navigate(.home)
        default:
            break
        }
    }
    
    func prepareSelectedTeam() {
        guard !selectedTeams.isEmpty else { return }
        selectedTeam = selectedTeams.first
        view?.reloadData()
    }
    
    func getRecomendedGroupNames() {
        Task { @MainActor in
            await interactor.getRecomendedGroupNames(clubId: selectedTeam?.value ?? "")
        }
    }
    
    func didTappedChange() {
        selectionType = .team
        if selectedTeams.count > 1 {
            let model = selectedTeams.map {
                return SelectionModel(
                    id: $0.value,
                    value: $0.name,
                    image: $0.image,
                    isSelected: selectedTeam?.value == $0.value
                )
            }
            navigate(.openSelector(title: "Kulüp Değiştir", model: model, delegate: self))
        } else {
            navigate(.back)
        }
    }
    
    func textFieldDidEndEditing(_ text: String) {
        groupName = text
        view?.reloadData()
    }
    
    func didTappedSelectionCollectionViewCell(_ tag: Int) {
        selectionType = .season
        let model = season.map {
            return SelectionModel(id: $0.value, value: $0.name, isSelected: $0.isSelected)
        }
        navigate(.openSelector(title: "Sezon Seçimi", model: model, delegate: self))
    }
    
    private func getSeasons() {
        Task { @MainActor in
            await interactor.getSeasons()
        }
    }
    
    private func addTrainingGroup() {
        guard let selectedTeam = selectedTeam else {
            showAlert(type: .warning, message: "Lütfen bir takım seçin.")
            return
        }
        
        guard let selectedSeason = selectedSeason else {
            showAlert(type: .warning, message: "Lütfen bir sezon seçin.")
            return
        }
        
        guard !groupName.isEmpty else {
            showAlert(type: .warning, message: "Lütfen antrenman grubu adını girin.")
            return
        }
        
        var request: [String: Any] = [
            "teamId": selectedTeam.value ?? "",
            "groupName": groupName,
            "season": selectedSeason.value
        ]
        
        if let model {
            request["id"] = model.groupId
        }
        
        Task { @MainActor in
            await interactor.addTrainingGroup(request, isEdit: isEdit)
        }
    }
    
    func prepareEditTrainingGroup() {
        if let model {
            isEdit = true
            if let team = model.team {
                let team = TeamItemModel(name: team.name,
                                         value: team.value,
                                         detail: team.detail,
                                         image: team.detail)
                selectedTeams = [team]
                prepareSelectedTeam()
            }
            selectedSeason = season.first(where: { $0.value == model.season })
            groupName = model.groupName
            view?.reloadData()
        }
    }
}

// MARK: - CreateTrainingGroupInteractorDelegate
extension CreateTrainingGroupPresenter: CreateTrainingGroupInteractorDelegate {
    func didGetSeasons(_ seasons: [Season]) {
        self.season = seasons
        prepareEditTrainingGroup()
        getRecomendedGroupNames()
        view?.reloadData()
    }
    
    func didAddTrainingGroup(_ response: TrainingGroupResponse) {
        navigate(.successTrainingGroup(trainingGroup: response, model: isEdit ? model : nil))
    }
    
    func didGetRecomendedGroupNames(_ names: [String]) {
        suggestionNames = names
        view?.reloadData()
    }
}

extension CreateTrainingGroupPresenter: SelectionViewDelegate {
    func didSelectionItem(with model: SelectionModel) {
        switch selectionType {
        case .team:
            selectedTeam = selectedTeams.first(where: {
                return $0.value == model.id
            })
        case .season:
            season.enumerated().forEach { (index, value) in
                season[index].isSelected = false
            }
            
            if let index = season.firstIndex(where: { $0.value == model.id }) {
                season[index].isSelected.toggle()
                selectedSeason = season[index]
            }
        }
        
        view?.reloadData()
    }
}
