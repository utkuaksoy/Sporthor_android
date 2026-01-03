//
//  ExperienceJobPresenter.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//
//

import Foundation

final class ExperienceJobPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ExperienceJobPresenterDelegate? {
        get { return self.baseView as? ExperienceJobPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ExperienceJobInteractorProtocol {
        get { return self.baseInteractor as! ExperienceJobInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ExperienceJobRouterProtocol {
        get { return self.baseRouter as! ExperienceJobRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: ExperienceJobPresenterDelegate,
         interactor: ExperienceJobInteractorProtocol,
         router: ExperienceJobRouterProtocol,
         isEdit: Bool = false) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.isEdit = isEdit
    }
    
    var model: [NameValueDetailModel] = ApplicationContext.shared.getConfiguration.userRoles ?? []
    private var updateProfileRequest: UpdateProfileRequest = UpdateProfileRequest()
    var isEdit: Bool = false
}

// MARK: - ExperienceJobPresenterProtocol
extension ExperienceJobPresenter: ExperienceJobPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        view?.didSetTitleAndDescription(
            title: isEdit ? "Rolünü Güncelle" : DesignKitL10n.Experience.Job.title,
            description: isEdit ? "En az birini seçerek güncelleme işlemi yapabilirsiniz" : DesignKitL10n.Experience.Job.description
        )
        view?.didSetContinueButtonTitle(isEdit ? "Güncelle" : DesignKitL10n.Experience.Job.buttonTitle)
        getMyRoles()
    }
    
    private func getMyRoles() {
        if isEdit {
            view?.updateEditView()
            Task { @MainActor in
                await interactor.getMyRoles()
            }
        }
    }
    
    private func navigate(_ routes: ExperienceJobRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didSelectItemAt(_ indexPath: IndexPath) {
        var item = model[indexPath.item]
        item.isSelected = !item.isSelected
        model[indexPath.item] = item
        // TODO: Antranör ve Kulüp Yetkilisi seçim kontrolü yapılacak
        if !isEdit {
            if item.value == "1" {
                ApplicationContext.shared.isSelectedCoach = item.isSelected
            } else if item.value == "2" {
                ApplicationContext.shared.isSelectedClubOfficial = item.isSelected
            }
        }
        checkButtonState()
    }
    
    private func checkButtonState() {
        let isEnabled = model.contains { $0.isSelected }
        view?.updateContinueButtonEnabled(isEnabled)
        view?.reloadData()
    }
    
    func didTappedContinueButton(_ tag: Int) {
        updateProfileRequest.userRoles = model.filter({ return $0.isSelected }).map({ return $0.value ?? "" })
        if isEdit {
            Task { @MainActor in
                await interactor.updateUserRoles(["roles": updateProfileRequest.userRoles ?? []])
            }
        } else {
            navigate(.experienceBranch(updateProfileRequest: updateProfileRequest))
        }
    }
    
    func back() {
        navigate(.back)
    }
}

// MARK: - ExperienceJobInteractorDelegate
extension ExperienceJobPresenter: ExperienceJobInteractorDelegate {
    func didGetMyRoles(_ roles: [String]) {
        for (index, item) in model.enumerated() {
            if roles.contains(item.value ?? "") {
                model[index].isSelected = true
            }
        }
        checkButtonState()
    }
    
    func didUpdateUserRoles(_ roles: [String]) {
        didGetMyRoles(roles)
        showAlert(delegate: self,
                  type: .success,
                  message: "Rolünüz başarıyla güncellenmiştir.")
    }
}

// MARK: - AlertViewDelegate
extension ExperienceJobPresenter: AlertViewDelegate {
    func didTappedAlertButton(_ tag: Int) {
        back()
    }
}
