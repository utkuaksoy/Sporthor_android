//
//  ExperienceBirthdateAndGenderContracts.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//
//

import Foundation

protocol ExperienceBirthdateAndGenderPresenterProtocol: BasePresenterProtocol {
    var view: ExperienceBirthdateAndGenderPresenterDelegate? { get set }
    var interactor: ExperienceBirthdateAndGenderInteractorProtocol { get set }
    var router: ExperienceBirthdateAndGenderRouterProtocol { get set }
    var gender: [String] { get set }
    var selectedGenderIndex: Int? { get set }
    var day: String { get set }
    var month: String { get set }
    var year: String { get set }
    
    func viewDidLoad()
    func selectedGender(_ index: Int)
    func checkMaxLengthAndFocusNextTextField(text: String, tag: Int, indexPath: IndexPath)
    func textFieldDidEndEditing(text: String, tag: Int)
    func didTappedButton(_ tag: Int)
}

protocol ExperienceBirthdateAndGenderPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func didSetHeaderTitle(_ title: String)
    func didSetContinueButtonTitle(_ title: String)
    func reloadData()
    func updateContinueButtonEnabled(_ isEnabled: Bool)
    func didSetFocusTextField(at indexPath: IndexPath)
}

protocol ExperienceBirthdateAndGenderInteractorProtocol: BaseInteractorProtocol {
    var delegate: ExperienceBirthdateAndGenderInteractorDelegate? { get set }
    
    func updateProfile(_ request: [String: Any]) async
}

protocol ExperienceBirthdateAndGenderInteractorDelegate: BaseInteractorDelegate {
    func didUpdateProfile()
}

protocol ExperienceBirthdateAndGenderRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ExperienceBirthdateAndGenderRoutes)
}

enum ExperienceBirthdateAndGenderRoutes {
    case nextOnboarding
}

enum BirthdateTextFieldMaxLength {
    case birthdateDay
    case birthdateMonth
    case birthDateYear
    
    func rawValue() -> Int {
        switch self {
        case .birthdateDay:
            return 2
        case .birthdateMonth:
            return 2
        case .birthDateYear:
            return 4
        }
    }
}
