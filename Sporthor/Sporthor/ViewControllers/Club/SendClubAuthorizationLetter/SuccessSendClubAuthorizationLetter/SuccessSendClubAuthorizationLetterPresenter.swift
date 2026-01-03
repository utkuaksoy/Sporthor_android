//
//  SuccessSendClubAuthorizationLetterPresenter.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import Foundation

final class SuccessSendClubAuthorizationLetterPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: SuccessSendClubAuthorizationLetterPresenterDelegate? {
        get { return self.baseView as? SuccessSendClubAuthorizationLetterPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: SuccessSendClubAuthorizationLetterInteractorProtocol {
        get { return self.baseInteractor as! SuccessSendClubAuthorizationLetterInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: SuccessSendClubAuthorizationLetterRouterProtocol {
        get { return self.baseRouter as! SuccessSendClubAuthorizationLetterRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: SuccessSendClubAuthorizationLetterPresenterDelegate,
         interactor: SuccessSendClubAuthorizationLetterInteractorProtocol,
         router: SuccessSendClubAuthorizationLetterRouterProtocol,
         sportClub: SportClub) {
        self.sportClub = sportClub
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    private var sportClub: SportClub
}

// MARK: - SuccessSendClubAuthorizationLetterPresenterProtocol
extension SuccessSendClubAuthorizationLetterPresenter: SuccessSendClubAuthorizationLetterPresenterProtocol {
    func viewDidLoad() {
        view?.prepareSuccessHeaderAndDescription(
            header: "Belgelerini inceleyeceğiz",
            description: "Belgelerin incelenecek. Uygun bulunması halinde profilinde gerekli izinler ve yetkilendirmeler sağlanacaktır.\n\nDoğrulama tamamlandığında sana bildirim göndereceğiz.\n\nAyrıca, belgelerini Profil Ayarları bölümünden görüntüleyebilirsin."
        )
        view?.prepareUI()
        prepareContinueButton()
    }
    
    private func navigate(_ routes: SuccessSendClubAuthorizationLetterRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    private func prepareContinueButton() {
        if ApplicationContext.shared.isSelectedClubOfficial &&
            ApplicationContext.shared.isSelectedCoach {
            view?.prepareContinueButton("Devam Et")
        } else if ApplicationContext.shared.isSelectedCoach {
            view?.prepareContinueButton("Devam Et")
        } else {
            view?.prepareContinueButton("Anasayfa")
        }
    }
    
    func didTappedHomeButton() {
        let teams: [TeamItemModel] = [
            TeamItemModel(name: sportClub.clubName,
                          value: sportClub.id,
                          image: sportClub.logo)
        ]
        if ApplicationContext.shared.isSelectedClubOfficial &&
            ApplicationContext.shared.isSelectedCoach {
            navigate(.createTrainingGroup(teams: teams))
        } else if ApplicationContext.shared.isSelectedCoach {
            navigate(.createTrainingGroup(teams: teams))
        } else {
            navigate(.home)
        }
    }
}

// MARK: - SuccessSendClubAuthorizationLetterInteractorDelegate
extension SuccessSendClubAuthorizationLetterPresenter: SuccessSendClubAuthorizationLetterInteractorDelegate {

}
