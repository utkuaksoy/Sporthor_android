//
//  AddPersonTrainingGroupContracts.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

protocol AddPersonTrainingGroupPresenterProtocol: BasePresenterProtocol {
    var view: AddPersonTrainingGroupPresenterDelegate? { get set }
    var interactor: AddPersonTrainingGroupInteractorProtocol { get set }
    var router: AddPersonTrainingGroupRouterProtocol { get set }
    var viewType: AddPersonTrainingGroupViewType { get set }
    var searches: [SearchList] { get set }
    var selectedSearches: [SearchList] { get set }
    
    func viewDidLoad()
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func search(_ text: String)
    func searchCancel()
    func didSelectRowAt(_ indexPath: IndexPath)
    func didTappedButton(_ tag: Int)
}

protocol AddPersonTrainingGroupPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func prepareClub(image: String, name: String, groupName: String)
    func reloadData()
}

protocol AddPersonTrainingGroupInteractorProtocol: BaseInteractorProtocol {
    var delegate: AddPersonTrainingGroupInteractorDelegate? { get set }
    func search(_ request: [String : Any]) async
    func addTrainingGroupUser(_ request: [String: Any]) async
    func updateCoach(_ request: [String: Any]) async
}

protocol AddPersonTrainingGroupInteractorDelegate: BaseInteractorDelegate {
    func didSearch(_ searchList: [SearchList])
    func didAddTrainingGroupUser()
}

protocol AddPersonTrainingGroupRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AddPersonTrainingGroupRoutes)
}

enum AddPersonTrainingGroupRoutes {
    case back
    case dashboard
}

enum AddPersonTrainingGroupViewType {
    case search
    case selection
}
