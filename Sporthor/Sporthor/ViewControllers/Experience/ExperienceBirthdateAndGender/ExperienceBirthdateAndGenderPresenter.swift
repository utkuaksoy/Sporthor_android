//
//  ExperienceBirthdateAndGenderPresenter.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//
//

import Foundation

final class ExperienceBirthdateAndGenderPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ExperienceBirthdateAndGenderPresenterDelegate? {
        get { return self.baseView as? ExperienceBirthdateAndGenderPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ExperienceBirthdateAndGenderInteractorProtocol {
        get { return self.baseInteractor as! ExperienceBirthdateAndGenderInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ExperienceBirthdateAndGenderRouterProtocol {
        get { return self.baseRouter as! ExperienceBirthdateAndGenderRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: ExperienceBirthdateAndGenderPresenterDelegate,
         interactor: ExperienceBirthdateAndGenderInteractorProtocol,
         router: ExperienceBirthdateAndGenderRouterProtocol,
         updateProfileRequest: UpdateProfileRequest? = nil) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.updateProfileRequest = updateProfileRequest
    }
    var gender: [String] = []
    var selectedGenderIndex: Int?
    var day: String = ""
    var month: String = ""
    var year: String = ""
    private var birthdate: String = ""
    private var updateProfileRequest: UpdateProfileRequest?
}

// MARK: - ExperienceBirthdateAndGenderPresenterProtocol
extension ExperienceBirthdateAndGenderPresenter: ExperienceBirthdateAndGenderPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        view?.didSetHeaderTitle(DesignKitL10n.Experience.BirthdateAndGender.title)
        view?.didSetContinueButtonTitle(DesignKitL10n.Experience.BirthdateAndGender.buttonTitle)
        prepareGender()
    }
    
    private func navigate(_ routes: ExperienceBirthdateAndGenderRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    private func prepareGender() {
        gender = ["Kadın", "Erkek", "Belirtmek istemiyorum"]
    }
    
    func selectedGender(_ index: Int) {
        selectedGenderIndex = index
        checkButtonState()
        view?.reloadData()
    }
    
    func checkMaxLengthAndFocusNextTextField(text: String, tag: Int, indexPath: IndexPath) {
        let maxLength: Int
        switch tag {
        case 0: maxLength = BirthdateTextFieldMaxLength.birthdateDay.rawValue()
        case 1: maxLength = BirthdateTextFieldMaxLength.birthdateMonth.rawValue()
        case 2: maxLength = BirthdateTextFieldMaxLength.birthDateYear.rawValue()
        default: return
        }
        
        var focusedIndexPath: IndexPath = indexPath
        if text.count == maxLength {
            focusedIndexPath = IndexPath(item: indexPath.item + 1,
                                         section: indexPath.section)
        } else if text.isEmpty, tag > 0 {
            focusedIndexPath = IndexPath(item: indexPath.item - 1,
                                         section: indexPath.section)
        }
        view?.didSetFocusTextField(at: focusedIndexPath)
    }


    func textFieldDidEndEditing(text: String, tag: Int) {
        switch tag {
        case 0: day = text
        case 1: month = text
        case 2: year = text
        default: return
        }
        
        birthdate = [day, month, year].filter { !$0.isEmpty }.joined(separator: ".")
        
        checkButtonState()
    }
    
    private func checkButtonState() {
        guard let _ = birthdate.toDate(),
              let _ = selectedGenderIndex else {
            view?.updateContinueButtonEnabled(false)
            return
        }
        
        view?.updateContinueButtonEnabled(true)
    }
    
    func didTappedButton(_ tag: Int) {
        updateProfileRequest?.birthDate = birthdate
        updateProfileRequest?.gender = selectedGenderIndex
        Task {
            @MainActor in
            await interactor.updateProfile(updateProfileRequest?.dictionary() ?? [:])
        }
    }
}

// MARK: - ExperienceBirthdateAndGenderInteractorDelegate
extension ExperienceBirthdateAndGenderPresenter: ExperienceBirthdateAndGenderInteractorDelegate {
    func didUpdateProfile() {
        navigate(.nextOnboarding)
    }
}
