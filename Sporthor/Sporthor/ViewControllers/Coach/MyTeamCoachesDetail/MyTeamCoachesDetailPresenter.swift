//
//  MyTeamCoachesDetailPresenter.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//
//

import Foundation
import ComponentKit

final class MyTeamCoachesDetailPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: MyTeamCoachesDetailPresenterDelegate? {
        get { return self.baseView as? MyTeamCoachesDetailPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: MyTeamCoachesDetailInteractorProtocol {
        get { return self.baseInteractor as! MyTeamCoachesDetailInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: MyTeamCoachesDetailRouterProtocol {
        get { return self.baseRouter as! MyTeamCoachesDetailRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: MyTeamCoachesDetailPresenterDelegate,
         interactor: MyTeamCoachesDetailInteractorProtocol,
         router: MyTeamCoachesDetailRouterProtocol,
         model: GetClubsAndDetailClub) {
        self.model = model
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var model: GetClubsAndDetailClub
    var isDeleted: Bool = false
}

// MARK: - MyTeamCoachesDetailPresenterProtocol
extension MyTeamCoachesDetailPresenter: MyTeamCoachesDetailPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle(model.name)
        view?.prepareNavigationBar()
        view?.prepareUI()
        view?.changeDeleteSubmitButtonHiddenState(true)
    }
    
    private func navigate(_ routes: MyTeamCoachesDetailRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back)
        case .textRight:
            isDeleted.toggle()
            view?.changeDeleteSubmitButtonHiddenState(!isDeleted)
            if isDeleted {
                for index in model.coaches.indices { model.coaches[index].isSelected = false }
            }
            view?.reloadData()
        default:
            break
        }
    }
    
    func didTappedCKButton(_ tag: Int) {
        switch tag {
        case 1:
            guard let selectedCoach = model.coaches.first(where: { $0.isSelected }) else {
                showAlert(type: .warning, message: "Silme işlemi yapabilmek için 1 adet antrenör seçmelisiniz!")
                return
            }
            showCKDefaultAlert(delegate: self, message: "\(selectedCoach.name) adlı antrenörü silmek istediğinize emin misiniz?",
                               okTitle: "Evet",
                               cancelTitle: "Hayır")
        default:
            break
        }
    }
    
    func didSelectRowAt(_ indexPath: IndexPath) {
        switch indexPath.section {
        case 0:
            guard let trainingGroup = model.trainingGroups[safe: indexPath.row] else { return }
            let trainingGroupResponse: TrainingGroupResponse = TrainingGroupResponse(
                trainingGroupId: trainingGroup.id,
                teamName: model.name,
                logo: model.logo,
                teamId: "",
                season: "",
                groupName: trainingGroup.name
            )
            let userModel: GetTrainingGroupUserModel = GetTrainingGroupUserModel(
                groupName: trainingGroup.name,
                groupImage: model.logo,
                groupId: trainingGroup.id,
                season: "",
                users: [trainingGroup.coach ?? GetTrainingGroupUserModelUser(id: "", name: "", username: "", summary: "", imageUrl: "", isFollow: false, isCurrentUser: false, isSelected: false)],
                coaches: trainingGroup.coaches,
                isSelected: false
            )
            navigate(.editUser(trainingGroup: trainingGroupResponse, model: userModel, isUpdateCoach: true))
        case 1:
            guard let selectedCoach = model.coaches[safe: indexPath.row] else { return }
            for index in model.coaches.indices {
                let isSameCoach = model.coaches[index].id == selectedCoach.id
                model.coaches[index].isSelected = isSameCoach ? !model.coaches[index].isSelected : false
            }
            
            view?.reloadData()
        default:
            break
        }
    }
}

// MARK: - MyTeamCoachesDetailInteractorDelegate
extension MyTeamCoachesDetailPresenter: MyTeamCoachesDetailInteractorDelegate {
    func didDeleteCoach() {
        guard let selectedCoaches = model.coaches.first(where: { $0.isSelected }) else { return }
        showAlert(type: .success, message: "\(selectedCoaches.name) adlı antrenör başarıyla silinmiştir.")
        model.coaches.removeAll { $0.id == selectedCoaches.id }
        view?.reloadData()
    }
}

extension MyTeamCoachesDetailPresenter: CKDefaultAlertDelegate {
    func ckDefaultAlertDidTapOK() {
        guard let selectedCoach = model.coaches.first(where: { $0.isSelected }),
              let trainingGroup = model.trainingGroups.first(where: { $0.coach?.id == selectedCoach.id }) else { return }
        let request: [String: Any] = ["coachId": selectedCoach.id,
                                      "trainingGroupId": trainingGroup.id]
        Task { @MainActor in
            await interactor.deleteCoach(request)
        }
    }
}
