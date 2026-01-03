//
//  ExperienceCoachSelectedPresenter.swift
//  Sporthor
//
//  Created by derTurke on 16.05.2025.
//
//

import Foundation

final class ExperienceCoachSelectedPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ExperienceCoachSelectedPresenterDelegate? {
        get { return self.baseView as? ExperienceCoachSelectedPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ExperienceCoachSelectedInteractorProtocol {
        get { return self.baseInteractor as! ExperienceCoachSelectedInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ExperienceCoachSelectedRouterProtocol {
        get { return self.baseRouter as! ExperienceCoachSelectedRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: ExperienceCoachSelectedPresenterDelegate,
         interactor: ExperienceCoachSelectedInteractorProtocol,
         router: ExperienceCoachSelectedRouterProtocol,
         updateProfileRequest: UpdateProfileRequest? = nil) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.updateProfileRequest = updateProfileRequest
    }
    
    private var updateProfileRequest: UpdateProfileRequest?
    var model: [NameValueDetailModel] = ApplicationContext.shared.getConfiguration.coachRoles ?? []
}

// MARK: - ExperienceCoachSelectedPresenterProtocol
extension ExperienceCoachSelectedPresenter: ExperienceCoachSelectedPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        view?.didSetTitleAndDescription(title: "Antrenör rolün nedir?",
                                        description: "*Çoklu seçim yapabilirsin.")
        view?.didSetContinueButtonTitle("Devam Et")
    }
    
    private func navigate(_ routes: ExperienceCoachSelectedRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didSelectItemAt(_ indexPath: IndexPath) {
        var item = model[indexPath.item]
        item.isSelected = !item.isSelected
        model[indexPath.item] = item
        checkButtonState()
    }
    
    private func checkButtonState() {
        let isEnabled = model.contains { $0.isSelected }
        view?.updateContinueButtonEnabled(isEnabled)
        view?.reloadData()
    }
    
    func didTappedContinueButton() {
        updateProfileRequest?.coachRoles = model.filter({ $0.isSelected }).map({ $0.value ?? "" })
        navigate(.experienceBirthdateAndGender(profileRequest: updateProfileRequest ?? UpdateProfileRequest()))
    }
}

// MARK: - ExperienceCoachSelectedInteractorDelegate
extension ExperienceCoachSelectedPresenter: ExperienceCoachSelectedInteractorDelegate {

}
