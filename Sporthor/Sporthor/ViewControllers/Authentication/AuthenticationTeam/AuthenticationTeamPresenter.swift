//
//  AuthenticationTeamPresenter.swift
//  Sporthor
//
//  Created by derTurke on 24.02.2025.
//
//

import Foundation

final class AuthenticationTeamPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AuthenticationTeamPresenterDelegate? {
        get { return self.baseView as? AuthenticationTeamPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AuthenticationTeamInteractorProtocol {
        get { return self.baseInteractor as! AuthenticationTeamInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AuthenticationTeamRouterProtocol {
        get { return self.baseRouter as! AuthenticationTeamRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AuthenticationTeamPresenterDelegate,
         interactor: AuthenticationTeamInteractorProtocol,
         router: AuthenticationTeamRouterProtocol,
         isLogin: Bool = false,
         isCoach: Bool = false,
         isManager: Bool = false) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.isLogin = isLogin
        self.isCoach = isCoach
        self.isManager = isManager
    }
    
    private var teamList: [TeamItemModel] = []
    var filteredList: [TeamItemModel] = []
    private var selectedTeamList: [String] = []
    var isEmptyList: Bool = false
    private var selectedTeamArray: [TeamItemModel] = []
    private var isLogin: Bool = false
    private var isCoach: Bool = false
    private var isManager: Bool = false
}

// MARK: - AuthenticationTeamPresenterProtocol
extension AuthenticationTeamPresenter: AuthenticationTeamPresenterProtocol {
    func viewDidLoad() {
        preapareTitleAndDescription()
        view?.prepareUI()
        getTeams()
        prepareButtonWithSelectedBranch()
    }
    
    func viewWillAppear() {
        view?.setNavigationBarHidden(!isLogin)
    }
    
    func viewWillDisappear() {
        view?.setNavigationBarHidden(false)
    }
    
    private func navigate(_ routes: AuthenticationTeamRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    private func preapareTitleAndDescription() {
        var title: String = ""
        var description: String = ""
        if isLogin {
            if isCoach {
                title = "Hangi kulüpte antrenörsün?"
                description = "Antrenörü olduğun kulübü listeden seçebilirsin."
            } else if isManager {
                title = "Hangi kulüpte yetkilisin?"
                description = "Yetkilisi olduğun kulübü listeden seçebilirsin. Kulübün listede yoksa kulüp oluştur."
            }
        } else {
            if ApplicationContext.shared.isSelectedClubOfficial && ApplicationContext.shared.isSelectedCoach {
                title = "Hangi kulüpte yetkilisin?"
                description = "Yetkilisi olduğun kulübü listeden seçebilirsin. Kulübün listede yoksa kulüp oluştur."
            } else if ApplicationContext.shared.isSelectedClubOfficial {
                title = "Hangi kulüpte yetkilisin?"
                description = "Yetkilisi olduğun kulübü listeden seçebilirsin. Kulübün listede yoksa kulüp oluştur."
            } else {
                title = "Hangi kulüpte antrenörsün?"
                description = "Antrenörü olduğun kulübü listeden seçebilirsin."
            }
        }
        
        view?.didSetTitleAndDescriptionText(title, description)
    }
    
    func searching(_ text: String) {
        if text.count > 2 {
            filteredList = teamList.filter({ $0.name?.lowercased().contains(text.lowercased()) ?? false })
        } else {
            filteredList = teamList
        }
        
        isEmptyList = filteredList.isEmpty
        
        view?.reloadData()
    }
    
    private func getTeams() {
        Task {
            @MainActor in
            await interactor.getTeams()
        }
    }
    
    func selectedTeam(_ model: TeamItemModel) {
        if isCoach {
            selectedTeamList.removeAll()
            selectedTeamArray.removeAll()

            var updatedModel = model
            updatedModel.isSelected = true
            selectedTeamList.append(model.value ?? "")
            selectedTeamArray.append(updatedModel)

            for index in filteredList.indices {
                filteredList[index].isSelected = (filteredList[index].value == model.value)
            }

            for index in teamList.indices {
                teamList[index].isSelected = (teamList[index].value == model.value)
            }
        } else {
            if let index = selectedTeamList.firstIndex(where: { $0 == model.value }) {
                selectedTeamList.remove(at: index)
                selectedTeamArray.remove(at: index)
            } else {
                var updatedModel = model
                updatedModel.isSelected = true
                selectedTeamList.append(model.value ?? "")
                selectedTeamArray.append(updatedModel)
            }

            if let index = filteredList.firstIndex(where: { $0.value == model.value }) {
                filteredList[index].isSelected.toggle()
            }
            
            if let index = teamList.firstIndex(where: { $0.value == model.value }) {
                teamList[index].isSelected.toggle()
            }
        }
        view?.reloadData()
    }
    
    func didTappedButton(tag: Int) {
        switch tag {
        case 0:
            continueButtonTapped()
        case 1:
            secondButtonTapped()
        default:
            break
        }
    }
    
    private func continueButtonTapped() {
        guard !selectedTeamList.isEmpty else {
            showAlert(type: .warning,
                      message: "En az 1 adet takım seçmelisiniz.")
            return
        }
        // TODO: Burası düzeltilecek
//        navigate(.trainingGroup(selectedTeams: teamList.filter({ $0.isSelected })))
        let request: [String: Any] = ["teams": selectedTeamList]
        Task {
            @MainActor in
            await interactor.saveUserTeams(request)
        }
    }
    
    private func prepareButtonWithSelectedBranch() {
        if isLogin {
            if isCoach {
                view?.hiddenSecondButton(true)
            } else if isManager {
                view?.prepareClubOfficialButton()
            }
        } else {
            if ApplicationContext.shared.isSelectedCoach &&
                ApplicationContext.shared.isSelectedClubOfficial {
                view?.prepareClubOfficialButton()
            } else if ApplicationContext.shared.isSelectedClubOfficial {
                view?.prepareClubOfficialButton()
            } else {
                view?.prepareCoachButton()
            }
        }
    }
    
    private func secondButtonTapped() {
        if isLogin {
            if isManager {
               openCreateClub()
            }
        } else {
            if ApplicationContext.shared.isSelectedCoach &&
                ApplicationContext.shared.isSelectedClubOfficial {
                openCreateClub()
            } else if ApplicationContext.shared.isSelectedClubOfficial {
                openCreateClub()
            } else {
                navigate(.home)
            }
        }
    }
    
    func openCreateClub() {
        navigate(.createClub(isLogin: isLogin))
    }
}

// MARK: - AuthenticationTeamInteractorDelegate
extension AuthenticationTeamPresenter: AuthenticationTeamInteractorDelegate {
    func didGetTeams(_ response: TeamResponse) {
        teamList = response.teams ?? []
        filteredList = teamList
        view?.reloadData()
    }
    
    func didSaveUserTeams(_ response: SaveUserTeamResponse) {
        let sportClub = SportClub(id: selectedTeamArray.first?.value ?? "",
                                  clubName: selectedTeamArray.first?.name ?? "",
                                  logo: selectedTeamArray.first?.image ?? "")
        if ApplicationContext.shared.isSelectedClubOfficial && ApplicationContext.shared.isSelectedCoach {
            navigate(.sendClubAuthorizationLetter(sportClub: sportClub))
        } else if ApplicationContext.shared.isSelectedClubOfficial || (isLogin && isManager) {
            navigate(.sendClubAuthorizationLetter(sportClub: sportClub))
        } else if ApplicationContext.shared.isSelectedCoach || (isLogin && isCoach) {
            let selectedTeams = teamList.filter({ $0.isSelected })
            navigate(.trainingGroup(selectedTeams: selectedTeams, isLogin: isLogin))
        } else {
            navigate(.home)
        }
    }
}
