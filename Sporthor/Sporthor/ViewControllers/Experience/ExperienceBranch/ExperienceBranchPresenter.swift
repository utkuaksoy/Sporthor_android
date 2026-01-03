//
//  ExperienceBranchPresenter.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//
//

import Foundation

final class ExperienceBranchPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ExperienceBranchPresenterDelegate? {
        get { return self.baseView as? ExperienceBranchPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ExperienceBranchInteractorProtocol {
        get { return self.baseInteractor as! ExperienceBranchInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ExperienceBranchRouterProtocol {
        get { return self.baseRouter as! ExperienceBranchRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: ExperienceBranchPresenterDelegate,
         interactor: ExperienceBranchInteractorProtocol,
         router: ExperienceBranchRouterProtocol,
         updateProfileRequest: UpdateProfileRequest? = nil) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.updateProfileRequest = updateProfileRequest
    }
    
    var updateProfileRequest: UpdateProfileRequest?
    var model: [NameValueDetailModel] = ApplicationContext.shared.getConfiguration.branches ?? []
}

// MARK: - ExperienceBranchPresenterProtocol
extension ExperienceBranchPresenter: ExperienceBranchPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        view?.didSetTitleAndDescription(title: DesignKitL10n.Experience.Branch.title,
                                        description: DesignKitL10n.Experience.Branch.description)
        view?.didSetContinueButtonTitle(DesignKitL10n.Experience.Branch.buttonTitle)
    }
    
    private func navigate(_ routes: ExperienceBranchRoutes) {
        router.handleRouter(routes)
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
    
    func didTappedContinueButton(_ tag: Int) {
        updateProfileRequest?.branchesofInterests = model.filter({ $0.isSelected }).map({ $0.value ?? "" })
            
        if ApplicationContext.shared.isSelectedCoach {
            navigate(.experienceCoachSelected(updateProfileRequest: updateProfileRequest ?? UpdateProfileRequest()))
        } else {
            navigate(.experienceBirthdateAndGender(updateProfileRequest: updateProfileRequest ?? UpdateProfileRequest()))
        }
    }
}

// MARK: - ExperienceBranchInteractorDelegate
extension ExperienceBranchPresenter: ExperienceBranchInteractorDelegate {

}
