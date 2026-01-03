//
//  StorySettingContracts.swift
//  Sporthor
//
//  Created by derTurke on 11.05.2025.
//
//

import Foundation

protocol StorySettingPresenterProtocol: BasePresenterProtocol {
    var view: StorySettingPresenterDelegate? { get set }
    var interactor: StorySettingInteractorProtocol { get set }
    var router: StorySettingRouterProtocol { get set }
    var items: [StorySettingItems] { get set }
    
    func viewDidLoad()
    func didSelectRow(at indexPath: IndexPath)
}

protocol StorySettingPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func reloadData()
}

protocol StorySettingInteractorProtocol: BaseInteractorProtocol {
    var delegate: StorySettingInteractorDelegate? { get set }
    
    func deleteStory(_ request: [String: Any]) async
}

protocol StorySettingInteractorDelegate: BaseInteractorDelegate {
    func didDeleteStory()
}

protocol StorySettingRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: StorySettingRoutes)
}

enum StorySettingRoutes {
    case dismiss(delegate: StorySettingDelegate?, model: StoryDetail)
}

protocol StorySettingDelegate: AnyObject {
    func deleteStory(_ storyDetail: StoryDetail)
}
